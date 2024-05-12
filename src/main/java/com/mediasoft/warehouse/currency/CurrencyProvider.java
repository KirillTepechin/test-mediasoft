package com.mediasoft.warehouse.currency;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
@Getter
@Setter
public class CurrencyProvider {
    private Currency currentCurrency = Currency.RUB;
}
