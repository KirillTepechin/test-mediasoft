package com.mediasoft.warehouse.integration.crm;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
public interface CrmServiceClient {
    CompletableFuture<Map<String, String>> getInnByLogins(String[] logins);
}
