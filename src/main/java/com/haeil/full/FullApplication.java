package com.haeil.full;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
@ComponentScan(basePackages = {"com.haeil.full", "com.haeil.be.chatbot"})
public class FullApplication {

    public static void main(String[] args) {
        SpringApplication.run(FullApplication.class, args);
    }
}
