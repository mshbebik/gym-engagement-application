package com.gym.engagement.data.dto;

import com.gym.engagement.model.Trainee;
import com.gym.engagement.model.Trainer;
import com.gym.engagement.model.Training;
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
