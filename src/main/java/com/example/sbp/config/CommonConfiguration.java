package com.example.sbp.config;

import com.example.sbp.common.web.CommonObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfiguration {
    @Bean
    ObjectMapper objectMapper() {
        return new CommonObjectMapper();
    }
}
