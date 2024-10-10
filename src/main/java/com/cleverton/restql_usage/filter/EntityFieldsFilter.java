package com.cleverton.restql_usage.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cleverton.heusner.restql.RestQlResponseWrapper;
import io.github.cleverton.heusner.selector.FieldsSelector;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.instancio.internal.util.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static io.github.cleverton.heusner.selector.FieldsSelector.FIELDS;

@Component
public class EntityFieldsFilter implements Filter {

    private final FieldsSelector fieldsSelector;
    private final ObjectMapper objectMapper;

    public EntityFieldsFilter(final FieldsSelector fieldsSelector, final ObjectMapper objectMapper) {
        this.fieldsSelector = fieldsSelector;
        this.objectMapper = objectMapper;
    }

    @Override
    public void doFilter(final ServletRequest request,
                         final ServletResponse response,
                         final FilterChain chain)
            throws IOException, ServletException {

        final String fields = request.getParameter(FIELDS);
        final var restQlResponseWrapper = new RestQlResponseWrapper(response, objectMapper);
        chain.doFilter(request, restQlResponseWrapper);

        if (((HttpServletRequest) request).getRequestURI().contains("/fields-selection-with-filter")) {
            if (!StringUtils.isBlank(fields)) {
                final var entityWithSelectedFields = fieldsSelector.from(restQlResponseWrapper.readEntity()).select(fields);
                restQlResponseWrapper.writeEntityWithSelectedFields(entityWithSelectedFields);
            }
            else {
                restQlResponseWrapper.writeEntityWithAllFields();
            }
        }
        else {
            chain.doFilter(request, response);
        }
    }
}