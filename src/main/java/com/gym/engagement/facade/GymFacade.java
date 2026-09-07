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
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GymFacade {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final ModelMapper mapper;

    public TraineeDTO createTrainee(TraineeDTO traineeDto) {
        Trainee trainee = mapper.map(traineeDto, Trainee.class);
        Trainee createdTrainee = traineeService.createTrainee(trainee);
        return mapper.map(createdTrainee, TraineeDTO.class);
    }

    public TraineeDTO updateTrainee(TraineeDTO traineeDto) {
        Trainee trainee = mapper.map(traineeDto, Trainee.class);
        Trainee updatedTrainee =  traineeService.updateTrainee(trainee);
        return mapper.map(updatedTrainee, TraineeDTO.class);
    }

    public void deleteTrainee(Long id) {
        traineeService.deleteTrainee(id);
    }

    public TraineeDTO selectTraineeById(Long id) {
        Trainee selectedTrainee = traineeService.selectTraineeById(id);
        return mapper.map(selectedTrainee, TraineeDTO.class);
    }

    public TrainerDTO createTrainer(TrainerDTO trainerDto) {
        Trainer trainer = mapper.map(trainerDto, Trainer.class);
        Trainer createdTrainer = trainerService.createTrainer(trainer);
        return mapper.map(createdTrainer, TrainerDTO.class);
    }

    public TrainerDTO updateTrainer(TrainerDTO trainerDto) {
        Trainer trainer = mapper.map(trainerDto, Trainer.class);
        Trainer updatedTrainer = trainerService.updateTrainer(trainer);
        return mapper.map(updatedTrainer, TrainerDTO.class);
    }

    public TrainerDTO selectTrainerById(Long id) {
        Trainer selectedTrainer = trainerService.selectTrainerById(id);
        return mapper.map(selectedTrainer, TrainerDTO.class);
    }

    public TrainingDTO createTraining(TrainingDTO trainingDto) {
        Training training = mapper.map(trainingDto, Training.class);
        Training createdTraining = trainingService.createTraining(training);
        return mapper.map(createdTraining, TrainingDTO.class);
    }

    public TrainingDTO selectTrainingById(Long id) {
        Training selectedTraining = trainingService.selectTrainingById(id);
        return mapper.map(selectedTraining, TrainingDTO.class);
    }

    public List<TrainingDTO> selectTrainingsByTrainer(TrainerDTO trainerDto) {
        Trainer trainer = mapper.map(trainerDto, Trainer.class);
        List<Training> trainings = trainingService.selectTrainingsByTrainer(trainer);
        return trainings.stream()
                .map(training -> mapper.map(training, TrainingDTO.class))
                .toList();
    }

    public List<TrainingDTO> selectTrainingsByTrainee(TraineeDTO traineeDto) {
        Trainee trainee = mapper.map(traineeDto, Trainee.class);
        List<Training> trainings = trainingService.selectTrainingsByTrainee(trainee);
        return trainings.stream()
                .map(training -> mapper.map(training, TrainingDTO.class))
                .toList();
    }
}
