package com.cleverton.restql_usage.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PojoMapper {

    @Autowired
    private ObjectMapper objectMapper;

    public Object toPojo(final String json, final String typeField) {
        try {
            final JsonNode responseBodyTree = objectMapper.readTree(json);
            final String entityName = responseBodyTree.get(typeField).asText();

            return objectMapper.convertValue(responseBodyTree, Class.forName(entityName));
        } catch (final JsonProcessingException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}