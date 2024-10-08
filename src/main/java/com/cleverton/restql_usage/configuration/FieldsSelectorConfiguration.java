package com.cleverton.restql_usage.configuration;

import io.github.cleverton.heusner.selector.FieldsSelector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FieldsSelectorConfiguration {

    @Bean
    public FieldsSelector fieldsSelector() {
        return new FieldsSelector();
    }
}
