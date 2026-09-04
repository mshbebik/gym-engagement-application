package com.epam.gym.engagement.configuration;

import com.epam.gym.engagement.data.EntityStorage;
import com.epam.gym.engagement.data.impl.InMemoryStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = "com.epam.gym.engagement")
@PropertySource("classpath:application.properties")
public class AppConfig {

    // In memory storage for entities
    @Bean
    public EntityStorage entityStorage() {
        return new InMemoryStorage();
    }
}
