package com.gym.engagement.service.impl;

import com.gym.engagement.dao.impl.TrainingDao;
import com.gym.engagement.exception.EntityNotFoundException;
import com.gym.engagement.model.Trainee;
import com.gym.engagement.model.Trainer;
import com.gym.engagement.model.Training;
import com.gym.engagement.service.TrainingService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class TrainingServiceImpl implements TrainingService {

    @Setter(onMethod_ = {@Autowired})
    private TrainingDao trainingDao;

    @Override
    public Training createTraining(Training training) {
        Objects.requireNonNull(training);
        trainingDao.save(training.getId(), training);
        return training;
    }

    @Override
    public List<Training> selectTrainingsByTrainer(Trainer trainer) {
        Objects.requireNonNull(trainer);
        return trainingDao.getAll().stream()
                .filter(training -> training.getTrainerId().equals(trainer.getUserId()))
                .toList();
    }

    @Override
    public List<Training> selectTrainingsByTrainee(Trainee trainee) {
        Objects.requireNonNull(trainee);
        return trainingDao.getAll().stream()
                .filter(training -> training.getTraineeId().equals(trainee.getUserId()))
                .toList();
    }

    @Override
    public Training selectTrainingById(Long id) {
        Objects.requireNonNull(id);
        return trainingDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Training with id: %d was not found".formatted(id)));
    }
}
