package com.gym.engagement.data.impl;

import com.gym.engagement.data.EntityStorage;
import com.gym.engagement.data.EntityType;

import java.util.HashMap;
import java.util.Map;

public class InMemoryStorage implements EntityStorage {
    private final Map<EntityType, Map<Long, Object>> storage = new HashMap<>();

    public InMemoryStorage() {
        storage.put(EntityType.TRAINEES, new HashMap<>());
        storage.put(EntityType.TRAINERS, new HashMap<>());
        storage.put(EntityType.TRAININGS, new HashMap<>());
    }

    @Override
    public Map<Long, Object> getEntities(EntityType entityType) {
        return storage.get(entityType);
    }
}
