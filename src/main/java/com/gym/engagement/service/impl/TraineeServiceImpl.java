package com.gym.engagement.service.impl;

import com.gym.engagement.dao.impl.TraineeDao;
import com.gym.engagement.dao.impl.TrainerDao;
import com.gym.engagement.exception.EntityNotFoundException;
import com.gym.engagement.model.Trainee;
import com.gym.engagement.service.TraineeService;
import com.gym.engagement.service.common.UserCredentialsManager;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TraineeServiceImpl implements TraineeService {

    @Setter(onMethod_ = {@Autowired})
    private UserCredentialsManager credentialsManager;

    @Setter(onMethod_ = {@Autowired})
    private TrainerDao trainerDao;

    @Setter(onMethod_ = {@Autowired})
    private TraineeDao traineeDao;

    @Override
    public Trainee createTrainee(Trainee trainee) {
        String username = credentialsManager.generateUsername(trainee.getFirstName(),
                trainee.getLastName(),
                u -> traineeDao.existsByUsername(u) || trainerDao.existsByUsername(u));

        Trainee newTrainee = enrichTrainee(trainee, username, credentialsManager.generateRandomPassword());

        traineeDao.save(newTrainee.getUserId(), newTrainee);
        return newTrainee;
    }

    @Override
    public Trainee updateTrainee(Trainee trainee) {
        traineeDao.save(trainee.getUserId(), trainee);
        return trainee;
    }

    @Override
    public void deleteTrainee(Long id) {
        traineeDao.deleteById(id);
    }

    @Override
    public Trainee selectTraineeById(Long id) {
        return traineeDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Trainee with id: %d was not found".formatted(id)));
    }

    private Trainee enrichTrainee(Trainee trainee, String username, String password) {
        return Trainee.builder()
                .firstName(trainee.getFirstName())
                .lastName(trainee.getLastName())
                .userName(username)
                .password(credentialsManager.generateRandomPassword())
                .isActive(trainee.getIsActive())
                .dateOfBirth(trainee.getDateOfBirth())
                .address(trainee.getAddress())
                .userId(trainee.getUserId())
                .trainings(trainee.getTrainings())
                .build();
    }
}
