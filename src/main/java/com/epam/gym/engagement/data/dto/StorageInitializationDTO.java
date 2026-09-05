package com.epam.gym.engagement.data.dto;

import com.epam.gym.engagement.model.Trainee;
import com.epam.gym.engagement.model.Trainer;
import com.epam.gym.engagement.model.Training;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class StorageInitializationDTO {
    private List<Trainee> trainees;
    private List<Trainer> trainers;
    private List<Training> trainings;
}
