package ch.pixelframemarketing.webtool.api.dto;

import ch.pixelframemarketing.webtool.general.exception.ValidationException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
public class ExceptionDTO {
    
    public String error;
    public Map<String, String> errorMapping;
    
    // maybe remove this in the future, really useful for development though
    public String errorType;
    public String stackTrace;
    
    public ExceptionDTO(Exception e) {
        this.error = e.getMessage();
        this.errorMapping = new HashMap<>();
        this.errorType = e.getClass().getName();
        this.stackTrace = ExceptionUtils.getStackTrace(e);
    }
    
    public ExceptionDTO(ValidationException e) {
        this.error = e.getMessage();
        this.errorMapping = e.getErrors();
        this.errorType = e.getClass().getName();
        this.stackTrace = ExceptionUtils.getStackTrace(e);
    }
    
}
