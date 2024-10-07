package com.cleverton.restql_usage.filter;

import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CustomResponseWrapper extends ContentCachingResponseWrapper {

    public CustomResponseWrapper(final ServletResponse response) {
        super((HttpServletResponse) response);
    }

    public String getContentAsString() {
        return new String(getContentAsByteArray(), StandardCharsets.UTF_8);
    }

    public void overwriteContent(final String newContent) {
        resetBuffer();
        try {
            getWriter().write(newContent);
            copyBodyToResponse();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
