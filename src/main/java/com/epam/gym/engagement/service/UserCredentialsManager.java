package com.epam.gym.engagement.service;

import com.epam.gym.engagement.dao.impl.TraineeDao;
import com.epam.gym.engagement.dao.impl.TrainerDao;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.function.Predicate;

@Component
public class UserCredentialsManager {
    private static final Integer PASSWORD_LENGTH = 10;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public String generateUsername(String firstName, String lastName, Predicate<String> usernameExists) {
        String baseUsername = firstName.toLowerCase() + "." + lastName.toLowerCase();
        String candidateUsername = baseUsername;
        int suffix = 1;

        while (usernameExists.test(candidateUsername)) {
            candidateUsername = baseUsername + suffix;
            suffix++;
        }

        return candidateUsername;
    }

    public String generateRandomPassword() {
        StringBuilder sb = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int randomIndex = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }
}
