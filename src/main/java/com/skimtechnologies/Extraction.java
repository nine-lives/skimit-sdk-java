package com.skimtechnologies;

import java.util.List;

public class Extraction {
    private String uri;
    private String favicon;
    private String title;
    private String text;
    private String language;
    private String metaKeywords;
    private String metaDescription;
    private Object markup;
    private String date;
    private List<String> images;
    private List<String> videos;
    private String pageType;

    public String getUri() {
        return uri;
    }

    public String getFavicon() {
        return favicon;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getLanguage() {
        return language;
    }

    public String getMetaKeywords() {
        return metaKeywords;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public Object getMarkup() {
        return markup;
    }

    public List<String> getImages() {
        return images;
    }

    public List<String> getVideos() {
        return videos;
    }

    public String getPageType() {
        return pageType;
    }

    public String getDate() {
        return date;
    }
}
