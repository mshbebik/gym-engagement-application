package com.gym.engagement.data;

import com.gym.engagement.data.dto.StorageInitializationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class StorageInitializerBeanPostProcessor implements BeanPostProcessor {
    private StorageParser storageParser;

    @Value("${data.source.file.path}")
    private String sourceFilePath;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (!(bean instanceof EntityStorage entityStorage)) {
            return bean;
        }

        StorageInitializationDTO storageInitDto = storageParser.parseSourceData(sourceFilePath);
        if (storageInitDto == null) {
            return bean;
        }

        populateStorage(entityStorage, storageInitDto);
        return bean;
    }

    private void populateStorage(EntityStorage entityStorage, StorageInitializationDTO storageInitDto) {
        if (storageInitDto.getTrainees() != null) {
            Map<Long, Object> trainees = entityStorage.getEntities(EntityType.TRAINEES);
            storageInitDto.getTrainees().forEach(trainee -> trainees.put(trainee.getUserId(), trainee));
        }

        if (storageInitDto.getTrainers() != null) {
            Map<Long, Object> trainers = entityStorage.getEntities(EntityType.TRAINERS);
            storageInitDto.getTrainers().forEach(trainer -> trainers.put(trainer.getUserId(), trainer));
        }

        if (storageInitDto.getTrainings() != null) {
            Map<Long, Object> trainings = entityStorage.getEntities(EntityType.TRAININGS);
            storageInitDto.getTrainings().forEach(training -> trainings.put(training.getId(), training));
        }
    }
}
