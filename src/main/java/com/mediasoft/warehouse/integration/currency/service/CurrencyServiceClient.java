package com.mediasoft.warehouse.integration.currency.service;

import com.mediasoft.warehouse.integration.currency.CurrencyRates;
import org.springframework.stereotype.Component;

@Component
public interface CurrencyServiceClient {
    CurrencyRates getCurrencyRates();
}
