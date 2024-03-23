package com.app.auditlog;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Arrays;
import java.util.Map;

@Aspect
@Component
public class RequestHistory {
    private final HttpServletRequest request;
    private final ObjectMapper objectMapper;

    public RequestHistory(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @Pointcut("execution(* com.app.controller.HomeController.forHistory(..))")
    private void specificMethod() {
        // Pointcut definition for your specific method
    }

    @AfterReturning("specificMethod()")
    public void logAfterExecution(JoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("methodName:- " + methodName);
        String httpMethod = RequestMethod.valueOf(request.getMethod()).name();
        System.out.println("httpMethod:- " + httpMethod);
        String endpoint = joinPoint.getTarget().getClass().getName() + "." + methodName;
        System.out.println("endpoint:- " + endpoint);
        String parameters = getRequestParameters(request);
        System.out.println("parameters:- " + parameters);
        String requestBody = getRequestBody(joinPoint);
        System.out.println("requestBody:- " + requestBody);
    }

    private String getRequestParameters(HttpServletRequest request) {
        Map<String, String[]> paramMap = request.getParameterMap();
        if (paramMap.isEmpty()) {
            return "No parameters";
        }
        StringBuilder parameters = new StringBuilder();
        for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
            parameters.append(entry.getKey()).append("=").append(Arrays.toString(entry.getValue())).append(",");
        }
        parameters.deleteCharAt(parameters.length() - 1); // Remove trailing comma
        return parameters.toString();
    }

    private String getRequestBody(JoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg != null && !(arg instanceof HttpServletRequest)) {
                // Convert the request body object to JSON
                return objectMapper.writeValueAsString(arg);
            }
        }
        return "No request body";
    }
}
