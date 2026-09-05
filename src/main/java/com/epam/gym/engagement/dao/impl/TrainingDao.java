package com.epam.gym.engagement.dao.impl;

import com.epam.gym.engagement.dao.AbstractDao;
import com.epam.gym.engagement.data.EntityType;
import com.epam.gym.engagement.model.Trainee;
import com.epam.gym.engagement.model.Training;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class TrainingDao extends AbstractDao<Training> {

    @Autowired
    public void setStorage(Map<Long, Training> storage) {
        super.setStorage(storage);
    }
}
