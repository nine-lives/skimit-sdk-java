package com.skimtechnologies;

import com.fasterxml.jackson.core.type.TypeReference;
import com.skimtechnologies.client.HttpClient;

import java.util.List;
import java.util.Map;

/**
 * SkimIt SDK entry point
 */
public final class SkimIt {
    private final HttpClient client;

    private SkimIt(Configuration configuration) {
        this.client = new HttpClient(configuration);
    }

    /**
     * Get a SkimIt instance for your given client id and authentication token. To obtain an API token contact api-key@skim.it.
     * @param apiKey your client id
     * @return a SkimIt instance
     */
    public static SkimIt make(String apiKey) {
        return new SkimIt(new Configuration().withApiKey(apiKey));
    }

    /**
     * Get a SkimIt instance using finer grained control over configuration. To obtain an API token contact api-key@skim.it.
     * @param configuration your configuration
     * @return a SkimIt instance
     */
    public static SkimIt make(Configuration configuration) {
        return new SkimIt(configuration);
    }

    /**
     * A skim converts any webpage into a skim-readable card. The skim is designed to save users time when consuming
     * information, making the available content more readable and shareable.
     *
     * The skim is a summary of the content of the page with a category for the content. The number of different skim
     * genres, for now, includes News/Blog, Listicle, Video and Image.
     *
     * @param uri the uri to skim
     * @return a skim of the webpage
     */
    public Skim skim(String uri) {
        return client.get("skim", new UriRequest().withUri(uri), Skim.class);
    }


    /**
     * The Extractions API fetches a web page, removes boilerplate text and extracts the core information content of
     * the page.
     * @param uri the uri make an extract of
     * @return an extraction of the webpage
     */
    public Extraction extraction(String uri) {
        return client.get("extraction", new UriRequest().withUri(uri), Extraction.class);
    }

    /**
     * List of all available alerts
     * @return alerts
     */
    public List<Alert> alerts() {
        return client.get("alerts", null, new TypeReference<List<Alert>>() { });
    }

    public AlertSkimsPage alertSkims(String alertId, AlertSkimsRequest request) {
        return client.get(String.format("alerts/%s", alertId), request, AlertSkimsPage.class);
    }

    /**
     * Create an alert
     * @return the create alert
     */
    public Alert alertCreate(AlertCreateRequest request) {
        return client.post("alerts", request, Alert.class);
    }

    /**
     * Delete an alert
     * @param alertId the alert id to be deleted
     * @return the alert delete response
     */
    public boolean alertDelete(String alertId) {
        Map<String, String> response = client.delete(
                String.format("alerts/%s", alertId),
                null,
                new TypeReference<Map<String, String>>() { });
        return response.get("deleted").equals(alertId);
    }

    /**
     * Mark a skim of an alert as relevant or irrelevant
     * @param alertId the alert id
     * @param skimId the alert id
     * @param value the feedback value
     * @return the feedback summary
     */
    public AlertFeedback alertFeedback(String alertId, String skimId, AlertFeedbackValue value) {
        return client.put(
                String.format("alerts/%s/skims/%s/feedback/%d", alertId, skimId, value.getFeedbackValue()),
                null,
                AlertFeedback.class);
    }

}
