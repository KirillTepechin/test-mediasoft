package com.mediasoft.warehouse.currency.service;

import com.mediasoft.warehouse.currency.CurrencyRates;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyServiceClientImpl implements CurrencyServiceClient{
    private final WebClient webClient = WebClient.create();
    @Value("${currency-service.host}")
    private String host;
    @Value("${currency-service.methods.get-currency}")
    private String endpoint;

    @Override
    @Cacheable(unless = "#result == null", cacheNames = "currencyRates")
    public CurrencyRates getCurrencyRates() {
        log.info("Get rates from currency service");
        return webClient.get()
                .uri(host + endpoint)
                .retrieve()
                .bodyToMono(CurrencyRates.class)
                .retry(2)
                .block();
    }
}
