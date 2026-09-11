package com.gym.engagement.aspect;

import com.gym.engagement.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    private static final Set<String> SENSITIVE_FIELD_NAMES = Set.of("password");

    @Pointcut("within(com.gym.engagement.service..*) && !@annotation(com.gym.engagement.annotation.NoLogging)")
    public void serviceLayer() {}

    @Pointcut("within(com.gym.engagement.dao..*)")
    public void daoLayer() {}

    @Pointcut("within(com.gym.engagement.facade..*)")
    public void facadeLayer() {}

    @Pointcut("serviceLayer() || daoLayer() || facadeLayer()")
    public void applicationLayer() {}

    @Around("applicationLayer()")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        String signature = joinPoint.getSignature().toShortString();
        String args = formatArgs(joinPoint.getArgs());

        log.debug("Entering {} with args: [{}]", signature, args);
        long startTime = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;
            log.info("Executed {} in {}ms, returned: {}", signature, duration, formatResult(result));
            return result;
        } catch (Exception ex) {
            long duration = System.currentTimeMillis() - startTime;
            logException(signature, duration, ex);
            throw ex;
        }
    }

    private void logException(String signature, long duration, Exception ex) {
        if (ex instanceof EntityNotFoundException) {
            log.warn("{} failed after {}ms: {}", signature, duration, ex.getMessage());
        } else if (ex instanceof NullPointerException || ex instanceof IllegalArgumentException) {
            log.warn("{} rejected invalid input after {}ms: {}", signature, duration, ex.getMessage());
        } else {
            log.error("{} threw unexpected exception after {}ms", signature, duration, ex);
        }
    }

    private String formatArgs(Object[] args) {
        if (args == null || args.length == 0) {
            return "";
        }

        return Arrays.stream(args)
                .map(this::formatValue)
                .collect(Collectors.joining(", "));
    }

    private String formatResult(Object result) {
        if (result == null) {
            return "null";
        }

        if (result instanceof Optional<?> optional) {
            return optional.map(this::formatValue).orElse("empty");
        }

        if (result instanceof Collection<?> collection) {
            return "Collection[size=%d]".formatted(collection.size());
        }

        return formatValue(result);
    }

    private String formatValue(Object value) {
        if (value == null) {
            return "null";
        }

        if (isSimpleType(value)) {
            return String.valueOf(value);
        }

        return redactSensitiveFields(value);
    }

    private boolean isSimpleType(Object value) {
        return value instanceof CharSequence
                || value instanceof Number
                || value instanceof Boolean
                || value.getClass().isEnum();
    }

    private String redactSensitiveFields(Object value) {
        List<Field> allFields = getAllFields(value.getClass());

        StringBuilder sb = new StringBuilder(value.getClass().getSimpleName()).append("{");
        for (int i = 0; i < allFields.size(); i++) {
            Field field = allFields.get(i);
            field.setAccessible(true);
            try {
                Object fieldValue = SENSITIVE_FIELD_NAMES.contains(field.getName().toLowerCase())
                        ? "REDACTED"
                        : field.get(value);
                sb.append(field.getName()).append("=").append(fieldValue);

                if (i < allFields.size() - 1) {
                    sb.append(", ");
                }
            } catch (IllegalAccessException e) {
                sb.append(field.getName()).append("=<inaccessible>");
            }
        }

        return sb.append("}").toString();
    }

    private List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        Class<?> current = clazz;

        while (current != null && current != Object.class) {
            fields.addAll(Arrays.asList(current.getDeclaredFields()));
            current = current.getSuperclass();
        }

        return fields;
    }
}