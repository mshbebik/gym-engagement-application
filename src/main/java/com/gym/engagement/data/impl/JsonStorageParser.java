package com.gym.engagement.data.impl;

import com.gym.engagement.data.StorageParser;
import com.gym.engagement.data.dto.StorageInitializationDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class JsonStorageParser implements StorageParser {
    private final ResourceLoader resourceLoader;

    @Override
    public StorageInitializationDTO parseSourceData(String sourceFilePath) {
        ObjectMapper objectMapper = new ObjectMapper();

        Resource resource = resourceLoader.getResource(sourceFilePath);

        try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readValue(inputStream, StorageInitializationDTO.class);
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to initialize storage from file: " + sourceFilePath, ex);
        }
    }
}
