package com.gym.engagement.facade.impl;

import com.gym.engagement.facade.GymFacade;
import com.gym.engagement.model.Trainee;
import com.gym.engagement.model.Trainer;
import com.gym.engagement.model.Training;
import com.gym.engagement.service.TraineeService;
import com.gym.engagement.service.TrainerService;
import com.gym.engagement.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GymFacadeImpl implements GymFacade {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    @Override
    public Trainee createTrainee(Trainee trainee) {
        return traineeService.createTrainee(trainee);
    }

    @Override
    public Trainee updateTrainee(Trainee trainee) {
        return traineeService.updateTrainee(trainee);
    }

    @Override
    public void deleteTrainee(Long id) {
        traineeService.deleteTrainee(id);
    }

    @Override
    public Trainee selectTraineeById(Long id) {
        return traineeService.selectTraineeById(id);
    }

    @Override
    public Trainer createTrainer(Trainer trainer) {
        return trainerService.createTrainer(trainer);
    }

    @Override
    public Trainer updateTrainer(Trainer trainer) {
        return trainerService.updateTrainer(trainer);
    }

    @Override
    public Trainer selectTrainerById(Long id) {
        return trainerService.selectTrainerById(id);
    }

    @Override
    public Training createTraining(Training training) {
        return trainingService.createTraining(training);
    }

    @Override
    public Training selectTrainingById(Long id) {
        return trainingService.selectTrainingById(id);
    }

    @Override
    public List<Training> selectTrainingsByTrainer(Trainer trainer) {
        return trainingService.selectTrainingsByTrainer(trainer);
    }

    @Override
    public List<Training> selectTrainingsByTrainee(Trainee trainee) {
        return trainingService.selectTrainingsByTrainee(trainee);
    }
}
