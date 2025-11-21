package com.haeil.full;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FullApplication {

    public static void main(String[] args) {
        SpringApplication.run(FullApplication.class, args);
    }
}
