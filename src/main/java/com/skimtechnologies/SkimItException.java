package com.skimtechnologies;

public class SkimItException extends RuntimeException {

    public SkimItException(String message) {
        super(message);
    }

    public SkimItException(Throwable e) {
        super(e);
    }
}
