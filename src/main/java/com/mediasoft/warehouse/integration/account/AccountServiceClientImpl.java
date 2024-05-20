package com.mediasoft.warehouse.integration.account;

import com.mediasoft.warehouse.configuration.RestConfigurationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class AccountServiceClientImpl implements AccountServiceClient{
    private final WebClient accountWebClient;
    private final RestConfigurationProperties restConfigurationProperties;
    @Override
    public CompletableFuture<Map<String, String>> getAccountNumbersByLogins(String[] logins){
        return accountWebClient.post()
                .uri(restConfigurationProperties.getAccountService().getAccountNumEndpoint())
                .body(BodyInserters.fromValue(logins))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .delayElement(Duration.ofSeconds(3))
                .toFuture();
    }
}
