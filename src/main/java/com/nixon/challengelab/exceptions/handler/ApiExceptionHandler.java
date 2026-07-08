package com.nixon.challengelab.exceptions.handler;

import com.nixon.challengelab.dto.response.MethodArgNotValidExceptionResponse;
import com.nixon.challengelab.dto.response.StandardErrorResponse;
import com.nixon.challengelab.dto.response.ValidationErrors;
import com.nixon.challengelab.exceptions.ConflictException;
import com.nixon.challengelab.exceptions.ForbiddenException;
import com.nixon.challengelab.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardErrorResponse> handleEntityNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        StandardErrorResponse response = new StandardErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                OffsetDateTime.now(),
                request.getServletPath(),
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<StandardErrorResponse> handleConflictException(ConflictException ex, HttpServletRequest request) {
        StandardErrorResponse response = new StandardErrorResponse(
                HttpStatus.CONFLICT.value(),
                OffsetDateTime.now(),
                request.getServletPath(),
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<StandardErrorResponse> handleForbiddenException(ConflictException ex, HttpServletRequest request) {
        StandardErrorResponse response = new StandardErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                OffsetDateTime.now(),
                request.getServletPath(),
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardErrorResponse> handleException(Exception ex, HttpServletRequest request) {
        StandardErrorResponse response = new StandardErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                OffsetDateTime.now(),
                request.getServletPath(),
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public  ResponseEntity<MethodArgNotValidExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        StandardErrorResponse response = new StandardErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                OffsetDateTime.now(),
                request.getServletPath(),
                "A validation Error Occurred!"
        );

        List<ValidationErrors> fields = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach((fieldError) -> {
            fields.add(new ValidationErrors(fieldError.getField(), fieldError.getDefaultMessage()));
        });

        return new ResponseEntity<>(new MethodArgNotValidExceptionResponse(
                response, fields
        ), HttpStatus.BAD_REQUEST);
    }


}
