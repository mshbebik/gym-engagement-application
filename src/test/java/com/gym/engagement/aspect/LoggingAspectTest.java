package com.gym.engagement.aspect;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.gym.engagement.exception.EntityNotFoundException;
import com.gym.engagement.model.Trainee;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoggingAspectTest {

    private static final String TRAINEE_SIGNATURE = "TraineeServiceImpl.createTrainee(..)";
    private static final String SELECT_SIGNATURE = "TraineeServiceImpl.selectTraineeById(..)";
    private static final String TRAINING_LIST_SIGNATURE = "TrainingServiceImpl.selectTrainingsByTrainer(..)";
    private static final String DELETE_SIGNATURE = "TraineeServiceImpl.deleteTrainee(..)";
    private static final Long USER_ID = 99L;
    private static final String NOT_FOUND_MESSAGE = "Trainee with id: 99 was not found";
    private static final String USERNAME = "john.smith";
    private static final String ADDRESS = "123 Main St";
    private static final String PASSWORD = "supersecret123";

    private final Trainee traineeWithPassword = constructTraineeWithPassword();

    private ListAppender<ILoggingEvent> listAppender;
    private Logger aspectLogger;
    private LoggingAspect aspect;

    @BeforeEach
    void setUp() {
        aspect = new LoggingAspect();
        aspectLogger = (Logger) LoggerFactory.getLogger(LoggingAspect.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        aspectLogger.addAppender(listAppender);
    }

    @AfterEach
    void tearDown() {
        aspectLogger.detachAppender(listAppender);
    }

    @Test
    void logMethodExecution_shouldLogEntryAtDebug_andSuccessAtInfo() throws Throwable {
        ProceedingJoinPoint joinPoint = mockJoinPoint(TRAINEE_SIGNATURE, new Object[]{USER_ID});
        when(joinPoint.proceed()).thenReturn("result");

        aspect.logMethodExecution(joinPoint);

        assertThat(listAppender.list)
                .anyMatch(e -> e.getLevel() == Level.DEBUG && e.getFormattedMessage().contains("Entering"));
        assertThat(listAppender.list)
                .anyMatch(e -> e.getLevel() == Level.INFO && e.getFormattedMessage().contains("Executed"));
    }

    @Test
    void logMethodExecution_shouldReturnProceedResult_unchanged() throws Throwable {
        ProceedingJoinPoint joinPoint = mockJoinPoint(TRAINEE_SIGNATURE, new Object[]{});
        when(joinPoint.proceed()).thenReturn("actualResult");

        Object actual = aspect.logMethodExecution(joinPoint);

        assertThat(actual).isEqualTo("actualResult");
    }

    @Test
    void logMethodExecution_shouldLogWarn_whenEntityNotFoundExceptionThrown() throws Throwable {
        ProceedingJoinPoint joinPoint = mockJoinPoint(SELECT_SIGNATURE, new Object[]{USER_ID});
        when(joinPoint.proceed()).thenThrow(new EntityNotFoundException(NOT_FOUND_MESSAGE));

        assertThatThrownBy(() -> aspect.logMethodExecution(joinPoint))
                .isInstanceOf(EntityNotFoundException.class);
        assertThat(listAppender.list)
                .anyMatch(e -> e.getLevel() == Level.WARN && e.getFormattedMessage().contains(NOT_FOUND_MESSAGE));
        assertThat(listAppender.list).noneMatch(e -> e.getLevel() == Level.ERROR);
    }

    @Test
    void logMethodExecution_shouldLogWarn_whenNullPointerExceptionThrown() throws Throwable {
        ProceedingJoinPoint joinPoint = mockJoinPoint(TRAINEE_SIGNATURE, new Object[]{});
        when(joinPoint.proceed()).thenThrow(new NullPointerException());

        assertThatThrownBy(() -> aspect.logMethodExecution(joinPoint))
                .isInstanceOf(NullPointerException.class);
        assertThat(listAppender.list).anyMatch(e -> e.getLevel() == Level.WARN);
        assertThat(listAppender.list).noneMatch(e -> e.getLevel() == Level.ERROR);
    }

    @Test
    void logMethodExecution_shouldLogError_whenUnexpectedExceptionThrown() throws Throwable {
        ProceedingJoinPoint joinPoint = mockJoinPoint(TRAINEE_SIGNATURE, new Object[]{});
        RuntimeException unexpected = new RuntimeException("db connection lost");

        when(joinPoint.proceed()).thenThrow(unexpected);

        assertThatThrownBy(() -> aspect.logMethodExecution(joinPoint))
                .isInstanceOf(RuntimeException.class);

        assertThat(listAppender.list)
                .anyMatch(e -> e.getLevel() == Level.ERROR
                        && e.getFormattedMessage().contains("unexpected exception")
                        && e.getThrowableProxy() != null);
    }

    @Test
    void logMethodExecution_shouldRethrowOriginalException_unchanged() throws Throwable {
        ProceedingJoinPoint joinPoint = mockJoinPoint(TRAINEE_SIGNATURE, new Object[]{});
        EntityNotFoundException original = new EntityNotFoundException("not found");

        when(joinPoint.proceed()).thenThrow(original);

        assertThatThrownBy(() -> aspect.logMethodExecution(joinPoint))
                .isSameAs(original);
    }

    @Test
    void logMethodExecution_shouldRedactPasswordField_declaredOnSuperclass() throws Throwable {
        ProceedingJoinPoint joinPoint = mockJoinPoint(TRAINEE_SIGNATURE, new Object[]{traineeWithPassword});
        when(joinPoint.proceed()).thenReturn(traineeWithPassword);

        aspect.logMethodExecution(joinPoint);

        String actualLogOutput = collectLogOutput();

        assertThat(actualLogOutput).doesNotContain(PASSWORD);
        assertThat(actualLogOutput).contains("password=REDACTED");
        assertThat(actualLogOutput).contains("userName=" + USERNAME);
        assertThat(actualLogOutput).contains("address=" + ADDRESS);
    }

    @Test
    void logMethodExecution_shouldSummarizeCollectionResult_bySizeOnly() throws Throwable {
        ProceedingJoinPoint joinPoint = mockJoinPoint(TRAINING_LIST_SIGNATURE, new Object[]{});
        when(joinPoint.proceed()).thenReturn(List.of("a", "b", "c"));

        aspect.logMethodExecution(joinPoint);

        assertThat(listAppender.list)
                .anyMatch(e -> e.getLevel() == Level.INFO
                        && e.getFormattedMessage().contains("Collection[size=3]"));
    }

    @Test
    void logMethodExecution_shouldHandleNullArgs_withoutThrowing() throws Throwable {
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        Signature signature = mock(Signature.class);

        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.toShortString()).thenReturn(DELETE_SIGNATURE);
        when(joinPoint.getArgs()).thenReturn(null);
        when(joinPoint.proceed()).thenReturn(null);

        assertThatCode(() -> aspect.logMethodExecution(joinPoint)).doesNotThrowAnyException();
    }

    private String collectLogOutput() {
        return listAppender.list.stream()
                .map(ILoggingEvent::getFormattedMessage)
                .collect(Collectors.joining("\n"));
    }

    private ProceedingJoinPoint mockJoinPoint(String signatureText, Object[] args) {
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        Signature signature = mock(Signature.class);

        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.toShortString()).thenReturn(signatureText);
        when(joinPoint.getArgs()).thenReturn(args);

        return joinPoint;
    }

    private Trainee constructTraineeWithPassword() {
        return Trainee.builder()
                .userId(1L)
                .userName(USERNAME)
                .password(PASSWORD)
                .address(ADDRESS)
                .build();
    }
}