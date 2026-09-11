package com.gym.engagement.service.impl;

import com.gym.engagement.dao.impl.TraineeDao;
import com.gym.engagement.dao.impl.TrainerDao;
import com.gym.engagement.exception.EntityNotFoundException;
import com.gym.engagement.model.Trainee;
import com.gym.engagement.service.common.UserCredentialsManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
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
class TraineeServiceImplTest {

    private static final Long USERID = 2L;
    private static final String FIRSTNAME = "Jason";
    private static final String LASTNAME = "Paper";
    private static final String USERNAME = "Jason.Paper";
    private static final String PASSWORD = "beautifulDay9812";

    private final Trainee trainee = constructTrainee();

    @Mock
    private UserCredentialsManager credentialsManager;

    @Mock
    private TrainerDao trainerDao;

    @Mock
    private TraineeDao traineeDao;

    @InjectMocks
    private TraineeServiceImpl service;

    @Test
    void createTrainee_shouldGenerateUsernameAndPassword_andSave() {
        when(credentialsManager.generateUsername(eq(FIRSTNAME), eq(LASTNAME), any()))
                .thenReturn(USERNAME);
        when(credentialsManager.generateRandomPassword()).thenReturn(PASSWORD);

        Trainee actual = service.createTrainee(trainee);

        assertThat(actual.getUserName()).isEqualTo(USERNAME);
        assertThat(actual.getPassword()).isEqualTo(PASSWORD);
        assertThat(actual.getFirstName()).isEqualTo(FIRSTNAME);
        assertThat(actual.getUserId()).isEqualTo(USERID);
        verify(traineeDao).save(eq(USERID), argThat(t ->
                t.getUserName().equals(USERNAME) && t.getPassword().equals(PASSWORD)));
    }

    @Test
    void createTrainee_shouldThrowNPE_whenTraineeIsNull() {
        assertThatThrownBy(() -> service.createTrainee(null))
                .isInstanceOf(NullPointerException.class);

        verifyNoInteractions(credentialsManager, traineeDao);
    }

    @Test
    void createTrainee_usernameUniquenessCheck_shouldConsultBothDaos() {
        ArgumentCaptor<Predicate<String>> predicateCaptor = ArgumentCaptor.forClass(Predicate.class);

        when(credentialsManager.generateUsername(eq(FIRSTNAME), eq(LASTNAME), predicateCaptor.capture()))
                .thenReturn(USERNAME);
        when(credentialsManager.generateRandomPassword()).thenReturn(PASSWORD);
        when(traineeDao.existsByUsername(USERNAME)).thenReturn(false);
        when(trainerDao.existsByUsername(USERNAME)).thenReturn(true);

        service.createTrainee(trainee);

        Predicate<String> capturedPredicate = predicateCaptor.getValue();
        assertThat(capturedPredicate.test(USERNAME)).isTrue();
        verify(traineeDao).existsByUsername(USERNAME);
        verify(trainerDao).existsByUsername(USERNAME);
    }

    @Test
    void updateTrainee_shouldSaveAndReturnSameTrainee() {
        Trainee actual = service.updateTrainee(trainee);

        assertThat(actual).isEqualTo(trainee);
        verify(traineeDao).save(USERID, trainee);
    }

    @Test
    void updateTrainee_shouldThrowNPE_whenNull() {
        assertThatThrownBy(() -> service.updateTrainee(null))
                .isInstanceOf(NullPointerException.class);
        verifyNoInteractions(traineeDao);
    }

    @Test
    void deleteTrainee_shouldCallDaoDeleteById() {
        service.deleteTrainee(USERID);
        verify(traineeDao).deleteById(USERID);
    }

    @Test
    void deleteTrainee_shouldThrowNPE_whenIdIsNull() {
        assertThatThrownBy(() -> service.deleteTrainee(null))
                .isInstanceOf(NullPointerException.class);
        verifyNoInteractions(traineeDao);
    }

    @Test
    void selectTraineeById_shouldReturnTrainee_whenFound() {
        when(traineeDao.findById(USERID)).thenReturn(Optional.of(trainee));

        Trainee actual = service.selectTraineeById(USERID);

        assertThat(actual).isEqualTo(trainee);
    }

    @Test
    void selectTraineeById_shouldThrowEntityNotFoundException_whenMissing() {
        when(traineeDao.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.selectTraineeById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Trainee with id: %s was not found", "99");
    }

    private Trainee constructTrainee() {
        return Trainee.builder()
                .userId(USERID)
                .firstName(FIRSTNAME)
                .lastName(LASTNAME)
                .isActive(true)
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("28 Waterland St")
                .trainings(Collections.emptyList())
                .build();
    }
}