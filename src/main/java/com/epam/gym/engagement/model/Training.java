package com.epam.gym.engagement.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
public class Training {
    private Long traineeId;
    private Long trainerId;
    private String trainingName;
    private TrainingType trainingType;
    private LocalDate trainingDate;
    private Integer trainingDuration;
}
