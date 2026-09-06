package com.epam.gym.engagement.service.impl;

import com.epam.gym.engagement.dao.impl.TrainingDao;
import com.epam.gym.engagement.model.Trainee;
import com.epam.gym.engagement.model.Trainer;
import com.epam.gym.engagement.model.Training;
import com.epam.gym.engagement.service.TrainingService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingServiceImpl implements TrainingService {

    @Setter(onMethod_ = {@Autowired})
    private TrainingDao trainingDao;

    @Override
    public Training createTraining(Training training) {
        trainingDao.save(training.getId(), training);
        return training;
    }

    @Override
    public List<Training> selectTrainingsByTrainer(Trainer trainer) {
        return trainingDao.getAll().stream()
                .filter(training -> training.getTrainerId().equals(trainer.getUserId()))
                .toList();
    }

    @Override
    public List<Training> selectTrainingsByTrainee(Trainee trainee) {
        return trainingDao.getAll().stream()
                .filter(training -> training.getTraineeId().equals(trainee.getUserId()))
                .toList();
    }

    @Override
    public List<Training> selectTrainingsById(Long id) {
        return trainingDao.getAll().stream()
                .filter(training -> training.getId().equals(id))
                .toList();
    }
}
