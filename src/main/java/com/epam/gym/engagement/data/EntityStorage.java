package com.epam.gym.engagement.data;

import java.util.HashMap;
import java.util.Map;

public interface EntityStorage {
    Map<Long, Object> getEntities(EntityType entityType);
}
