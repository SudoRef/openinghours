package my.project.openinghours.exceptions;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.DateTimeException;

@ControllerAdvice
@Log4j2
public class CustomResponseEntityExceptionHandler {

    @ExceptionHandler({DateTimeException.class, HttpMessageNotReadableException.class, IllegalArgumentException.class})
    public ResponseEntity<ApiError> handleException(Exception exception) {
        ApiError apiError = new ApiError(exception.getMessage());
        log.error(exception.getMessage());
        return new ResponseEntity<>(apiError, null, HttpStatus.BAD_REQUEST);
    }
}