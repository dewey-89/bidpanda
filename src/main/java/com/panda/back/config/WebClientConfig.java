package com.panda.back.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    private static String CRONJOB_URL = "https://api.cron-job.org";
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(CRONJOB_URL)
                .build();
    }
}
