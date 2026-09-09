package com.gym.engagement.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class AbstractDaoTest {

    private AbstractDao<String> dao;
    private Map<Long, String> storage;

    @BeforeEach
    void setUp() {
        storage = new HashMap<>();
        dao = new AbstractDao<>();
        dao.setStorage(storage);
    }

    @Test
    void findById_shouldReturnEmpty_whenIdNotPresent() {
        Optional<String> result = dao.findById(1L);

        assertThat(result).isEmpty();
    }

    @Test
    void findById_shouldReturnValue_whenIdPresent() {
        storage.put(2L, "Mr Beast");

        Optional<String> result = dao.findById(2L);

        assertThat(result).contains("Mr Beast");
    }

    @Test
    void getAll_shouldReturnEmptyList_whenStorageIsEmpty() {
        List<String> result = dao.getAll();

        assertThat(result).isEmpty();
    }

    @Test
    void getAll_shouldReturnNonEmptyList_whenStorageHasValues() {
        storage.put(1L, "Mr Beast");
        storage.put(2L, "John Pork");
        storage.put(3L, "X man");

        List<String> actual = dao.getAll();

        assertThat(actual).containsExactlyInAnyOrder("Mr Beast", "John Pork", "X man");
    }

    @Test
    void save_shouldAddNewEntity() {
        dao.save(1L, "X man");

        assertThat(storage).containsEntry(1L, "X man");
    }

    @Test
    void save_shouldOverwriteExistingEntity() {
        storage.put(1L, "oldValue");

        dao.save(1L, "newValue");

        assertThat(storage.get(1L)).isEqualTo("newValue");
    }

    @Test
    void update_shouldReplaceExistingEntity() {
        storage.put(1L, "oldValue");

        dao.update(1L, "updatedValue");

        assertThat(storage.get(1L)).isEqualTo("updatedValue");
    }

    @Test
    void update_shouldAddEntity_whenIdNotPresent() {
        dao.update(1L, "value1");

        assertThat(storage).containsEntry(1L, "value1");
    }

    @Test
    void deleteById_shouldRemoveEntity() {
        storage.put(1L, "value1");

        dao.deleteById(1L);

        assertThat(storage).doesNotContainKey(1L);
    }
}
