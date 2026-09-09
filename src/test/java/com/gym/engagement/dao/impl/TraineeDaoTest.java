package com.gym.engagement.dao.impl;

import com.gym.engagement.model.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class TraineeDaoTest {

    private TraineeDao dao;
    private Map<Long, Trainee> storage;

    @BeforeEach
    void setUp() {
        dao = new TraineeDao();
        storage = new HashMap<>();
        dao.setStorage(storage);
    }

    @Test
    void existsByUsername_shouldReturnTrue_whenUsernameMatches() {
        Trainee trainee = Trainee.builder()
                .userName("Bill.Clover")
                .build();
        storage.put(1L, trainee);

        boolean result = dao.existsByUsername("Bill.Clover");

        assertThat(result).isTrue();
    }

    @Test
    void existsByUsername_shouldReturnFalse_whenUsernameDoesNotMatch() {
        Trainee trainee = Trainee.builder()
                .userName("Bill.Clover")
                .build();
        storage.put(1L, trainee);

        boolean result = dao.existsByUsername("Nick.Brown");

        assertThat(result).isFalse();
    }

    @Test
    void existsByUsername_shouldReturnFalse_whenStorageIsEmpty() {
        assertThat(dao.existsByUsername("Bill.Nick")).isFalse();
    }

    @Test
    void existsByUsername_shouldBeCaseSensitive() {
        Trainee trainee = Trainee.builder()
                .userName("Bill.Clover")
                .build();
        storage.put(1L, trainee);

        boolean result = dao.existsByUsername("bill.clover");

        assertThat(result).isFalse();
    }
}
