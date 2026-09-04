package com.epam.gym.engagement.service;

import com.epam.gym.engagement.model.Trainee;
import com.epam.gym.engagement.model.Trainer;

public interface TrainerService {

    public Trainer createTrainer(Trainer trainer);

    public Trainer updateTrainer(Trainer trainer);

    public Trainer selectTrainerById(Long id);
}
