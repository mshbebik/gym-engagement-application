package com.gym.engagement.dao.impl;

import com.gym.engagement.dao.AbstractDao;
import com.gym.engagement.model.Trainer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class TrainerDao extends AbstractDao<Trainer> {

    @Autowired
    public void setStorage(Map<Long, Trainer> storage) {
        super.setStorage(storage);
    }

    public boolean existsByUsername(String username) {
        return storage.values().stream()
                .anyMatch(trainer -> trainer.getUserName().equals(username));
    }
}
