package com.mediasoft.warehouse.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "rest.currency-service")
public class RestConfigurationProperties {
    @Getter
    @Setter
    private String host;
    @Setter
    private Map<String, String> methods;

    public String getCurrenciesEndpoint(){
        return methods.get("get-currency");
    }
}
