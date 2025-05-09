package com.example.demo;

import com.example.demo.dto.OpenAIProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(OpenAIProperties properties) {
        return WebClient.builder()
            .baseUrl(properties.getUrl())
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + properties.getKey())
            .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
            .build();
    }
}
