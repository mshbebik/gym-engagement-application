package com.gym.engagement.facade;

import com.gym.engagement.model.Trainee;
import com.gym.engagement.model.Trainer;
import com.gym.engagement.model.Training;

import java.util.List;

public interface GymFacade {
    Trainee createTrainee(Trainee trainee);
    Trainee updateTrainee(Trainee trainee);
    void deleteTrainee(Long id);
    Trainee selectTraineeById(Long id);

    Trainer createTrainer(Trainer trainer);
    Trainer updateTrainer(Trainer trainer);
    Trainer selectTrainerById(Long id);

    Training createTraining(Training training);
    Training selectTrainingById(Long id);
    List<Training> selectTrainingsByTrainer(Trainer trainer);
    List<Training> selectTrainingsByTrainee(Trainee trainee);
}
