package com.epam.gym.engagement.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Trainer extends User {
    private TrainingType specialization;
    private Long userId;
    private List<Training> trainings;
}
