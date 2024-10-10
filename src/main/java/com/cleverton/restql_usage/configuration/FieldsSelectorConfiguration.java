package com.cleverton.restql_usage.configuration;

import io.github.cleverton.heusner.query.RestQlQuery;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FieldsSelectorConfiguration {

    @Bean
    public RestQlQuery restQlQuery() {
        return new RestQlQuery();
    }
}
