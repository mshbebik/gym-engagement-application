package com.gym.engagement.model;

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
