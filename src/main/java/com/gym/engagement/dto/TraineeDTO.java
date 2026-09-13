package com.gym.engagement.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class TraineeDTO {
    private Long userId;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private Boolean isActive;
    private LocalDate dateOfBirth;
    private String address;
    private List<TrainingDTO> trainings;
}
