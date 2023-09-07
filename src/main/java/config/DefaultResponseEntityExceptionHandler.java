package config;

import config.common.CurrentDateTime;
import config.exception.HeaderAuthorizationNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class DefaultResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        return sendResponse(ex.getClass().toString(), ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HeaderAuthorizationNotFoundException.class)
    public ResponseEntity<Object> handleCustomRequestException(HeaderAuthorizationNotFoundException ex) {
        return sendResponse(ex.getClass().toString(), ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException ex) {
        return sendResponse(ex.getClass().toString(), ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<Object> handleMalformedJwtExceptionException(MalformedJwtException ex) {
        return sendResponse(ex.getClass().toString(), ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> sendResponse(String message, String description, HttpStatus status) {
        ErrorResponse errorResponse = new ErrorResponse(message, description, CurrentDateTime.defaultFormat(), status.value());
        return new ResponseEntity<>(errorResponse, status);
    }
}
