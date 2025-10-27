package ru.Diana.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Event Management API")
                        .version("1.0")
                        .description("API для управления событиями и напоминаниями")
                        .contact(new Contact()
                                .name("Diana")
                                .email("support@example.com")));
    }
}