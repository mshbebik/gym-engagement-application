package com.epam.gym.engagement.dao;

import com.epam.gym.engagement.data.EntityStorage;
import com.epam.gym.engagement.data.EntityType;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Setter
public class AbstractDao<V> implements Dao<V> {
    private Map<Long, V> storage;

    @Override
    public Optional<V> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<V> getAll() {
        return (List<V>) storage.values();
    }

    @Override
    public void save(Long id, V entity) {
        storage.put(id, entity);
    }

    @Override
    public void update(Long id, V entity) {
        storage.put(id, entity);
    }

    @Override
    public void deleteById(Long id) {
        storage.remove(id);
    }
}
