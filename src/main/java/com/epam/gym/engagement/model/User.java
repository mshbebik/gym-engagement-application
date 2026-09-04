package com.epam.gym.engagement.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public abstract class User {
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private Boolean isActive;
}
