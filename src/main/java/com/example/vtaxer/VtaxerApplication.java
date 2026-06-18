package com.example.vtaxer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class VtaxerApplication {

    public static void main(String[] args) {
        SpringApplication.run(VtaxerApplication.class, args);
    }
}
