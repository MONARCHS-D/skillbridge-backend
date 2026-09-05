package com.monarchs.SkillBridge.config;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.json.JsonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SystemClassConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return JsonMapper.builder()
                .configure(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS,false)
                .build();
    }
}