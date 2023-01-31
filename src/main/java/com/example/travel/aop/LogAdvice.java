package com.example.travel.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
public class LogAdvice {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Around("execution(* com.example.travel.controller..*Controller.*(..))" +
            "|| execution(* com.example.travel.service..*Service.*(..))" +
            "|| execution(* com.example.travel.repository..*Repository.*(..))"
    )
    public Object logAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String className = proceedingJoinPoint.getSignature().getDeclaringTypeName(); // 클래스명
        String methodName = proceedingJoinPoint.getSignature().getName();             // 메서드명
        String parameter = Arrays.toString(proceedingJoinPoint.getArgs());            // 파라미터

        logger.info("===========================[[START]]===========================");
        logger.info("" + className + "." + methodName + "() CALLED");
        logger.info("PARAMETERS : " + parameter);
        logger.info("============================[[END]]============================");

        return proceedingJoinPoint.proceed();
    }
}