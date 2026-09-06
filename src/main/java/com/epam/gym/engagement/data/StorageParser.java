package com.epam.gym.engagement.data;

import com.epam.gym.engagement.data.dto.StorageInitializationDTO;

public interface StorageParser {
    StorageInitializationDTO parseSourceData(String sourceFilePath);
}
