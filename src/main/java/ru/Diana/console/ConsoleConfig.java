package ru.Diana.console;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Scanner;

@Configuration
public class ConsoleConfig {

    private final CommandProcessor commandProcessor;

    @Value("${app.name}")
    private String appName;

    @Value("${app.version}")
    private String appVersion;

    @Autowired
    public ConsoleConfig(CommandProcessor commandProcessor) {
        this.commandProcessor = commandProcessor;
    }

    @PostConstruct
    public void printAppInfo() {
        System.out.println("Приложение: " + appName);
        System.out.println("Версия: " + appVersion);
    }

    @Bean
    public CommandLineRunner commandScanner(CommandProcessor commandProcessor) {
        return args -> {
            try (Scanner scanner = new Scanner(System.in)) {
                System.out.println("Введите команду. 'exit' для выхода.");

                while (true) {
                    System.out.print("> ");
                    String input = scanner.nextLine().trim();

                    if ("exit".equalsIgnoreCase(input)) {
                        System.out.println("Завершение работы программы...");
                        break;
                    }

                    if (!input.isEmpty()) {
                        commandProcessor.processCommand(input);
                    }
                }
            }
        };
    }
}
