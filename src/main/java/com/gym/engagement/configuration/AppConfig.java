package com.gym.engagement.configuration;

import com.gym.engagement.data.EntityStorage;
import com.gym.engagement.data.impl.InMemoryStorage;
import com.gym.engagement.factory.ModelMapperFactory;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(basePackages = "com.gym.engagement")
@PropertySource("classpath:application.properties")
public class AppConfig {

    @Bean
    public EntityStorage entityStorage() {
        return new InMemoryStorage();
    }

    @Bean
    public ModelMapper modelMapper() {
        return ModelMapperFactory.create();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
