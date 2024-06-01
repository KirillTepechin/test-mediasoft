package com.mediasoft.warehouse.integration.currency.service;

import com.mediasoft.warehouse.integration.currency.CurrencyRates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Random;

@Component
@ConditionalOnProperty("rest.currency-service.mock.enabled")
@Primary
@Slf4j
public class CurrencyServiceClientMock implements CurrencyServiceClient{
    @Override
    public CurrencyRates getCurrencyRates() {
        log.info("Mock service work");
        return new CurrencyRates(
                BigDecimal.valueOf(new Random().nextFloat()),
                BigDecimal.valueOf(new Random().nextFloat()),
                BigDecimal.valueOf(new Random().nextFloat())
        );
    }
}
