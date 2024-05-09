package com.mediasoft.warehouse.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfiguration {
    private final RestConfigurationProperties restConfigurationProperties;
    @Bean
    public WebClient webClient(){
        return WebClient.builder()
                .baseUrl(restConfigurationProperties.getCurrencyService().getHost())
                .build();
    }
}
