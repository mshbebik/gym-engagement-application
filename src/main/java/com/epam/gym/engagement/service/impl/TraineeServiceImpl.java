package com.epam.gym.engagement.service.impl;

import com.epam.gym.engagement.dao.impl.TraineeDao;
import com.epam.gym.engagement.dao.impl.TrainerDao;
import com.epam.gym.engagement.exception.TraineeNotFoundException;
import com.epam.gym.engagement.model.Trainee;
import com.epam.gym.engagement.service.TraineeService;
import com.epam.gym.engagement.service.UserCredentialsManager;
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
        String username = credentialsManager.generateUsername(
                trainee.getFirstName(),
                trainee.getLastName(),
                u -> traineeDao.existsByUsername(u) || trainerDao.existsByUsername(u)
        );

        trainee.setUserName(username);
        trainee.setPassword(credentialsManager.generateRandomPassword());

        traineeDao.save(trainee.getUserId(), trainee);
        return trainee;
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
                .orElseThrow(() -> new TraineeNotFoundException(id));
    }
}
