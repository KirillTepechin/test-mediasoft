package com.mediasoft.warehouse.integration.currency;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class CurrencyRates {
    private BigDecimal CNY;
    private BigDecimal USD;
    private BigDecimal EUR;
}
