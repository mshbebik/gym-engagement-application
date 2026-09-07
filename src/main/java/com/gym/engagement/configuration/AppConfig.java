package com.gym.engagement.configuration;

import com.gym.engagement.data.EntityStorage;
import com.gym.engagement.data.impl.InMemoryStorage;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = "com.epam.gym.engagement")
@PropertySource("classpath:application.properties")
public class AppConfig {

    @Bean
    public EntityStorage entityStorage() {
        return new InMemoryStorage();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
