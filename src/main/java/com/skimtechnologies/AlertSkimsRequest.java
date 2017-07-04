package com.skimtechnologies;

import com.skimtechnologies.util.RequestParameterMapper;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import java.util.StringJoiner;

public class AlertSkimsRequest {
    private Integer limit;
    private Integer since;
    private String topics;

    public AlertSkimsRequest withLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public AlertSkimsRequest withSince(Integer since) {
        this.since = since;
        return this;
    }

    public AlertSkimsRequest withTopics(Set<Topic> topics) {
        StringJoiner joiner = new StringJoiner(",");
        topics.forEach(o -> joiner.add(o.toString()));
        this.topics = joiner.toString();
        return this;
    }

    public static AlertSkimsRequest forNextPage(AlertSkimsPage page) {
        try {
            String next = page.getPagination().getNext();
            if (next == null) {
                return null;
            }

            if (!next.startsWith("http")) {
                next = "https://api.skimtechnologies.com/v2" + next;
            }
            return new RequestParameterMapper().read(new URL(next), AlertSkimsRequest.class);
        } catch (MalformedURLException e) {
            throw new SkimItException(e);
        }
    }

    @Override
    public String toString() {
        return "AlertSkimsRequest{" +
                "limit=" + limit +
                ", since=" + since +
                ", topics='" + topics + '\'' +
                '}';
    }
}
