package com.epam.gym.engagement.data;

import java.util.HashMap;
import java.util.Map;

public interface EntityStorage {
    public Map<Long, Object> getEntities(EntityType entityType);
}
