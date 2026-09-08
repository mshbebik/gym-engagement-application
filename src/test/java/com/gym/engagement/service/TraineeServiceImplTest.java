package com.gym.engagement.service;

import com.gym.engagement.dao.impl.TraineeDao;
import com.gym.engagement.dao.impl.TrainerDao;
import com.gym.engagement.exception.EntityNotFoundException;
import com.gym.engagement.model.Trainee;
import com.gym.engagement.service.common.UserCredentialsManager;
import com.gym.engagement.service.impl.TraineeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
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

    @Mock
    private UserCredentialsManager credentialsManager;

    @Mock
    private TrainerDao trainerDao;

    @Mock
    private TraineeDao traineeDao;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    private Trainee sampleTrainee;

    @BeforeEach
    void setUp() {
        sampleTrainee = Trainee.builder()
                .userId(1L)
                .firstName("John")
                .lastName("Smith")
                .isActive(true)
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("123 Main St")
                .trainings(Collections.emptyList())
                .build();
    }

    @Test
    void createTrainee_shouldGenerateUsernameAndPassword_andSave() {
        when(credentialsManager.generateUsername(eq("John"), eq("Smith"), any()))
                .thenReturn("John.Smith");
        when(credentialsManager.generateRandomPassword())
                .thenReturn("randomPass123");

        Trainee result = traineeService.createTrainee(sampleTrainee);

        assertThat(result.getUserName()).isEqualTo("John.Smith");
        assertThat(result.getPassword()).isEqualTo("randomPass123");
        assertThat(result.getFirstName()).isEqualTo("John");
        assertThat(result.getUserId()).isEqualTo(1L);

        verify(traineeDao).save(eq(1L), argThat(t ->
                t.getUserName().equals("John.Smith") && t.getPassword().equals("randomPass123")));
    }

    @Test
    void createTrainee_shouldThrowNPE_whenTraineeIsNull() {
        assertThatThrownBy(() -> traineeService.createTrainee(null))
                .isInstanceOf(NullPointerException.class);

        verifyNoInteractions(credentialsManager, traineeDao);
    }

    @Test
    void createTrainee_usernameUniquenessCheck_shouldConsultBothDaos() {
        ArgumentCaptor<Predicate<String>> predicateCaptor = ArgumentCaptor.forClass(Predicate.class);

        when(credentialsManager.generateUsername(eq("John"), eq("Smith"), predicateCaptor.capture()))
                .thenReturn("John.Smith1");
        when(credentialsManager.generateRandomPassword()).thenReturn("pw");
        when(traineeDao.existsByUsername("John.Smith")).thenReturn(false);
        when(trainerDao.existsByUsername("John.Smith")).thenReturn(true);

        traineeService.createTrainee(sampleTrainee);

        Predicate<String> capturedPredicate = predicateCaptor.getValue();
        assertThat(capturedPredicate.test("John.Smith")).isTrue();

        verify(traineeDao).existsByUsername("John.Smith");
        verify(trainerDao).existsByUsername("John.Smith");
    }

    @Test
    void updateTrainee_shouldSaveAndReturnSameTrainee() {
        Trainee result = traineeService.updateTrainee(sampleTrainee);

        assertThat(result).isEqualTo(sampleTrainee);
        verify(traineeDao).save(1L, sampleTrainee);
    }

    @Test
    void updateTrainee_shouldThrowNPE_whenNull() {
        assertThatThrownBy(() -> traineeService.updateTrainee(null))
                .isInstanceOf(NullPointerException.class);
        verifyNoInteractions(traineeDao);
    }

    @Test
    void deleteTrainee_shouldCallDaoDeleteById() {
        traineeService.deleteTrainee(1L);
        verify(traineeDao).deleteById(1L);
    }

    @Test
    void deleteTrainee_shouldThrowNPE_whenIdIsNull() {
        assertThatThrownBy(() -> traineeService.deleteTrainee(null))
                .isInstanceOf(NullPointerException.class);
        verifyNoInteractions(traineeDao);
    }

    @Test
    void selectTraineeById_shouldReturnTrainee_whenFound() {
        when(traineeDao.findById(1L)).thenReturn(Optional.of(sampleTrainee));

        Trainee result = traineeService.selectTraineeById(1L);

        assertThat(result).isEqualTo(sampleTrainee);
    }

    @Test
    void selectTraineeById_shouldThrowEntityNotFoundException_whenMissing() {
        when(traineeDao.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> traineeService.selectTraineeById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }
}