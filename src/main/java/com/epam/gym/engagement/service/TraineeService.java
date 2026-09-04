package com.epam.gym.engagement.service;

import com.epam.gym.engagement.model.Trainee;

public interface TraineeService {

    public Trainee createTrainee(Trainee trainee);

    public Trainee updateTrainee(Trainee trainee);

    public void deleteTrainee(Long id);

    public Trainee selectTraineeById(Long id);
}
