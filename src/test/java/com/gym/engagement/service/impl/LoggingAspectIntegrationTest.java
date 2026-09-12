package com.gym.engagement.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.gym.engagement.aspect.LoggingAspect;
import com.gym.engagement.dao.impl.TraineeDao;
import com.gym.engagement.dao.impl.TrainerDao;
import com.gym.engagement.model.Trainee;
import com.gym.engagement.service.TraineeService;
import com.gym.engagement.service.common.UserCredentialsManager;
import java.util.Iterator;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

class LoggingAspectIntegrationTest {

    private static final Long TRAINEE_ID = 1L;

    private TraineeDao traineeDao;
    private Logger logger;
    private ListAppender<ILoggingEvent> logAppender;
    private TraineeService service;

    @BeforeEach
    void setUp() {
        traineeDao = mock(TraineeDao.class);
        TraineeServiceImpl target = new TraineeServiceImpl();
        target.setCredentialsManager(mock(UserCredentialsManager.class));
        target.setPasswordEncoder(mock(PasswordEncoder.class));
        target.setTrainerDao(mock(TrainerDao.class));
        target.setTraineeDao(traineeDao);

        AspectJProxyFactory proxyFactory = new AspectJProxyFactory(target);
        proxyFactory.addAspect(new LoggingAspect());
        service = proxyFactory.getProxy();

        logger = (Logger) LoggerFactory.getLogger(LoggingAspect.class);
        logger.setLevel(Level.DEBUG);
        logAppender = new ListAppender<>();
        logAppender.start();
        logger.addAppender(logAppender);
    }

    @AfterEach
    void tearDown() {
        logger.detachAppender(logAppender);
        logAppender.stop();
    }

    @Test
    void shouldApplyLoggingAspectToTraineeService() {
        Trainee trainee = Trainee.builder()
                .userId(TRAINEE_ID)
                .firstName("John")
                .lastName("Smith")
                .build();

        when(traineeDao.findById(TRAINEE_ID))
                .thenReturn(Optional.of(trainee));

        Trainee actual = service.selectTraineeById(TRAINEE_ID);

        assertThat(AopUtils.isAopProxy(service)).isTrue();
        assertThat(actual).isSameAs(trainee);
        assertThat(logAppender.list).hasSize(2);

        Iterator<ILoggingEvent> iterator = logAppender.list.iterator();
        ILoggingEvent firstEvent = iterator.next();
        ILoggingEvent secondEvent = iterator.next();

        assertThat(firstEvent.getLevel()).isEqualTo(Level.DEBUG);
        assertThat(firstEvent.getFormattedMessage())
                .isEqualTo("Entering TraineeService.selectTraineeById(..) with args: [1]");
        assertThat(secondEvent.getLevel()).isEqualTo(Level.INFO);
        assertThat(secondEvent.getFormattedMessage())
                .contains("Executed TraineeService.selectTraineeById(..) in ")
                .contains("ms, returned: Trainee{");
    }
}