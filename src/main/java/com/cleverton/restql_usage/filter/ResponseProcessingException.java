package com.cleverton.restql_usage.filter;

public class ResponseProcessingException extends RuntimeException {
    public ResponseProcessingException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
