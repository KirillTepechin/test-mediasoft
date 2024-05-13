package com.mediasoft.warehouse.currency.service;

import com.mediasoft.warehouse.currency.CurrencyRates;
import org.springframework.stereotype.Component;

@Component
public interface CurrencyServiceClient {
    CurrencyRates getCurrencyRates();
}
