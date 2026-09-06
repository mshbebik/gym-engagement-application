package com.gym.engagement.model;


import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Trainer extends User {
    private TrainingType specialization;
    private Long userId;
    private List<Training> trainings;
}
