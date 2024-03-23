package com.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableScheduling
public class QuickStartSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuickStartSpringBootApplication.class, args);
    }

}
