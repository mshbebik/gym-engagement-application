package com.gym.engagement.service.impl;

import com.gym.engagement.dao.impl.TraineeDao;
import com.gym.engagement.dao.impl.TrainerDao;
import com.gym.engagement.exception.EntityNotFoundException;
import com.gym.engagement.model.Trainer;
import com.gym.engagement.model.TrainingType;
import com.gym.engagement.service.common.UserCredentialsManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;
import java.util.function.Predicate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainerServiceImplTest {

    private static final Long USERID = 2L;
    private static final String FIRSTNAME = "Jason";
    private static final String LASTNAME = "Paper";
    private static final String USERNAME = "Jason.Paper";
    private static final String PASSWORD = "beautifulDay9812";

    private final Trainer trainer = constructTrainer();

    @Mock
    private UserCredentialsManager credentialsManager;

    @Mock
    private TrainerDao trainerDao;

    @Mock
    private TraineeDao traineeDao;

    @InjectMocks
    private TrainerServiceImpl service;

    @Test
    void createTrainer_shouldGenerateUsernameAndPassword_andSave() {
        when(credentialsManager.generateUsername(eq(FIRSTNAME), eq(LASTNAME), any()))
                .thenReturn(USERNAME);
        when(credentialsManager.generateRandomPassword()).thenReturn(PASSWORD);

        Trainer actual = service.createTrainer(trainer);

        assertThat(actual.getUserName()).isEqualTo(USERNAME);
        assertThat(actual.getPassword()).isEqualTo(PASSWORD);
        assertThat(actual.getFirstName()).isEqualTo(FIRSTNAME);
        assertThat(actual.getUserId()).isEqualTo(USERID);
        verify(trainerDao).save(eq(USERID), argThat(t ->
                t.getUserName().equals(USERNAME) && t.getPassword().equals(PASSWORD)));
    }

    @Test
    void createTrainer_shouldThrowNPE_whenTraineeIsNull() {
        assertThatThrownBy(() -> service.createTrainer(null))
                .isInstanceOf(NullPointerException.class);

        verifyNoInteractions(credentialsManager, trainerDao);
    }

    @Test
    void createTrainer_usernameUniquenessCheck_shouldConsultBothDaos() {
        ArgumentCaptor<Predicate<String>> predicateCaptor = ArgumentCaptor.forClass(Predicate.class);

        when(credentialsManager.generateUsername(eq(FIRSTNAME), eq(LASTNAME), predicateCaptor.capture()))
                .thenReturn(USERNAME);
        when(credentialsManager.generateRandomPassword()).thenReturn(PASSWORD);
        when(traineeDao.existsByUsername(USERNAME)).thenReturn(false);
        when(trainerDao.existsByUsername(USERNAME)).thenReturn(true);

        service.createTrainer(trainer);

        Predicate<String> capturedPredicate = predicateCaptor.getValue();
        assertThat(capturedPredicate.test(USERNAME)).isTrue();
        verify(traineeDao).existsByUsername(USERNAME);
        verify(trainerDao).existsByUsername(USERNAME);
    }

    @Test
    void updateTrainer_shouldSaveAndReturnSameTrainer() {
        Trainer actual = service.updateTrainer(trainer);

        assertThat(actual).isEqualTo(trainer);
        verify(trainerDao).save(USERID, trainer);
    }

    @Test
    void updateTrainer_shouldThrowNPE_whenNull() {
        assertThatThrownBy(() -> service.updateTrainer(null))
                .isInstanceOf(NullPointerException.class);
        verifyNoInteractions(trainerDao);
    }

    @Test
    void selectTrainerById_shouldReturnTrainer_whenFound() {
        when(trainerDao.findById(USERID)).thenReturn(Optional.of(trainer));

        Trainer actual = service.selectTrainerById(USERID);

        assertThat(actual).isEqualTo(trainer);
    }

    @Test
    void selectTrainerById_shouldThrowEntityNotFoundException_whenMissing() {
        when(trainerDao.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.selectTrainerById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Trainer with id: %s was not found", "99");
    }

    private Trainer constructTrainer() {
        return Trainer.builder()
                .userId(USERID)
                .firstName(FIRSTNAME)
                .lastName(LASTNAME)
                .isActive(true)
                .specialization(new TrainingType("Swimming"))
                .trainings(Collections.emptyList())
                .build();
    }
}
