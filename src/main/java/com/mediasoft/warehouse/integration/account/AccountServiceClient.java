package com.mediasoft.warehouse.integration.account;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
public interface AccountServiceClient {
    CompletableFuture<Map<String, String>> getAccountNumbersByLogins(String[] logins);
}
