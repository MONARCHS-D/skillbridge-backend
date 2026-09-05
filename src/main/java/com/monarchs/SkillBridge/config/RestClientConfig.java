package com.monarchs.SkillBridge.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient embeddingRestClient(
            @Value("${embedding.service.url}") String url
    ) {
        return RestClient.builder()
                .baseUrl(url)
                .build();
    }

    @Bean
    public RestClient brevoRestClient() {

        return RestClient.builder()
                .baseUrl("https://api.brevo.com")
                .build();
    }

}
