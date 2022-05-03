package com.github.kerner1000.terra.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@org.springframework.boot.autoconfigure.SpringBootApplication
public class SpringBootBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootBotApplication.class, args);
    }



}
