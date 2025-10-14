package ru.Diana.NauJava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс Spring Boot приложения для управления событиями
 * Практическая работа 3 - Spring Framework
 */

@SpringBootApplication(scanBasePackages = "ru.Diana")
public class NauJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(NauJavaApplication.class, args);
    }
}
