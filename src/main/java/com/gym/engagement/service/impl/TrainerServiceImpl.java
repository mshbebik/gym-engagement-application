package com.gym.engagement.service.impl;

import com.gym.engagement.dao.impl.TraineeDao;
import com.gym.engagement.dao.impl.TrainerDao;
import com.gym.engagement.exception.EntityNotFoundException;
import com.gym.engagement.model.Trainer;
import com.gym.engagement.service.TrainerService;
import com.gym.engagement.service.common.UserCredentialsManager;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

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
        Objects.requireNonNull(trainer);
        String username = credentialsManager.generateUsername(trainer.getFirstName(),
                trainer.getLastName(),
                u -> traineeDao.existsByUsername(u) || trainerDao.existsByUsername(u));

        Trainer newTrainer = enrichTrainer(trainer, username, credentialsManager.generateRandomPassword());

        trainerDao.save(newTrainer.getUserId(), newTrainer);
        return newTrainer;
    }

    @Override
    public Trainer updateTrainer(Trainer trainer) {
        Objects.requireNonNull(trainer);
        trainerDao.save(trainer.getUserId(), trainer);
        return trainer;
    }

    @Override
    public Trainer selectTrainerById(Long id) {
        Objects.requireNonNull(id);
        return trainerDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Trainer with id: %d was not found".formatted(id)));
    }

    private Trainer enrichTrainer(Trainer trainer, String username, String password) {
        return Trainer.builder()
                .firstName(trainer.getFirstName())
                .lastName(trainer.getLastName())
                .userName(username)
                .password(password)
                .isActive(trainer.getIsActive())
                .specialization(trainer.getSpecialization())
                .userId(trainer.getUserId())
                .trainings(trainer.getTrainings())
                .build();
    }
}
