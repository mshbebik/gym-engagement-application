package com.gym.engagement.dao.impl;

import com.gym.engagement.dao.AbstractDao;
import com.gym.engagement.model.Training;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class TrainingDao extends AbstractDao<Training> {

    @Autowired
    public void setStorage(Map<Long, Training> storage) {
        super.setStorage(storage);
    }
}
