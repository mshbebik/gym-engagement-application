package com.gym.engagement.dao.impl;

import com.gym.engagement.dao.AbstractDao;
import com.gym.engagement.model.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class TraineeDao extends AbstractDao<Trainee> {

    @Autowired
    public void setStorage(Map<Long, Trainee> storage) {
        super.setStorage(storage);
    }

    public boolean existsByUsername(String username) {
        return storage.values().stream()
                .anyMatch(trainee -> trainee.getUserName().equals(username));
    }
}
