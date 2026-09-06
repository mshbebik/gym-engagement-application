package com.gym.engagement.configuration;

import com.gym.engagement.data.EntityStorage;
import com.gym.engagement.data.EntityType;
import com.gym.engagement.model.Trainee;
import com.gym.engagement.model.Trainer;
import com.gym.engagement.model.Training;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class StorageConfig {

    @Bean
    public Map<Long, Trainee> traineeStorage(EntityStorage entityStorage) {
        return (Map<Long, Trainee>) (Map<?, ?>) entityStorage.getEntities(EntityType.TRAINEES);
    }

    @Bean
    public Map<Long, Trainer> trainerStorage(EntityStorage entityStorage) {
        return (Map<Long, Trainer>) (Map<?, ?>) entityStorage.getEntities(EntityType.TRAINERS);
    }

    @Bean
    public Map<Long, Training> trainingStorage(EntityStorage entityStorage) {
        return (Map<Long, Training>) (Map<?, ?>) entityStorage.getEntities(EntityType.TRAININGS);
    }
}

