package com.app.audit;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ApplicationLogging {

    @Before("execution(* com.app.controller.*.*(..))")
    public void beforeControllerMethodExecution() {
        System.out.println("Logging: Controller method is about to execute...");
    }
    @After("execution(* com.app.controller.*.*(..))")
    public void afterControllerMethodExecution() {
        System.out.println("Logging: Controller method after execute...");
    }


    @AfterThrowing(pointcut = "execution(* com.app..*.*(..))", throwing = "exception")
    public void logAfterThrowingAllMethods(Exception exception) {
        System.out.println("Exception occurred: " + exception.getMessage());
    }


}
