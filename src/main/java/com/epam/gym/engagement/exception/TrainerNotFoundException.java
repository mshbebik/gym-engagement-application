package com.epam.gym.engagement.exception;

public class TrainerNotFoundException extends NotFoundException {
    public TrainerNotFoundException(Long id) {
        super("Trainer with id: " + id + " was not found");
    }
}
