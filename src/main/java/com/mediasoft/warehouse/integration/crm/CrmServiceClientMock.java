package com.mediasoft.warehouse.integration.crm;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Component
@ConditionalOnProperty("rest.crm-service.mock.enabled")
@Primary
public class CrmServiceClientMock implements CrmServiceClient{
    @Override
    public CompletableFuture<Map<String, String>> getInnByLogins(String[] logins) {
        Map<String, String> inn = new HashMap<>();
        for (String login : logins) {
            inn.put(login, generateRndAccountNum());
        }
        return CompletableFuture.supplyAsync(() -> inn);
    }

    private String generateRndAccountNum(){
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
