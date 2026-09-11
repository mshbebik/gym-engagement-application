package com.gym.engagement.data;

import com.gym.engagement.data.dto.StorageInitializationDTO;
import com.gym.engagement.model.Trainee;
import com.gym.engagement.model.Trainer;
import com.gym.engagement.model.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StorageInitializerBeanPostProcessorTest {

    private static final String BEAN_NAME = "storageBean";
    private static final String SOURCE_PATH = "classpath:test-data.json";

    @Mock
    private StorageParser storageParser;

    @Mock
    private EntityStorage entityStorage;

    private StorageInitializerBeanPostProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new StorageInitializerBeanPostProcessor(storageParser);
        ReflectionTestUtils.setField(processor, "sourceFilePath", SOURCE_PATH);
    }

    @Test
    void postProcessAfterInitialization_shouldReturnBeanUnchanged_whenNotEntityStorage() {
        Object someOtherBean = new Object();

        Object actual = processor.postProcessAfterInitialization(someOtherBean, BEAN_NAME);

        assertThat(actual).isSameAs(someOtherBean);
        verifyNoInteractions(storageParser);
    }

    @Test
    void postProcessAfterInitialization_shouldReturnBeanUnchanged_whenParserReturnsNull() {
        when(storageParser.parseSourceData(SOURCE_PATH)).thenReturn(null);

        Object actual = processor.postProcessAfterInitialization(entityStorage, BEAN_NAME);

        assertThat(actual).isSameAs(entityStorage);
        verifyNoInteractions(entityStorage);
    }

    @Test
    void postProcessAfterInitialization_shouldPopulateTrainees_whenPresent() {
        Trainee trainee1 = Trainee.builder().userId(1L).firstName("John").build();
        Trainee trainee2 = Trainee.builder().userId(2L).firstName("Jane").build();
        StorageInitializationDTO dto = new StorageInitializationDTO();
        dto.setTrainees(List.of(trainee1, trainee2));
        Map<Long, Object> traineeMap = new HashMap<>();

        when(storageParser.parseSourceData(SOURCE_PATH)).thenReturn(dto);
        when(entityStorage.getEntities(EntityType.TRAINEES)).thenReturn(traineeMap);

        processor.postProcessAfterInitialization(entityStorage, BEAN_NAME);

        assertThat(traineeMap).containsEntry(1L, trainee1).containsEntry(2L, trainee2);
    }

    @Test
    void postProcessAfterInitialization_shouldPopulateTrainers_whenPresent() {
        Trainer trainer = Trainer.builder().userId(10L).firstName("Mike").build();
        StorageInitializationDTO dto = new StorageInitializationDTO();
        dto.setTrainers(List.of(trainer));
        Map<Long, Object> trainerMap = new HashMap<>();

        when(storageParser.parseSourceData(SOURCE_PATH)).thenReturn(dto);
        when(entityStorage.getEntities(EntityType.TRAINERS)).thenReturn(trainerMap);

        processor.postProcessAfterInitialization(entityStorage, BEAN_NAME);

        assertThat(trainerMap).containsEntry(10L, trainer);
    }

    @Test
    void postProcessAfterInitialization_shouldPopulateTrainings_whenPresent() {
        Training training = Training.builder().id(100L).trainingName("Leg Day").build();
        StorageInitializationDTO dto = new StorageInitializationDTO();
        dto.setTrainings(List.of(training));
        Map<Long, Object> trainingMap = new HashMap<>();

        when(storageParser.parseSourceData(SOURCE_PATH)).thenReturn(dto);
        when(entityStorage.getEntities(EntityType.TRAININGS)).thenReturn(trainingMap);

        processor.postProcessAfterInitialization(entityStorage, BEAN_NAME);

        assertThat(trainingMap).containsEntry(100L, training);
    }

    @Test
    void postProcessAfterInitialization_shouldSkipNullLists_withoutTouchingStorage() {
        StorageInitializationDTO dto = new StorageInitializationDTO();
        when(storageParser.parseSourceData(SOURCE_PATH)).thenReturn(dto);

        processor.postProcessAfterInitialization(entityStorage, BEAN_NAME);

        verifyNoInteractions(entityStorage);
    }

    @Test
    void postProcessAfterInitialization_shouldReturnOriginalBeanReference() {
        StorageInitializationDTO dto = new StorageInitializationDTO();
        when(storageParser.parseSourceData(SOURCE_PATH)).thenReturn(dto);

        Object actual = processor.postProcessAfterInitialization(entityStorage, BEAN_NAME);

        assertThat(actual).isSameAs(entityStorage);
    }
}
