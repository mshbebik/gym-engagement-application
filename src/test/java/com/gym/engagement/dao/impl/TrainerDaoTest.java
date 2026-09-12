package com.gym.engagement.dao.impl;

import com.gym.engagement.model.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class TrainerDaoTest {

    private static final Long ID = 1L;
    private static final String USERNAME = "Bill.Clover";

    private TrainerDao dao;
    private Map<Long, Trainer> storage;

    @BeforeEach
    void setUp() {
        dao = new TrainerDao();
        storage = new HashMap<>();
        dao.setStorage(storage);
    }

    @Test
    void existsByUsername_shouldReturnTrue_whenUsernameMatches() {
        Trainer trainer = Trainer.builder()
                .userName(USERNAME)
                .build();
        storage.put(ID, trainer);

        boolean actual = dao.existsByUsername(USERNAME);

        assertThat(actual).isTrue();
    }

    @Test
    void existsByUsername_shouldReturnFalse_whenUsernameDoesNotMatch() {
        Trainer trainer = Trainer.builder()
                .userName(USERNAME)
                .build();
        storage.put(ID, trainer);

        boolean actual = dao.existsByUsername(USERNAME + "1");

        assertThat(actual).isFalse();
    }

    @Test
    void existsByUsername_shouldReturnFalse_whenStorageIsEmpty() {
        assertThat(dao.existsByUsername(USERNAME)).isFalse();
    }

    @Test
    void existsByUsername_shouldBeCaseSensitive() {
        Trainer trainer = Trainer.builder()
                .userName(USERNAME)
                .build();
        storage.put(ID, trainer);

        boolean actual = dao.existsByUsername(USERNAME.toLowerCase());

        assertThat(actual).isFalse();
    }
}
