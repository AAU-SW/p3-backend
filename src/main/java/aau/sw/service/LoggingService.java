package aau.sw.service;

import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoggingService {

    private static final Logger log = LoggerFactory.getLogger(LoggingService.class);
    
    public <T> T logExecution(Supplier<T> action, String successMessage) {
        try {
            T result = action.get();
            log.info(successMessage);
            return result;
        } catch (Exception ex) {
            log.error("Operation failed: {}", ex);
            throw ex;
        }

    }
}
