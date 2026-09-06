package com.gym.engagement.data;

import java.util.Map;

public interface EntityStorage {
    Map<Long, Object> getEntities(EntityType entityType);
}
