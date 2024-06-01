package com.mediasoft.warehouse.properties;

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
