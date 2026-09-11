package com.gym.engagement.service.impl;

import com.gym.engagement.dao.impl.TrainingDao;
import com.gym.engagement.exception.EntityNotFoundException;
import com.gym.engagement.model.Trainee;
import com.gym.engagement.model.Trainer;
import com.gym.engagement.model.Training;
import com.gym.engagement.model.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceImplTest {

    private static final Long TRAINING_ID = 1L;
    private static final Long TRAINEE_ID = 2L;
    private static final Long TRAINER_ID = 3L;

    private final Training training = constructTraining();
    private final Trainee trainee = constructTrainee();
    private final Trainer trainer = constructTrainer();
    private final List<Training> trainings = constructTrainings();

    @Mock
    private TrainingDao trainingDao;

    @InjectMocks
    private TrainingServiceImpl service;

    @Test
    void createTraining_shouldSaveTraining() {
        Training actual = service.createTraining(training);

        assertThat(actual).isEqualTo(training);
        verify(trainingDao).save(training.getId(), training);
    }

    @Test
    void createTraining_shouldThrowNPE_whenTrainingIsNull() {
        assertThatThrownBy(() -> service.createTraining(null))
                .isInstanceOf(NullPointerException.class);

        verifyNoInteractions(trainingDao);
    }

    @Test
    void selectTrainingsByTrainer_shouldReturnEmptyList_whenNoTrainingsFoundForTrainer() {
        when(trainingDao.getAll()).thenReturn(trainings);

        List<Training> actual = service.selectTrainingsByTrainer(trainer);

        assertThat(actual).isEmpty();
    }

    @Test
    void selectTrainingsByTrainer_shouldReturnTrainings_whenTrainingsFoundForTrainer() {
        Training training2 = Training.builder().trainerId(TRAINER_ID).trainingName("Cardio").build();
        Training training3 = Training.builder().trainerId(TRAINER_ID).trainingName("Yoga").build();
        trainings.addAll(List.of(training, training2, training3));

        when(trainingDao.getAll()).thenReturn(trainings);

        List<Training> actual = service.selectTrainingsByTrainer(trainer);

        assertThat(actual).containsExactlyInAnyOrder(training, training2, training3);
    }

    @Test
    void selectTrainingsByTrainer_shouldThrowNPE_whenTrainerIsNull() {
        assertThatThrownBy(() -> service.selectTrainingsByTrainer(null))
                .isInstanceOf(NullPointerException.class);

        verifyNoInteractions(trainingDao);
    }

    @Test
    void selectTrainingsByTrainee_shouldReturnEmptyList_whenNoTrainingsFoundForTrainee() {
        when(trainingDao.getAll()).thenReturn(trainings);

        List<Training> actual = service.selectTrainingsByTrainee(trainee);

        assertThat(actual).isEmpty();
    }

    @Test
    void selectTrainingsByTrainee_shouldReturnTrainings_whenTrainingsFoundForTrainee() {
        Training training2 = Training.builder().traineeId(TRAINEE_ID).trainingName("Cardio").build();
        Training training3 = Training.builder().traineeId(TRAINEE_ID).trainingName("Yoga").build();
        trainings.addAll(List.of(training, training2, training3));

        when(trainingDao.getAll()).thenReturn(trainings);

        List<Training> actual = service.selectTrainingsByTrainee(trainee);

        assertThat(actual).containsExactlyInAnyOrder(training, training2, training3);
    }

    @Test
    void selectTrainingsByTrainee_shouldThrowNPE_whenTraineeIsNull() {
        assertThatThrownBy(() -> service.selectTrainingsByTrainee(null))
                .isInstanceOf(NullPointerException.class);

        verifyNoInteractions(trainingDao);
    }

    @Test
    void selectTrainingById_shouldReturnTraining_whenFound() {
        when(trainingDao.findById(TRAINING_ID)).thenReturn(Optional.of(training));

        Training actual = service.selectTrainingById(TRAINING_ID);

        assertThat(actual).isEqualTo(training);
    }

    @Test
    void selectTrainingById_shouldThrowEntityNotFoundException_whenMissing() {
        when(trainingDao.findById(TRAINING_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.selectTrainingById(TRAINING_ID))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Training with id: %s was not found", TRAINING_ID.toString());
    }

    @Test
    void selectTrainingById_shouldThrowNPE_whenIdIsNull() {
        assertThatThrownBy(() -> service.selectTrainingById(null))
                .isInstanceOf(NullPointerException.class);
        verifyNoInteractions(trainingDao);
    }

    private Training constructTraining() {
        return Training.builder()
                .id(TRAINING_ID)
                .traineeId(TRAINEE_ID)
                .trainerId(TRAINER_ID)
                .trainingName("Leg Day")
                .trainingType(new TrainingType("Strength training"))
                .trainingDate(LocalDate.now())
                .trainingDuration(45)
                .build();
    }

    private Trainee constructTrainee() {
        return Trainee.builder()
                .userId(TRAINEE_ID)
                .firstName("Jonatan")
                .lastName("Bezos")
                .isActive(true)
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("28 Waterland St")
                .trainings(Collections.emptyList())
                .build();
    }

    private Trainer constructTrainer() {
        return Trainer.builder()
                .userId(TRAINER_ID)
                .firstName("Mike")
                .lastName("Brown")
                .isActive(true)
                .specialization(new TrainingType("Swimming"))
                .trainings(Collections.emptyList())
                .build();
    }

    private List<Training> constructTrainings() {
        return new ArrayList<>(List.of(
                new Training(1L, 10L, 10L, "Sprint", null, null, null),
                new Training(2L, 33L, 44L, "Push ups", null, null, null),
                new Training(2L, 13L, 20L, "Pull ups", null, null, null)));
    }
}
