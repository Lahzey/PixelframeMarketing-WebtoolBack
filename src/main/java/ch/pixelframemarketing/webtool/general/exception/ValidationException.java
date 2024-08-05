package ch.pixelframemarketing.webtool.general.exception;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;

@Getter
public class ValidationException extends RuntimeException {
    private Map<String, String> errors;

    public ValidationException(String key, String message) {
        this(Collections.singletonMap(key, message));
    }

    public ValidationException(Map<String, String> errors) {
        super(generateMessage(errors));
        this.errors = errors;
    }
    
    private static String generateMessage(Map<String , String> errors) {
        if (errors.isEmpty()) return "Validation failed.";
        else if (errors.size() == 1) return errors.values().iterator().next();
        else return "[1/" + errors.size() + "] " + errors.values().iterator().next();
    }

}