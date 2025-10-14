package ru.Diana.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import ru.Diana.model.Event;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class EventConfig {
    @Bean
    @Scope(value = BeanDefinition.SCOPE_SINGLETON)
    public List<Event> eventContainer() {
        return new ArrayList<>();
    }
}
