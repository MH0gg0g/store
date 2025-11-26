package com.example.store.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Slf4j
@Configuration
public class PerformanceTrackingAspect {

    @Around("@annotation(com.example.store.config.TrackTime)")
    public Object trackTime(ProceedingJoinPoint joinPoint) throws Throwable {
        Long startTime = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        Long endTime = System.currentTimeMillis();
        log.info("Method {} executed in {} ms", joinPoint.getSignature(), (endTime - startTime));
        return proceed;
    }
}