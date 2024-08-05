package ch.pixelframemarketing.webtool.general.config;

import ch.pixelframemarketing.webtool.api.dto.ExceptionDTO;
import ch.pixelframemarketing.webtool.general.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ExceptionDTO> handleValidationException(ValidationException ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new ExceptionDTO(ex), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDTO> handleGeneralException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new ExceptionDTO(ex), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}