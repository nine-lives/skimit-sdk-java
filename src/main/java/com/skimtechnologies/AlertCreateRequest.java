package com.skimtechnologies;

import java.util.Set;

public class AlertCreateRequest {
    private String title;
    private Set<String> keyTerms;

    public AlertCreateRequest withTitle(String title) {
        this.title = title;
        return this;
    }

    public AlertCreateRequest withKeyTerms(Set<String> keyTerms) {
        this.keyTerms = keyTerms;
        return this;
    }

    @Override
    public String toString() {
        return "AlertCreateRequest{" +
                "title='" + title + '\'' +
                ", keyTerms=" + keyTerms +
                '}';
    }
}
