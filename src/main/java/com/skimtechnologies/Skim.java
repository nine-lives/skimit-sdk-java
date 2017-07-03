package com.skimtechnologies;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Skim {
    private int readingTimeOriginal;
    private int readingTimeSaved;

    private String uri;
    private String type;
    private String title;
    private String body;
    private String html;
    private List<String> images;
    private List<String> keywords;
    private Map<String, BigDecimal> subtopicProbabilities;

    public int getReadingTimeOriginal() {
        return readingTimeOriginal;
    }

    public int getReadingTimeSaved() {
        return readingTimeSaved;
    }

    public String getUri() {
        return uri;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getHtml() {
        return html;
    }

    public List<String> getImages() {
        return Collections.unmodifiableList(images);
    }

    public List<String> getKeywords() {
        return Collections.unmodifiableList(keywords);
    }

    public Map<String, BigDecimal> getSubtopicProbabilities() {
        return Collections.unmodifiableMap(subtopicProbabilities);
    }
}
