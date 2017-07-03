package com.skimtechnologies.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skimtechnologies.Configuration;
import com.skimtechnologies.SkimItError;
import com.skimtechnologies.SkimItException;
import com.skimtechnologies.SkimItServerException;
import com.skimtechnologies.util.ObjectMapperFactory;
import com.skimtechnologies.util.RateLimiter;
import com.skimtechnologies.util.RequestParameterMapper;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

public class HttpClient {
    private static final String HEADER_API_KEY = "x-api-key";
    private static final String HEADER_USER_AGENT = "User-Agent";
    private static final int TIMEOUT_MILLIS = -1;

    private final RequestParameterMapper parameterMapper = new RequestParameterMapper();
    private final ObjectMapper objectMapper = ObjectMapperFactory.make();
    private final Configuration configuration;
    private final CloseableHttpClient httpClient;
    private final ThreadLocal<HttpClientContext> httpContext = new ThreadLocal<>();
    private final RateLimiter rateLimiter;

    public HttpClient(Configuration configuration) {
        this.configuration = configuration;
        this.httpClient = makeHttpClient(configuration);
        this.rateLimiter = new RateLimiter(configuration.getRequestsPerSecond(), configuration.getRequestBurstSize());
    }

    public <T> T get(String path, Object parameters, Class<T> responseType) {
        try {
            return objectMapper.readValue(execute(new HttpGet(getUri(path, parameters))), responseType);
        } catch (IOException | URISyntaxException e) {
            throw new SkimItException(e);
        }
    }

    public <T> T put(String path, Object parameters, Class<T> responseType) {
        try {
            return objectMapper.readValue(execute(new HttpPut(getUri(path, parameters))), responseType);
        } catch (IOException | URISyntaxException e) {
            throw new SkimItException(e);
        }
    }

    public <T> T get(String path, Object parameters, TypeReference<T> responseType) {
        try {
            return objectMapper.readValue(execute(new HttpGet(getUri(path, parameters))), responseType);
        } catch (IOException | URISyntaxException e) {
            throw new SkimItException(e);
        }
    }

    public <T> T post(String path, Object data, Class<T> responseType) {
        try {
            HttpPost request = setPayload(new HttpPost(getUri(path, null)), data);
            return objectMapper.readValue(execute(request), responseType);
        } catch (IOException | URISyntaxException e) {
            throw new SkimItException(e);
        }
    }

    public <T> T delete(String path, Object parameters, TypeReference<T> responseType) {
        try {
            return objectMapper.readValue(execute(new HttpDelete(getUri(path, parameters))), responseType);
        } catch (IOException | URISyntaxException e) {
            throw new SkimItException(e);
        }
    }

    private byte[] execute(HttpUriRequest request) throws IOException {
        if (configuration.isBlockTillRateLimitReset()) {
            rateLimiter.blockTillRateLimitReset();
        }

        request.addHeader(HEADER_API_KEY, configuration.getApiKey());
        request.addHeader(HEADER_USER_AGENT, configuration.getUserAgent());
        request.addHeader("Accepts", "application/json");

        try (CloseableHttpResponse response = httpClient.execute(request, getHttpContext())) {
            if (response.getStatusLine().getStatusCode() >= 400) {
                throwError(response);
            }

            return readEntity(response);
        }
    }

    private <T extends HttpEntityEnclosingRequest> T setPayload(T request, Object payload) throws JsonProcessingException, UnsupportedEncodingException {
        StringEntity entity = new StringEntity(objectMapper.writeValueAsString(payload));
        entity.setContentType("application/json");
        request.setEntity(entity);
        return request;
    }

    private void throwError(CloseableHttpResponse response) {
        try {
            byte[] content = readEntity(response);
            throw new SkimItServerException(
                    response.getStatusLine().getStatusCode(),
                    response.getStatusLine().getReasonPhrase(),
                    objectMapper.readValue(content, SkimItError.class));
        } catch (IOException ignore) {
            throw new SkimItServerException(
                    response.getStatusLine().getStatusCode(),
                    response.getStatusLine().getReasonPhrase(),
                    null);
        }
    }

    private byte[] readEntity(CloseableHttpResponse response) throws IOException {
        if (response.getEntity() == null) {
            return null;
        }
        return EntityUtils.toByteArray(response.getEntity());
    }


    private CloseableHttpClient makeHttpClient(Configuration configuration) {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(configuration.getMaxConnectionsPerRoute());
        connectionManager.setDefaultMaxPerRoute(configuration.getMaxConnectionsPerRoute());
        return HttpClients.custom().setConnectionManager(connectionManager).build();
    }

    private HttpClientContext getHttpContext() {
        HttpClientContext context = httpContext.get();
        if (context == null) {
            context = HttpClientContext.create();
            context.setRequestConfig(RequestConfig.custom()
                    .setSocketTimeout(TIMEOUT_MILLIS)
                    .setConnectTimeout(TIMEOUT_MILLIS)
                    .setConnectionRequestTimeout(TIMEOUT_MILLIS)
                    .build());
            httpContext.set(context);
        }
        return context;
    }

    private URI getUri(String path, Object params) throws URISyntaxException {
        StringBuilder uri = new StringBuilder(configuration.getEndpoint())
                .append("/")
                .append(path);

        if (params != null) {
            uri.append(parameterMapper.write(params));
        }

        return new URI(uri.toString());
    }
}
