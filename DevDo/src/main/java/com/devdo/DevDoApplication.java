package com.devdo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DevDoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevDoApplication.class, args);
    }

}
