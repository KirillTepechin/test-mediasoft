package com.mediasoft.warehouse.configuration;

import lombok.Data;

import java.util.Map;
@Data
public class CurrencyServiceProperties {
    private String host;
    private Map<String, String> methods;

    public String getCurrenciesEndpoint(){
        return methods.get("get-currency");
    }
}
