package com.mediasoft.warehouse.integration.crm;

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
public class CrmServiceClientImpl implements CrmServiceClient{
    private final WebClient crmWebClient;
    private final RestConfigurationProperties restConfigurationProperties;
    @Override
    public CompletableFuture<Map<String, String>> getInnByLogins(String[] logins){
        return crmWebClient.post()
                .uri(restConfigurationProperties.getCrmService().getINNEndpoint())
                .body(BodyInserters.fromValue(logins))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .delayElement(Duration.ofSeconds(3))
                .toFuture();
    }
}
