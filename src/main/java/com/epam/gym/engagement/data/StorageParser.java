package com.epam.gym.engagement.data;

import com.epam.gym.engagement.data.dto.StorageInitializationDTO;

public interface StorageParser {
    public StorageInitializationDTO parseSourceData(String sourceFilePath);
}
