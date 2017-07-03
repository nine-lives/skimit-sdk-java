package com.skimtechnologies;

public class SkimItServerException extends SkimItException {
    private final int statusCode;
    private final String statusMessage;
    private final SkimItError error;

    public SkimItServerException(int statusCode, String statusMessage, SkimItError error) {
        super(buildMessage(statusCode, statusMessage, error));
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.error = error;
    }

    /**
     * Get the HTTP status code
     * @return the status code
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Get the HTTP status message
     * @return the status message
     */
    public String getStatusMessage() {
        return statusMessage;
    }

    /**
     * Get the error returned by the server
     * @return the list of errors
     */
    public SkimItError getError() {
        return error;
    }

    private static String buildMessage(int statusCode, String statusMessage, SkimItError error) {
        StringBuilder sb = new StringBuilder();
        sb.append(statusCode).append(": ").append(statusMessage);

        if (error != null) {
            sb.append(" - ").append(error.getError());
        }

        return sb.toString();
    }
}
