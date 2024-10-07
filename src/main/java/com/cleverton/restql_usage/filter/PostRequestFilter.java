package com.cleverton.restql_usage.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import org.cleverton.selector.FieldsSelector;
import org.instancio.internal.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.cleverton.selector.FieldsSelector.FIELDS;

@Component
public class PostRequestFilter implements Filter {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FieldsSelector fieldsSelector;

    @Autowired
    private PojoMapper pojoMapper;

    @Override
    public void doFilter(final ServletRequest request,
                         final ServletResponse response,
                         final FilterChain chain)
            throws IOException, ServletException {

        final String fields = request.getParameter(FIELDS);

        if (!StringUtils.isBlank(fields)) {
            final var responseWrapper = new CustomResponseWrapper(response);
            chain.doFilter(request, responseWrapper);
            final Object entity = pojoMapper.toPojo(responseWrapper.getContentAsString(), "@type");
            final var selectedFields = fieldsSelector.from(entity).select(fields);

            responseWrapper.overwriteContent(objectMapper.writeValueAsString(selectedFields));
        }
        else {
            chain.doFilter(request, response);
        }
    }
}