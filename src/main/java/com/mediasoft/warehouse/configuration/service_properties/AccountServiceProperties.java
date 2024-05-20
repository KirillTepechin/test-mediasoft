package com.mediasoft.warehouse.configuration.service_properties;

import lombok.Data;

import java.util.Map;

@Data
public class AccountServiceProperties {
    private String host;
    private Map<String, String> methods;

    public String getAccountNumEndpoint(){
        return methods.get("get-account-num");
    }
}
