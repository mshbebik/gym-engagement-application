package com.epam.gym.engagement.exception;

public class TraineeNotFoundException extends NotFoundException {
    public TraineeNotFoundException(Long id) {
        super("Trainee with id: " + id + " was not found");
    }
}
