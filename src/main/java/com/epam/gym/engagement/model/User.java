package com.epam.gym.engagement.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public abstract class User {
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private Boolean isActive;
}
