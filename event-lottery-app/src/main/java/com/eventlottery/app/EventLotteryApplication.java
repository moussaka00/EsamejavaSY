package com.eventlottery.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EventLotteryApplication {
    public static void main(String[] args) {
        SpringApplication.run(EventLotteryApplication.class, args);
    }
}
