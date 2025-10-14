package com.mediasoft.warehouse.integration.account;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Component
@ConditionalOnProperty("rest.account-service.mock.enabled")
@Primary
public class AccountServiceClientMock implements AccountServiceClient{
    public CompletableFuture<Map<String, String>> getAccountNumbersByLogins(String[] logins) {
        Map<String, String> accountNumbers = new HashMap<>();

        for (String login : logins) {
            accountNumbers.put(login, generateRndAccountNum());
        }
        return CompletableFuture.supplyAsync(() -> accountNumbers);
    }

    private String generateRndAccountNum(){
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }
}
