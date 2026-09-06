package com.gym.engagement.service;

import com.gym.engagement.model.Trainee;
import com.gym.engagement.model.Trainer;
import com.gym.engagement.model.Training;

import java.util.List;

public interface TrainingService {

    Training createTraining(Training training);

    List<Training> selectTrainingsByTrainer(Trainer trainer);

    List<Training> selectTrainingsByTrainee(Trainee trainee);

    List<Training> selectTrainingsById(Long id);
}
