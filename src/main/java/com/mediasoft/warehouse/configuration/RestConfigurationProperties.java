package com.mediasoft.warehouse.configuration;

import com.mediasoft.warehouse.configuration.service_properties.AccountServiceProperties;
import com.mediasoft.warehouse.configuration.service_properties.CrmServiceProperties;
import com.mediasoft.warehouse.configuration.service_properties.CurrencyServiceProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "rest")
public class RestConfigurationProperties {
    private CurrencyServiceProperties currencyService;
    private AccountServiceProperties accountService;
    private CrmServiceProperties crmService;
}
