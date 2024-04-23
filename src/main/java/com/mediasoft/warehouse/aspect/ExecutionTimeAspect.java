package com.mediasoft.warehouse.aspect;

import com.mediasoft.warehouse.exception.ProductNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ExecutionTimeAspect {
    @Around("@annotation(com.mediasoft.warehouse.annotation.MeasureExecutionTime)")
    public Object measure(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        String methodName = signature.getName();
        String className = signature.getDeclaringType().getSimpleName();

        long startTime = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        }
        finally {
            long endTime = System.currentTimeMillis() - startTime;
            log.info("Execution time of {} method from the {} = {} ms", methodName, className, endTime);
        }
    }
}
