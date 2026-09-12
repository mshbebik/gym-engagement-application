package com.gym.engagement.config;

import com.gym.engagement.aspect.LoggingAspect;
import com.gym.engagement.dao.impl.TraineeDao;
import com.gym.engagement.dao.impl.TrainerDao;
import com.gym.engagement.service.TraineeService;
import com.gym.engagement.service.common.UserCredentialsManager;
import com.gym.engagement.service.impl.TraineeServiceImpl;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableAspectJAutoProxy
public class LoggingAspectTestConfig {

    private final TraineeDao traineeDao = Mockito.mock(TraineeDao.class);

    @Bean
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }

    @Bean
    public TraineeService traineeService() {
        TraineeServiceImpl service = new TraineeServiceImpl();
        service.setCredentialsManager(Mockito.mock(UserCredentialsManager.class));
        service.setPasswordEncoder(Mockito.mock(PasswordEncoder.class));
        service.setTrainerDao(Mockito.mock(TrainerDao.class));
        service.setTraineeDao(traineeDao);
        return service;
    }

    public TraineeDao getTraineeDao() {
        return traineeDao;
    }
}