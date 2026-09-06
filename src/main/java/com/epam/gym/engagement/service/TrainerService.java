package com.epam.gym.engagement.service;

import com.epam.gym.engagement.model.Trainee;
import com.epam.gym.engagement.model.Trainer;

public interface TrainerService {

    Trainer createTrainer(Trainer trainer);

    Trainer updateTrainer(Trainer trainer);

    Trainer selectTrainerById(Long id);
}
