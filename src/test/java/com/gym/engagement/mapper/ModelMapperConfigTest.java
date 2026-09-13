package com.gym.engagement.mapper;

import com.gym.engagement.dto.TraineeDTO;
import com.gym.engagement.dto.TrainerDTO;
import com.gym.engagement.dto.TrainingDTO;
import com.gym.engagement.factory.ModelMapperFactory;
import com.gym.engagement.model.Trainee;
import com.gym.engagement.model.Trainer;
import com.gym.engagement.model.Training;
import com.gym.engagement.model.TrainingType;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ModelMapperConfigTest {

    private static final Long USER_ID = 1L;
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Pork";
    private static final String USERNAME = "john.pork";
    private static final String PASSWORD = "realPassword123";
    private static final Boolean IS_ACTIVE = true;
    private static final LocalDate DATE_OF_BIRTH = LocalDate.of(1998, 5, 15);
    private static final String ADDRESS = "123 Main St";
    private static final Long TRAINING_ID = 10L;
    private static final String TRAINING_NAME = "Leg Day";
    private static final LocalDate TRAINING_DATE = LocalDate.of(2026, 9, 10);
    private static final Integer TRAINING_DURATION = 45;
    private static final String TRAINING_TYPE_NAME = "Strength";

    private static final Set<String> TRAINEE_COVERED_FIELDS = Set.of(
            "userId", "firstName", "lastName", "userName", "password",
            "isActive", "dateOfBirth", "address", "trainings");
    private static final Set<String> TRAINER_COVERED_FIELDS = Set.of(
            "userId", "firstName", "lastName", "userName", "password",
            "isActive", "specialization", "trainings");
    private static final Set<String> TRAINING_COVERED_FIELDS = Set.of(
            "id", "traineeId", "trainerId", "trainingName",
            "trainingType", "trainingDate", "trainingDuration");
    private static final Set<String> TRAINEE_DTO_COVERED_FIELDS = Set.of(
            "userId", "firstName", "lastName", "userName", "password",
            "isActive", "dateOfBirth", "address", "trainings");
    private static final Set<String> TRAINER_DTO_COVERED_FIELDS = Set.of(
            "userId", "firstName", "lastName", "userName", "password",
            "isActive", "specialization", "trainings");
    private static final Set<String> TRAINING_DTO_COVERED_FIELDS = Set.of(
            "id", "traineeId", "trainerId", "trainingName",
            "trainingType", "trainingDate", "trainingDuration");

    private final ModelMapper mapper = ModelMapperFactory.create();

    @Test
    void trainee_shouldHaveNoUncoveredFields() {
        Set<String> actualFields = getAllFieldNames(Trainee.class);
        assertThat(actualFields).containsExactlyInAnyOrderElementsOf(TRAINEE_COVERED_FIELDS);
    }

    @Test
    void trainer_shouldHaveNoUncoveredFields() {
        Set<String> actualFields = getAllFieldNames(Trainer.class);
        assertThat(actualFields).containsExactlyInAnyOrderElementsOf(TRAINER_COVERED_FIELDS);
    }

    @Test
    void training_shouldHaveNoUncoveredFields() {
        Set<String> actualFields = getAllFieldNames(Training.class);
        assertThat(actualFields).containsExactlyInAnyOrderElementsOf(TRAINING_COVERED_FIELDS);
    }

    @Test
    void traineeDTO_shouldHaveNoUncoveredFields() {
        Set<String> actualFields = getAllFieldNames(TraineeDTO.class);
        assertThat(actualFields).containsExactlyInAnyOrderElementsOf(TRAINEE_DTO_COVERED_FIELDS);
    }

    @Test
    void trainerDTO_shouldHaveNoUncoveredFields() {
        Set<String> actualFields = getAllFieldNames(TrainerDTO.class);
        assertThat(actualFields).containsExactlyInAnyOrderElementsOf(TRAINER_DTO_COVERED_FIELDS);
    }

    @Test
    void trainingDTO_shouldHaveNoUncoveredFields() {
        Set<String> actualFields = getAllFieldNames(TrainingDTO.class);
        assertThat(actualFields).containsExactlyInAnyOrderElementsOf(TRAINING_DTO_COVERED_FIELDS);
    }

    @Test
    void map_shouldCopyEveryField_fromTraineeToTraineeDTO() {
        TrainingType trainingType = TrainingType.builder().trainingTypeName(TRAINING_TYPE_NAME).build();
        Training training = Training.builder()
                .id(TRAINING_ID)
                .trainingName(TRAINING_NAME)
                .trainingType(trainingType)
                .trainingDate(TRAINING_DATE)
                .trainingDuration(TRAINING_DURATION)
                .build();
        Trainee trainee = Trainee.builder()
                .userId(USER_ID)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .userName(USERNAME)
                .password(PASSWORD)
                .isActive(IS_ACTIVE)
                .dateOfBirth(DATE_OF_BIRTH)
                .address(ADDRESS)
                .trainings(List.of(training))
                .build();

        TraineeDTO actual = mapper.map(trainee, TraineeDTO.class);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(actual.getUserId()).isEqualTo(USER_ID);
        softly.assertThat(actual.getFirstName()).isEqualTo(FIRST_NAME);
        softly.assertThat(actual.getLastName()).isEqualTo(LAST_NAME);
        softly.assertThat(actual.getUserName()).isEqualTo(USERNAME);
        softly.assertThat(actual.getPassword()).isEqualTo(PASSWORD);
        softly.assertThat(actual.getIsActive()).isEqualTo(IS_ACTIVE);
        softly.assertThat(actual.getDateOfBirth()).isEqualTo(DATE_OF_BIRTH);
        softly.assertThat(actual.getAddress()).isEqualTo(ADDRESS);
        softly.assertThat(actual.getTrainings()).hasSize(1);
        softly.assertThat(actual.getTrainings().getFirst().getId()).isEqualTo(TRAINING_ID);
        softly.assertAll();
    }

    @Test
    void map_shouldCopyEveryField_fromTraineeDTOToTrainee() {
        TraineeDTO dto = new TraineeDTO();
        dto.setUserId(USER_ID);
        dto.setFirstName(FIRST_NAME);
        dto.setLastName(LAST_NAME);
        dto.setUserName(USERNAME);
        dto.setPassword(PASSWORD);
        dto.setIsActive(IS_ACTIVE);
        dto.setDateOfBirth(DATE_OF_BIRTH);
        dto.setAddress(ADDRESS);

        Trainee actual = mapper.map(dto, Trainee.class);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(actual.getUserId()).isEqualTo(USER_ID);
        softly.assertThat(actual.getFirstName()).isEqualTo(FIRST_NAME);
        softly.assertThat(actual.getLastName()).isEqualTo(LAST_NAME);
        softly.assertThat(actual.getUserName()).isEqualTo(USERNAME);
        softly.assertThat(actual.getPassword()).isEqualTo(PASSWORD);
        softly.assertThat(actual.getIsActive()).isEqualTo(IS_ACTIVE);
        softly.assertThat(actual.getDateOfBirth()).isEqualTo(DATE_OF_BIRTH);
        softly.assertThat(actual.getAddress()).isEqualTo(ADDRESS);
        softly.assertAll();
    }

    @Test
    void map_shouldCopyEveryField_fromTrainerToTrainerDTO() {
        TrainingType specialization = TrainingType.builder().trainingTypeName(TRAINING_TYPE_NAME).build();
        Training training = Training.builder()
                .id(TRAINING_ID)
                .trainingName(TRAINING_NAME)
                .build();
        Trainer trainer = Trainer.builder()
                .userId(USER_ID)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .userName(USERNAME)
                .password(PASSWORD)
                .isActive(IS_ACTIVE)
                .specialization(specialization)
                .trainings(List.of(training))
                .build();

        TrainerDTO actual = mapper.map(trainer, TrainerDTO.class);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(actual.getUserId()).isEqualTo(USER_ID);
        softly.assertThat(actual.getFirstName()).isEqualTo(FIRST_NAME);
        softly.assertThat(actual.getLastName()).isEqualTo(LAST_NAME);
        softly.assertThat(actual.getUserName()).isEqualTo(USERNAME);
        softly.assertThat(actual.getPassword()).isEqualTo(PASSWORD);
        softly.assertThat(actual.getIsActive()).isEqualTo(IS_ACTIVE);
        softly.assertThat(actual.getSpecialization()).isEqualTo(specialization);
        softly.assertThat(actual.getSpecialization().getTrainingTypeName()).isEqualTo(TRAINING_TYPE_NAME);
        softly.assertThat(actual.getTrainings()).hasSize(1);
        softly.assertThat(actual.getTrainings().getFirst().getId()).isEqualTo(TRAINING_ID);
        softly.assertAll();
    }

    @Test
    void map_shouldCopyEveryField_fromTrainerDTOToTrainer() {
        TrainingType specialization = TrainingType.builder().trainingTypeName(TRAINING_TYPE_NAME).build();
        TrainerDTO dto = new TrainerDTO();
        dto.setUserId(USER_ID);
        dto.setFirstName(FIRST_NAME);
        dto.setLastName(LAST_NAME);
        dto.setUserName(USERNAME);
        dto.setPassword(PASSWORD);
        dto.setIsActive(IS_ACTIVE);
        dto.setSpecialization(specialization);

        Trainer actual = mapper.map(dto, Trainer.class);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(actual.getUserId()).isEqualTo(USER_ID);
        softly.assertThat(actual.getFirstName()).isEqualTo(FIRST_NAME);
        softly.assertThat(actual.getLastName()).isEqualTo(LAST_NAME);
        softly.assertThat(actual.getUserName()).isEqualTo(USERNAME);
        softly.assertThat(actual.getPassword()).isEqualTo(PASSWORD);
        softly.assertThat(actual.getIsActive()).isEqualTo(IS_ACTIVE);
        softly.assertThat(actual.getSpecialization()).isEqualTo(specialization);
        softly.assertAll();
    }

    @Test
    void map_shouldCopyEveryField_fromTrainingToTrainingDTO() {
        TrainingType trainingType = TrainingType.builder().trainingTypeName(TRAINING_TYPE_NAME).build();
        Training training = Training.builder()
                .id(TRAINING_ID)
                .traineeId(USER_ID)
                .trainerId(USER_ID + 1)
                .trainingName(TRAINING_NAME)
                .trainingType(trainingType)
                .trainingDate(TRAINING_DATE)
                .trainingDuration(TRAINING_DURATION)
                .build();

        TrainingDTO actual = mapper.map(training, TrainingDTO.class);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(actual.getId()).isEqualTo(TRAINING_ID);
        softly.assertThat(actual.getTraineeId()).isEqualTo(USER_ID);
        softly.assertThat(actual.getTrainerId()).isEqualTo(USER_ID + 1);
        softly.assertThat(actual.getTrainingName()).isEqualTo(TRAINING_NAME);
        softly.assertThat(actual.getTrainingType()).isEqualTo(trainingType);
        softly.assertThat(actual.getTrainingDate()).isEqualTo(TRAINING_DATE);
        softly.assertThat(actual.getTrainingDuration()).isEqualTo(TRAINING_DURATION);
        softly.assertAll();
    }

    @Test
    void map_shouldCopyEveryField_fromTrainingDTOToTraining() {
        TrainingType trainingType = TrainingType.builder().trainingTypeName(TRAINING_TYPE_NAME).build();
        TrainingDTO dto = new TrainingDTO();
        dto.setId(TRAINING_ID);
        dto.setTraineeId(USER_ID);
        dto.setTrainerId(USER_ID + 1);
        dto.setTrainingName(TRAINING_NAME);
        dto.setTrainingType(trainingType);
        dto.setTrainingDate(TRAINING_DATE);
        dto.setTrainingDuration(TRAINING_DURATION);

        Training actual = mapper.map(dto, Training.class);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(actual.getId()).isEqualTo(TRAINING_ID);
        softly.assertThat(actual.getTraineeId()).isEqualTo(USER_ID);
        softly.assertThat(actual.getTrainerId()).isEqualTo(USER_ID + 1);
        softly.assertThat(actual.getTrainingName()).isEqualTo(TRAINING_NAME);
        softly.assertThat(actual.getTrainingType()).isEqualTo(trainingType);
        softly.assertThat(actual.getTrainingDate()).isEqualTo(TRAINING_DATE);
        softly.assertThat(actual.getTrainingDuration()).isEqualTo(TRAINING_DURATION);
        softly.assertAll();
    }

    private Set<String> getAllFieldNames(Class<?> clazz) {
        Set<String> fieldNames = new HashSet<>();
        Class<?> current = clazz;

        while (current != null && current != Object.class) {
            for (Field field : current.getDeclaredFields()) {
                fieldNames.add(field.getName());
            }
            current = current.getSuperclass();
        }

        return fieldNames;
    }
}