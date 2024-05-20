package com.mediasoft.warehouse.configuration.service_properties;

import lombok.Data;

import java.util.Map;

@Data
public class CrmServiceProperties {
    private String host;
    private Map<String, String> methods;

    public String getINNEndpoint(){
        return methods.get("get-inn");
    }
}
