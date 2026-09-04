package com.epam.gym.engagement.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class User {
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private Boolean isActive;
}
