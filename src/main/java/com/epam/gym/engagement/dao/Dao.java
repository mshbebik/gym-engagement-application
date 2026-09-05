package com.epam.gym.engagement.dao;

import com.epam.gym.engagement.model.Trainee;

import java.util.List;
import java.util.Optional;

public interface Dao<V> {

    Optional<V> findById(Long id);

    List<V> getAll();

    void save(Long id, V entity);

    void update(Long id, V entity);

    void deleteById(Long id);
}
