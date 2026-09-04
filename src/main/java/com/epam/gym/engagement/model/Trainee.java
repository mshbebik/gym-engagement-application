package com.epam.gym.engagement.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Getter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Trainee extends User {
    private LocalDate dateOfBirth;
    private String address;
    private Long userId;
    private List<Training> trainings;
}
