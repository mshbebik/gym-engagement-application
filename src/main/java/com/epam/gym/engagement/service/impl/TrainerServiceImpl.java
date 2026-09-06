package com.epam.gym.engagement.service.impl;

import com.epam.gym.engagement.dao.impl.TraineeDao;
import com.epam.gym.engagement.dao.impl.TrainerDao;
import com.epam.gym.engagement.exception.TraineeNotFoundException;
import com.epam.gym.engagement.model.Trainer;
import com.epam.gym.engagement.service.TrainerService;
import com.epam.gym.engagement.service.UserCredentialsManager;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainerServiceImpl implements TrainerService {

    @Setter(onMethod_ = {@Autowired})
    private UserCredentialsManager credentialsManager;

    @Setter(onMethod_ = {@Autowired})
    private TrainerDao trainerDao;

    @Setter(onMethod_ = {@Autowired})
    private TraineeDao traineeDao;

    @Override
    public Trainer createTrainer(Trainer trainer) {
        String username = credentialsManager.generateUsername(
                trainer.getFirstName(),
                trainer.getLastName(),
                u -> traineeDao.existsByUsername(u) || trainerDao.existsByUsername(u)
        );

        trainer.setUserName(username);
        trainer.setPassword(credentialsManager.generateRandomPassword());

        trainerDao.save(trainer.getUserId(), trainer);
        return trainer;
    }

    @Override
    public Trainer updateTrainer(Trainer trainer) {
        trainerDao.save(trainer.getUserId(), trainer);
        return trainer;
    }

    @Override
    public Trainer selectTrainerById(Long id) {
        return trainerDao.findById(id)
                .orElseThrow(() -> new TraineeNotFoundException(id));
    }
}
