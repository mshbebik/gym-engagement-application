package com.gym.engagement.dto;

import com.gym.engagement.model.TrainingType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TrainerDTO {
    private Long userId;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private Boolean isActive;
    private TrainingType specialization;
    private List<TrainingDTO> trainings;
}
