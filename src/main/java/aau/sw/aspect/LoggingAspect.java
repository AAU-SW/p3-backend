package aau.sw.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    // Created my own AOP for loggs
    @Around("@annotation(logExecution) && within(aau.sw.controller..*)")
    public Object logWithAnnotation(ProceedingJoinPoint joinPoint, LogExecution logExecution) throws Throwable {
        try {
            // Execute the original method
            Object result = joinPoint.proceed();
            // Dynamically log 'id' field if it exists
            if (result != null) {
                Object body = result instanceof ResponseEntity ? ((ResponseEntity<?>) result).getBody() : result;

                if (body != null) {
                    try {
                        Field idField = body.getClass().getDeclaredField("id");
                        idField.setAccessible(true);
                        Object idValue = idField.get(body);
                        // Log annotation message
                        log.info("{} object ID: {}", logExecution.value(), idValue);
                    } catch (NoSuchFieldException ignored) {
                        // Ignore if no 'id' field exists
                    }
                }
            }

            return result;

        } catch (Exception ex) {
            log.error("Method {} status: BAD, failed: {}", ex.getMessage(), ex);
            throw ex;
        }
    }
}