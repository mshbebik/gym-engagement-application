package com.epam.gym.engagement.data;

import com.epam.gym.engagement.data.dto.StorageInitializationDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Component
public class StorageInitializerBeanPostProcessor implements BeanPostProcessor {

    @Value("${data.source.file.path}")
    private String sourceFilePath;

    private final ResourceLoader resourceLoader;

    public StorageInitializerBeanPostProcessor(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {

        if (bean instanceof EntityStorage entityStorage) {
            StorageInitializationDTO storageInitDto = parseSourceData();
            if (storageInitDto != null) {
                populateStorage(entityStorage, storageInitDto);
            }
        }

        return bean;
    }

    private StorageInitializationDTO parseSourceData() {
        ObjectMapper objectMapper = new ObjectMapper();

        Resource resource = resourceLoader.getResource(sourceFilePath);

        try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readValue(inputStream, StorageInitializationDTO.class);
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to initialize storage from file: " + sourceFilePath, ex);
        }
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
