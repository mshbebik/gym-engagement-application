package com.epam.gym.engagement.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class TrainingType {
    private String trainingTypeName;
}
