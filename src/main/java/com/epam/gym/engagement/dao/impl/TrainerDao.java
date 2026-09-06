package com.epam.gym.engagement.dao.impl;

import com.epam.gym.engagement.dao.AbstractDao;
import com.epam.gym.engagement.data.EntityType;
import com.epam.gym.engagement.model.Trainee;
import com.epam.gym.engagement.model.Trainer;
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
