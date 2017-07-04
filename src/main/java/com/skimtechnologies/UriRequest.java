package com.skimtechnologies;

public class UriRequest {
    private String uri;

    public UriRequest withUri(String uri) {
        this.uri = uri;
        return this;
    }

    @Override
    public String toString() {
        return "UriRequest{" +
                "uri='" + uri + '\'' +
                '}';
    }
}
