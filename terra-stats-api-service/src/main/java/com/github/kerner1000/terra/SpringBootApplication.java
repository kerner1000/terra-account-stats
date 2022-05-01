package com.github.kerner1000.terra;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@org.springframework.boot.autoconfigure.SpringBootApplication
@EnableFeignClients
public class SpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootApplication.class, args);
    }
}
