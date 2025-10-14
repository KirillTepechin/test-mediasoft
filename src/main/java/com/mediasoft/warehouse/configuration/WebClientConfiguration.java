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
    public WebClient currencyWebClient(){
        return WebClient.builder()
                .baseUrl(restConfigurationProperties.getCurrencyService().getHost())
                .build();
    }

    @Bean
    public WebClient accountWebClient(){
        return WebClient.builder()
                .baseUrl(restConfigurationProperties.getAccountService().getHost())
                .build();
    }

    @Bean
    public WebClient crmWebClient(){
        return WebClient.builder()
                .baseUrl(restConfigurationProperties.getCrmService().getHost())
                .build();
    }
}
