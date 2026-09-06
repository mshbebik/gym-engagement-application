package com.gym.engagement.service;

import com.gym.engagement.model.Trainee;

public interface TraineeService {

    Trainee createTrainee(Trainee trainee);

    Trainee updateTrainee(Trainee trainee);

    void deleteTrainee(Long id);

    Trainee selectTraineeById(Long id);
}
