package com.nus.project.capstone.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.nus.project.capstone.idc.repository.*")
@ComponentScan(basePackages = { "com.nus.project.capstone.idc.repository.*" })
@EntityScan("com.nus.project.capstone.idc.repository.*")
public class InfoCollectionSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(InfoCollectionSystemApplication.class, args);
    }

}
