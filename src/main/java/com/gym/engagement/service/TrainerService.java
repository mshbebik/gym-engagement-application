package com.gym.engagement.service;

import com.gym.engagement.model.Trainer;

public interface TrainerService {

    Trainer createTrainer(Trainer trainer);

    Trainer updateTrainer(Trainer trainer);

    Trainer selectTrainerById(Long id);
}
