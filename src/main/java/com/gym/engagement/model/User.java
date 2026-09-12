package com.gym.engagement.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public abstract class User {
    private String firstName;
    private String lastName;

    @JsonProperty("username")
    private String userName;
    private String password;
    private Boolean isActive;
}
