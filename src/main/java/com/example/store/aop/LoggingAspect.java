package com.example.store.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Slf4j
@Component
public class LoggingAspect {

    private final ObjectMapper mapper;

    public LoggingAspect() {
        this.mapper = new ObjectMapper();
        this.mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Around("@annotation(com.example.store.aop.Loggable)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        var signature = (MethodSignature) joinPoint.getSignature();
        String methodSig = signature.toShortString();

        var args = joinPoint.getArgs();
        String argsJson = safeSerialize(args);

        String userId = extractUserId();

        log.info("Entering {} - args={} user={}", methodSig, argsJson, userId);

        Long start = System.currentTimeMillis();
        try {
            var result = joinPoint.proceed();
            Long duration = System.currentTimeMillis() - start;
            String resultJson = safeSerialize(result);
            log.info("Exiting {} - success=true duration={}ms return={}", methodSig, duration, resultJson);
            return result;
        } catch (Throwable ex) {
            Long duration = System.currentTimeMillis() - start;
            log.error("Exiting {} - success=false duration={}ms error={}", methodSig, duration, ex.toString());
            throw ex;
        }
    }

    private String safeSerialize(Object value) {
        if (value == null)
            return "null";
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            return String.valueOf(value);
        }
    }

    private String extractUserId() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()) {
                return "Anoymous";
            }
            return auth.getName();

        } catch (Exception ex) {
            return "Anoymous";
        }
    }

}
