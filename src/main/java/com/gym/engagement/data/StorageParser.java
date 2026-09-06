package com.gym.engagement.data;

import com.gym.engagement.data.dto.StorageInitializationDTO;

public interface StorageParser {
    StorageInitializationDTO parseSourceData(String sourceFilePath);
}
