package com.mediasoft.warehouse.integration.currency;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.mediasoft.warehouse.integration.currency.service.CurrencyServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

@Component
@Slf4j
@RequiredArgsConstructor
public class CurrencyRateProvider {

    private final CurrencyServiceClient currencyServiceClient;
    private final CurrencyProvider currencyProvider;

    public BigDecimal getCurrentCurrencyRate() {
        Currency currency = currencyProvider.getCurrentCurrency();
        CurrencyRates currencyRates;
        try {
            currencyRates = currencyServiceClient.getCurrencyRates();
        }
        catch (Exception e){
            log.info("Currency service does not respond after 2 retries");
            currencyRates = getCurrencyRatesFromFile();
        }
        return getRateByCurrency(currency, currencyRates);
    }
    private CurrencyRates getCurrencyRatesFromFile(){
        log.info("Get rates from file");
        ObjectMapper mapper = JsonMapper.builder().build();
        CurrencyRates currencyRates;
        try {
            currencyRates = mapper.readValue(new File("exchange-rate.json"), CurrencyRates.class);
        } catch (IOException e) {
            log.warn("Error parsing file 'exchange-rate.json'. Error message: {}", e.getMessage());
            throw new RuntimeException(e);
        }
        return currencyRates;
    }
    private BigDecimal getRateByCurrency(Currency currency, CurrencyRates currencyRates){
        return switch (currency) {
            case CNY -> currencyRates.getCNY();
            case EUR -> currencyRates.getEUR();
            case USD -> currencyRates.getUSD();
            default -> BigDecimal.valueOf(1L);
        };
    }
}
