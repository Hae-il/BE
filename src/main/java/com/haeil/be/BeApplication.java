package com.haeil.be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@EnableJpaAuditing
@SpringBootApplication
public class BeApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeApplication.class, args);
    }

    @Bean
    public CommandLineRunner removeCheckConstraint(JdbcTemplate jdbcTemplate) {
        return args -> {
            try {
                jdbcTemplate.execute("ALTER TABLE cases DROP CHECK cases_chk_1");
                System.out.println("Successfully removed check constraint cases_chk_1");
            } catch (Exception e) {
                System.out.println("Check constraint might not exist or already removed: " + e.getMessage());
            }
        };
    }
}
