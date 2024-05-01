package com.mediasoft.warehouse.currency.service;

import com.mediasoft.warehouse.currency.CurrencyRates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Random;

@Component
@ConditionalOnProperty("currency-service.mock")
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
