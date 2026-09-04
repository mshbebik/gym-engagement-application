package com.epam.gym.engagement.model;


import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Trainer extends User {
    private TrainingType specialization;
    private Long userId;
    private List<Training> trainings;
}
