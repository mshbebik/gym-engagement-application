package com.gym.engagement.data;

import com.gym.engagement.data.impl.InMemoryStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

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
        assertThat(storage.getEntities(EntityType.TRAINEES)).isNotNull().isEmpty();
        assertThat(storage.getEntities(EntityType.TRAINERS)).isNotNull().isEmpty();
        assertThat(storage.getEntities(EntityType.TRAININGS)).isNotNull().isEmpty();
    }

    @Test
    void getEntities_shouldReturnSameMapInstance_onRepeatedCalls() {
        Map<Long, Object> first = storage.getEntities(EntityType.TRAINEES);
        Map<Long, Object> second = storage.getEntities(EntityType.TRAINEES);

        assertThat(first).isSameAs(second);
    }

    @Test
    void getEntities_shouldKeepNamespacesIsolated() {
        storage.getEntities(EntityType.TRAINEES).put(ID, VALUE);

        assertThat(storage.getEntities(EntityType.TRAINEES)).containsEntry(ID, VALUE);
        assertThat(storage.getEntities(EntityType.TRAINERS)).isEmpty();
        assertThat(storage.getEntities(EntityType.TRAININGS)).isEmpty();
    }

    @Test
    void getEntities_mutationsPersistAcrossCalls() {
        storage.getEntities(EntityType.TRAININGS).put(ID, VALUE);

        assertThat(storage.getEntities(EntityType.TRAININGS)).containsEntry(ID, VALUE);
    }
}
