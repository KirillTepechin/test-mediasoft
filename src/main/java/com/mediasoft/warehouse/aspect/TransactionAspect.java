package com.mediasoft.warehouse.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Aspect
@Component
@Slf4j
public class TransactionAspect implements TransactionSynchronization {

    long startTime = 0;

    @Before("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void registerTransactionSynchronization() {
        TransactionSynchronizationManager.registerSynchronization(this);
        startTime = System.currentTimeMillis();
    }

    @Override
    public void afterCommit() {
        log.info("Transaction time: {} ms", System.currentTimeMillis() - startTime);
    }
}
