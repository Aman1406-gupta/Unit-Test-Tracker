package com.sprinklr.unittesttracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class UnitTestTrackerApplication {
    public static void main(String[] args) {
        SpringApplication.run(UnitTestTrackerApplication.class, args);
    }
}
