package com.epam.gym.engagement.service.impl;

import com.epam.gym.engagement.model.Trainee;
import com.epam.gym.engagement.model.Trainer;
import com.epam.gym.engagement.model.Training;
import com.epam.gym.engagement.service.TrainingService;

import java.util.List;

public class TrainingServiceImpl implements TrainingService {
    @Override
    public Training createTraining(Training training) {
        return null;
    }

    @Override
    public List<Training> selectTrainingsByTrainer(Trainer trainer) {
        return List.of();
    }

    @Override
    public List<Training> selectTrainingsByTrainee(Trainee trainee) {
        return List.of();
    }
}
