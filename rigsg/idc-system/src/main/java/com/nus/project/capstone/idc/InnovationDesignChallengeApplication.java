package com.nus.project.capstone.idc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = { "com.nus.project.capstone.model" })
@EntityScan("com.nus.project.capstone.model")
public class InnovationDesignChallengeApplication {

    public static void main(String[] args) {
        SpringApplication.run(InnovationDesignChallengeApplication.class, args);
    }
}
