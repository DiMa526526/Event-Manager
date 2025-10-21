package ru.Diana.NauJava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "ru.Diana")
@EntityScan("ru.Diana.entity")
@EnableJpaRepositories(basePackages = "ru.Diana.repository")
public class NauJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(NauJavaApplication.class, args);
    }
}