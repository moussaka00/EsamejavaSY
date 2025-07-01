package com.eventlottery.touristservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class TouristServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(TouristServiceApplication.class, args);
    }
} 