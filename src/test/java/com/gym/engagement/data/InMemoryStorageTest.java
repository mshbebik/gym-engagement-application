package com.gym.engagement.data;

import com.gym.engagement.data.impl.InMemoryStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.gym.engagement.data.EntityType.TRAINEES;
import static com.gym.engagement.data.EntityType.TRAINERS;
import static com.gym.engagement.data.EntityType.TRAININGS;
import static org.assertj.core.api.Assertions.assertThat;

class InMemoryStorageTest {

    private static final Long ID = 1L;
    private static final String VALUE = "trainee-data";

    private InMemoryStorage storage;

    @BeforeEach
    void setUp() {
        storage = new InMemoryStorage();
    }

    @Test
    void constructor_shouldInitializeAllThreeNamespaces() {
        Map<Long, Object> actualTrainees = storage.getEntities(TRAINEES);
        Map<Long, Object> actualTrainers = storage.getEntities(TRAINERS);
        Map<Long, Object> actualTrainings = storage.getEntities(TRAININGS);

        assertThat(actualTrainees).isNotNull().isEmpty();
        assertThat(actualTrainers).isNotNull().isEmpty();
        assertThat(actualTrainings).isNotNull().isEmpty();
    }

    @Test
    void getEntities_shouldReturnSameMapInstance_onRepeatedCalls() {
        Map<Long, Object> first = storage.getEntities(TRAINEES);
        Map<Long, Object> second = storage.getEntities(TRAINEES);

        assertThat(first).isSameAs(second);
    }

    @Test
    void getEntities_shouldKeepNamespacesIsolated() {
        storage.getEntities(TRAINEES).put(ID, VALUE);

        assertThat(storage.getEntities(TRAINEES)).containsEntry(ID, VALUE);
        assertThat(storage.getEntities(TRAINERS)).isEmpty();
        assertThat(storage.getEntities(TRAININGS)).isEmpty();
    }

    @Test
    void getEntities_mutationsPersistAcrossCalls() {
        storage.getEntities(TRAININGS).put(ID, VALUE);

        assertThat(storage.getEntities(TRAININGS)).containsEntry(ID, VALUE);
    }
}
