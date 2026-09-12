package com.gym.engagement.service.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class UserCredentialsManagerTest {

    private static final String FIRSTNAME = "Bob";
    private static final String LASTNAME = "Nicks";
    private static final String USERNAME = FIRSTNAME.toLowerCase() + "." + LASTNAME.toLowerCase();

    private UserCredentialsManager credentialsManager;

    @BeforeEach
    void setUp() {
        credentialsManager = new UserCredentialsManager();
    }

    @Test
    void generateUsername_shouldReturnBaseUsername_whenNoConflict() {
        String actual = credentialsManager.generateUsername(FIRSTNAME, LASTNAME, username -> false);

        assertThat(actual).isEqualTo(USERNAME);
    }

    @Test
    void generateUsername_shouldLowercaseFirstAndLastName() {
        String actual = credentialsManager.generateUsername(FIRSTNAME.toUpperCase(),
                LASTNAME.toUpperCase(), username -> false);

        assertThat(actual).isEqualTo(USERNAME);
    }

    @Test
    void generateUsername_shouldAppendSuffix_whenBaseUsernameExists() {
        Predicate<String> usernameExists = username -> username.equals(USERNAME);

        String actual = credentialsManager.generateUsername(FIRSTNAME, LASTNAME, usernameExists);

        assertThat(actual).isEqualTo(USERNAME + "1");
    }

    @Test
    void generateUsername_shouldIncrementSuffix_untilFreeUsernameFound() {
        Set<String> takenUsernames = Set.of(USERNAME, USERNAME + "1", USERNAME + "2");
        Predicate<String> usernameExists = takenUsernames::contains;

        String actual = credentialsManager.generateUsername(FIRSTNAME, LASTNAME, usernameExists);

        assertThat(actual).isEqualTo(USERNAME + "3");
    }

    @Test
    void generateUsername_shouldCallPredicateWithExpectedCandidates_inOrder() {
        List<String> testedCandidates = new ArrayList<>();
        Predicate<String> recordingPredicate = username -> {
            testedCandidates.add(username);
            return testedCandidates.size() <= 2;
        };

        String actual = credentialsManager.generateUsername(FIRSTNAME, LASTNAME, recordingPredicate);

        assertThat(testedCandidates).containsExactly(USERNAME, USERNAME + "1", USERNAME + "2");
        assertThat(actual).isEqualTo(USERNAME + "2");
    }

    @Test
    void generateUsername_shouldNotCallPredicateAgain_onceFreeUsernameFound() {
        int[] callCount = {0};
        Predicate<String> countingPredicate = username -> {
            callCount[0]++;
            return false;
        };

        credentialsManager.generateUsername(FIRSTNAME, LASTNAME, countingPredicate);

        assertThat(callCount[0]).isEqualTo(1);
    }

    @Test
    void generateRandomPassword_shouldReturnStringOfExactLength() {
        String actual = credentialsManager.generateRandomPassword();

        assertThat(actual).hasSize(10);
    }

    @Test
    void generateRandomPassword_shouldOnlyContainAllowedCharacters() {
        String actual = credentialsManager.generateRandomPassword();
        assertThat(actual).matches("[A-Za-z0-9]+");
    }

    @Test
    void generateRandomPassword_shouldGenerateDifferentValues_acrossMultipleCalls() {
        Set<String> generatedPasswords = Stream.generate(credentialsManager::generateRandomPassword)
                .limit(100)
                .collect(Collectors.toSet());

        assertThat(generatedPasswords).hasSize(100);
    }
}
