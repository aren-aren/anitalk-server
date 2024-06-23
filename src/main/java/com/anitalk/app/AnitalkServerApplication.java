package com.anitalk.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AnitalkServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnitalkServerApplication.class, args);
    }

}
