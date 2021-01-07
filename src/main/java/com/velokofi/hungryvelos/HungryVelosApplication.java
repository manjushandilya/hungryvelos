package com.velokofi.hungryvelos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HungryVelosApplication {

    public static void main(String[] args) {
        SpringApplication.run(HungryVelosApplication.class, args);
    }

}
