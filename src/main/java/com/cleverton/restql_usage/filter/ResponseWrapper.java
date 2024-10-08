package com.cleverton.restql_usage.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ResponseWrapper extends ContentCachingResponseWrapper {

    private static final String CLAZZ = "@class";

    private final ObjectMapper objectMapper;

    public ResponseWrapper(final ServletResponse response) {
        this(response, new ObjectMapper());
    }

    public ResponseWrapper(final ServletResponse response, final ObjectMapper objectMapper) {
        super((HttpServletResponse) response);
        this.objectMapper = objectMapper;
    }

    public Object readEntity() {
        return convertJsonToEntity(getJson());
    }

    private Object convertJsonToEntity(final String json) {
        try {
            final JsonNode entityTree = objectMapper.readTree(json);
            final String entityName = entityTree.get("@class").asText();

            return objectMapper.convertValue(entityTree, Class.forName(entityName));
        } catch (final JsonProcessingException | ClassNotFoundException e) {
            throw new ResponseProcessingException("Failed to convert JSON to entity", e);
        }
    }

    public void writeEntityWithSelectedFields(final Map<String, Object> entityWithSelectedFields) {
        try {
            writeResponse(objectMapper.writeValueAsString(entityWithSelectedFields));
        } catch (final JsonProcessingException e) {
            throw new ResponseProcessingException("Error writing entity with selected fields", e);
        }
    }

    public void writeEntityWithAllFields() {
        try {
            final JsonNode entityTree = objectMapper.readTree(getJson());
            removeClassField(entityTree);
            writeResponse(objectMapper.writeValueAsString(entityTree));
        } catch (final IOException e) {
            throw new ResponseProcessingException("Error writing entity with all fields", e);
        }
    }

    private String getJson() {
        return new String(getContentAsByteArray(), StandardCharsets.UTF_8);
    }

    private void removeClassField(final JsonNode node) {
        if (node.isObject()) {
            if (node.has(CLAZZ)) {
                ((ObjectNode) node).remove(CLAZZ);
            }

            node.fields().forEachRemaining(entry -> removeClassField(entry.getValue()));
        } else if (node.isArray()) {
            node.forEach(this::removeClassField);
        }
    }

    private void writeResponse(final String content) {
        try {
            resetBuffer();
            getWriter().write(content);
            copyBodyToResponse();
        } catch (final IOException e) {
            throw new ResponseProcessingException("Error writing response", e);
        }
    }
}