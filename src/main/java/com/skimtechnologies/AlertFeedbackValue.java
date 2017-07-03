package com.skimtechnologies;

public enum AlertFeedbackValue {
    IRRELEVANT(0),
    RELEVANT(1);

    private int feedbackValue;

    AlertFeedbackValue(int feedbackValue) {
        this.feedbackValue = feedbackValue;
    }

    public int getFeedbackValue() {
        return feedbackValue;
    }
}
