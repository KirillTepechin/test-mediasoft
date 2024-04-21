package com.mediasoft.warehouse.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExecutionTimeAspect {
    @Around("@annotation(com.mediasoft.warehouse.annotation.MeasureExecutionTime)")
    public Object measure(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        String methodName = signature.getName();
        String className = signature.getDeclaringType().getSimpleName();

        long startTime = System.currentTimeMillis();
        Object output = null;
        try {
            output = joinPoint.proceed();
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis() - startTime;
        System.out.printf("Execution time of %s method from the %s = %d ms%n", methodName, className, endTime);

        return output;
    }
}
