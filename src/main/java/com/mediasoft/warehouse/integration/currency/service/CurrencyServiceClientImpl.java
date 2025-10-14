package com.mediasoft.warehouse.integration.currency.service;

import com.mediasoft.warehouse.configuration.RestConfigurationProperties;
import com.mediasoft.warehouse.integration.currency.CurrencyRates;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyServiceClientImpl implements CurrencyServiceClient{
    private final WebClient currencyWebClient;
    private final RestConfigurationProperties restConfigurationProperties;
    @Override
    @Cacheable(unless = "#result == null", cacheNames = "currencyRates")
    public CurrencyRates getCurrencyRates() {
        log.info("Get rates from currency service");
        return currencyWebClient.get()
                .uri(restConfigurationProperties.getCurrencyService().getCurrenciesEndpoint())
                .retrieve()
                .bodyToMono(CurrencyRates.class)
                .retry(2)
                .block();
    }
}
