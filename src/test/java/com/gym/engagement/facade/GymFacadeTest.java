package com.gym.engagement.facade;

import com.gym.engagement.dto.TraineeDTO;
import com.gym.engagement.dto.TrainerDTO;
import com.gym.engagement.dto.TrainingDTO;
import com.gym.engagement.model.Trainee;
import com.gym.engagement.model.Trainer;
import com.gym.engagement.model.Training;
import com.gym.engagement.service.TraineeService;
import com.gym.engagement.service.TrainerService;
import com.gym.engagement.service.TrainingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GymFacadeTest {

    private static final Long ENTITY_ID = 1L;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TrainingService trainingService;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private GymFacade facade;

    @Test
    void createTrainee_shouldMapDtoToEntity_delegateToService_andMapResultBack() {
        TraineeDTO inputDto = new TraineeDTO();
        Trainee mappedEntity = Trainee.builder().userId(ENTITY_ID).build();
        Trainee createdEntity = Trainee.builder().userId(ENTITY_ID).build();
        TraineeDTO expectedResultDto = new TraineeDTO();

        when(mapper.map(inputDto, Trainee.class)).thenReturn(mappedEntity);
        when(traineeService.createTrainee(mappedEntity)).thenReturn(createdEntity);
        when(mapper.map(createdEntity, TraineeDTO.class)).thenReturn(expectedResultDto);

        TraineeDTO actual = facade.createTrainee(inputDto);

        assertThat(actual).isSameAs(expectedResultDto);
        verify(traineeService).createTrainee(mappedEntity);
    }

    @Test
    void updateTrainee_shouldMapDelegateAndMapBack() {
        TraineeDTO inputDto = new TraineeDTO();
        Trainee mappedEntity = Trainee.builder().userId(ENTITY_ID).build();
        Trainee updatedEntity = Trainee.builder().userId(ENTITY_ID).build();
        TraineeDTO expectedResultDto = new TraineeDTO();

        when(mapper.map(inputDto, Trainee.class)).thenReturn(mappedEntity);
        when(traineeService.updateTrainee(mappedEntity)).thenReturn(updatedEntity);
        when(mapper.map(updatedEntity, TraineeDTO.class)).thenReturn(expectedResultDto);

        TraineeDTO actual = facade.updateTrainee(inputDto);

        assertThat(actual).isSameAs(expectedResultDto);
        verify(traineeService).updateTrainee(mappedEntity);
    }

    @Test
    void deleteTrainee_shouldDelegateToService() {
        facade.deleteTrainee(ENTITY_ID);

        verify(traineeService).deleteTrainee(ENTITY_ID);
        verifyNoInteractions(mapper);
    }

    @Test
    void selectTraineeById_shouldDelegateAndMapResult() {
        Trainee selectedEntity = Trainee.builder().userId(ENTITY_ID).build();
        TraineeDTO expectedResultDto = new TraineeDTO();

        when(traineeService.selectTraineeById(ENTITY_ID)).thenReturn(selectedEntity);
        when(mapper.map(selectedEntity, TraineeDTO.class)).thenReturn(expectedResultDto);

        TraineeDTO actual = facade.selectTraineeById(ENTITY_ID);

        assertThat(actual).isSameAs(expectedResultDto);
    }

    @Test
    void createTrainer_shouldMapDelegateAndMapBack() {
        TrainerDTO inputDto = new TrainerDTO();
        Trainer mappedEntity = Trainer.builder().userId(ENTITY_ID).build();
        Trainer createdEntity = Trainer.builder().userId(ENTITY_ID).build();
        TrainerDTO expectedResultDto = new TrainerDTO();

        when(mapper.map(inputDto, Trainer.class)).thenReturn(mappedEntity);
        when(trainerService.createTrainer(mappedEntity)).thenReturn(createdEntity);
        when(mapper.map(createdEntity, TrainerDTO.class)).thenReturn(expectedResultDto);

        TrainerDTO actual = facade.createTrainer(inputDto);

        assertThat(actual).isSameAs(expectedResultDto);
    }

    @Test
    void updateTrainer_shouldMapDelegateAndMapBack() {
        TrainerDTO inputDto = new TrainerDTO();
        Trainer mappedEntity = Trainer.builder().userId(ENTITY_ID).build();
        Trainer updatedEntity = Trainer.builder().userId(ENTITY_ID).build();
        TrainerDTO expectedResultDto = new TrainerDTO();

        when(mapper.map(inputDto, Trainer.class)).thenReturn(mappedEntity);
        when(trainerService.updateTrainer(mappedEntity)).thenReturn(updatedEntity);
        when(mapper.map(updatedEntity, TrainerDTO.class)).thenReturn(expectedResultDto);

        TrainerDTO actual = facade.updateTrainer(inputDto);

        assertThat(actual).isSameAs(expectedResultDto);
    }

    @Test
    void selectTrainerById_shouldDelegateAndMapResult() {
        Trainer selectedEntity = Trainer.builder().userId(ENTITY_ID).build();
        TrainerDTO expectedResultDto = new TrainerDTO();

        when(trainerService.selectTrainerById(ENTITY_ID)).thenReturn(selectedEntity);
        when(mapper.map(selectedEntity, TrainerDTO.class)).thenReturn(expectedResultDto);

        TrainerDTO actual = facade.selectTrainerById(ENTITY_ID);

        assertThat(actual).isSameAs(expectedResultDto);
    }

    @Test
    void createTraining_shouldMapDelegateAndMapBack() {
        TrainingDTO inputDto = new TrainingDTO();
        Training mappedEntity = Training.builder().id(ENTITY_ID).build();
        Training createdEntity = Training.builder().id(ENTITY_ID).build();
        TrainingDTO expectedResultDto = new TrainingDTO();

        when(mapper.map(inputDto, Training.class)).thenReturn(mappedEntity);
        when(trainingService.createTraining(mappedEntity)).thenReturn(createdEntity);
        when(mapper.map(createdEntity, TrainingDTO.class)).thenReturn(expectedResultDto);

        TrainingDTO actual = facade.createTraining(inputDto);

        assertThat(actual).isSameAs(expectedResultDto);
    }

    @Test
    void selectTrainingById_shouldDelegateAndMapResult() {
        Training selectedEntity = Training.builder().id(ENTITY_ID).build();
        TrainingDTO expectedResultDto = new TrainingDTO();

        when(trainingService.selectTrainingById(ENTITY_ID)).thenReturn(selectedEntity);
        when(mapper.map(selectedEntity, TrainingDTO.class)).thenReturn(expectedResultDto);

        TrainingDTO actual = facade.selectTrainingById(ENTITY_ID);

        assertThat(actual).isSameAs(expectedResultDto);
    }

    @Test
    void selectTrainingsByTrainer_shouldMapDelegateAndMapEachResult() {
        TrainerDTO trainerDto = new TrainerDTO();
        Trainer mappedTrainer = Trainer.builder().userId(ENTITY_ID).build();
        Training training1 = Training.builder().id(ENTITY_ID).build();
        Training training2 = Training.builder().id(ENTITY_ID + 1L).build();
        TrainingDTO dto1 = new TrainingDTO();
        TrainingDTO dto2 = new TrainingDTO();

        when(mapper.map(trainerDto, Trainer.class)).thenReturn(mappedTrainer);
        when(trainingService.selectTrainingsByTrainer(mappedTrainer)).thenReturn(List.of(training1, training2));
        when(mapper.map(training1, TrainingDTO.class)).thenReturn(dto1);
        when(mapper.map(training2, TrainingDTO.class)).thenReturn(dto2);

        List<TrainingDTO> actual = facade.selectTrainingsByTrainer(trainerDto);

        assertThat(actual).containsExactly(dto1, dto2);
    }

    @Test
    void selectTrainingsByTrainer_shouldReturnEmptyList_whenNoTrainingsFound() {
        TrainerDTO trainerDto = new TrainerDTO();
        Trainer mappedTrainer = Trainer.builder().userId(ENTITY_ID).build();

        when(mapper.map(trainerDto, Trainer.class)).thenReturn(mappedTrainer);
        when(trainingService.selectTrainingsByTrainer(mappedTrainer)).thenReturn(List.of());

        List<TrainingDTO> actual = facade.selectTrainingsByTrainer(trainerDto);

        assertThat(actual).isEmpty();
    }

    @Test
    void selectTrainingsByTrainee_shouldMapDelegateAndMapEachResult() {
        TraineeDTO traineeDto = new TraineeDTO();
        Trainee mappedTrainee = Trainee.builder().userId(ENTITY_ID).build();
        Training training1 = Training.builder().id(ENTITY_ID).build();
        TrainingDTO dto1 = new TrainingDTO();

        when(mapper.map(traineeDto, Trainee.class)).thenReturn(mappedTrainee);
        when(trainingService.selectTrainingsByTrainee(mappedTrainee)).thenReturn(List.of(training1));
        when(mapper.map(training1, TrainingDTO.class)).thenReturn(dto1);

        List<TrainingDTO> actual = facade.selectTrainingsByTrainee(traineeDto);

        assertThat(actual).containsExactly(dto1);
    }
}
