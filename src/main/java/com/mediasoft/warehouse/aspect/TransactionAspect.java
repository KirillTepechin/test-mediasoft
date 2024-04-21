package com.mediasoft.warehouse.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Aspect
@Component
public class TransactionAspect implements TransactionSynchronization {

    long startTime = 0;

    @Before("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void registerTransactionSynchronization() {
        TransactionSynchronizationManager.registerSynchronization(this);
        startTime = System.currentTimeMillis();
    }

    @Override
    public void beforeCommit(boolean readOnly) {
        //startTime = System.currentTimeMillis();
    }

    @Override
    public void afterCommit() {
        System.out.printf("Transaction time: %d ms%n", System.currentTimeMillis() - startTime);
    }
}
