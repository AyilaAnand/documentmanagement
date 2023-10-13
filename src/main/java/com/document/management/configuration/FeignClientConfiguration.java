package com.document.management.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;

import feign.RequestInterceptor;

public class FeignClientConfiguration {

    /**
     * Feign client interceptor updating the headers that will be passed in each request.
     * @return
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            requestTemplate.header("Accept", MediaType.APPLICATION_JSON_VALUE);
        };
    }
}