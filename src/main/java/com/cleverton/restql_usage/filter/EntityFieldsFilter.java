package com.cleverton.restql_usage.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cleverton.heusner.query.RestQlQuery;
import io.github.cleverton.heusner.restql.RestQlResponseWrapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.instancio.internal.util.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static io.github.cleverton.heusner.query.RestQlQuery.FIELDS;

@Component
public class EntityFieldsFilter implements Filter {

    private final RestQlQuery restQlQuery;
    private final ObjectMapper objectMapper;

    public EntityFieldsFilter(final RestQlQuery restQlQuery, final ObjectMapper objectMapper) {
        this.restQlQuery = restQlQuery;
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
                final var entityWithSelectedFields = restQlQuery.select(fields).from(restQlResponseWrapper.readEntity());
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