package com.cleverton.restql_usage.filter;

import io.github.cleverton.heusner.selector.FieldsSelector;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.instancio.internal.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static io.github.cleverton.heusner.selector.FieldsSelector.FIELDS;

@Component
public class EntityFieldsFilter implements Filter {

    @Autowired
    private FieldsSelector fieldsSelector;

    @Override
    public void doFilter(final ServletRequest request,
                         final ServletResponse response,
                         final FilterChain chain)
            throws IOException, ServletException {

        final String fields = request.getParameter(FIELDS);
        final var responseWrapper = new ResponseWrapper(response);
        chain.doFilter(request, responseWrapper);

        if (((HttpServletRequest) request).getRequestURI().contains("/fields-selection-with-filter")) {
            if (!StringUtils.isBlank(fields)) {
                final var entityWithSelectedFields = fieldsSelector.from(responseWrapper.readEntity()).select(fields);
                responseWrapper.writeEntityWithSelectedFields(entityWithSelectedFields);
            }
            else {
                responseWrapper.writeEntityWithAllFields();
            }
        }
        else {
            chain.doFilter(request, response);
        }
    }
}