package com.eventlottery.randomservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
// @EnableEurekaClient
public class RandomServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(RandomServiceApplication.class, args);
    }
} 