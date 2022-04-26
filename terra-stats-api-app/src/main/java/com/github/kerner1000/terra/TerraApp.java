package com.github.kerner1000.terra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TerraApp {


    public static void main(String[] args) {
        SpringApplication.run(TerraApp.class, args);
    }


}
