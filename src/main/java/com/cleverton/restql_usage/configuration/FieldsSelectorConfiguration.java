package com.cleverton.restql_usage.configuration;

import org.cleverton.selector.FieldsSelector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FieldsSelectorConfiguration {

    @Bean
    public FieldsSelector fieldsSelector() {
        return new FieldsSelector();
    }
}
