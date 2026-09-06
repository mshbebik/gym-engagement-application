package com.gym.engagement.dao;

import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Setter
public class AbstractDao<V> implements Dao<V> {
    protected Map<Long, V> storage;

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
