package com.epam.gym.engagement.service;

import com.epam.gym.engagement.model.Trainee;
import com.epam.gym.engagement.model.Trainer;
import com.epam.gym.engagement.model.Training;

import java.util.List;

public interface TrainingService {

    Training createTraining(Training training);

    List<Training> selectTrainingsByTrainer(Trainer trainer);

    List<Training> selectTrainingsByTrainee(Trainee trainee);

    List<Training> selectTrainingsById(Long id);
}
