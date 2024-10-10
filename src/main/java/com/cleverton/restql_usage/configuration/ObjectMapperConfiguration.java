package com.cleverton.restql_usage.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import io.github.cleverton.heusner.restql.RestQlTypeResolverBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        final var mapper = new ObjectMapper();
        mapper.setDefaultTyping(restQlTypeResolverBuilder());
        return mapper;
    }

    private StdTypeResolverBuilder restQlTypeResolverBuilder() {
        return new RestQlTypeResolverBuilder(BasicPolymorphicTypeValidator.builder()
                .allowIfBaseType(Object.class)
                .build()
        ).init();
    }
}