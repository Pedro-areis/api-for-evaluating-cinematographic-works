package com.example.api_ecw.infra.exceptions;

import com.example.api_ecw.infra.exceptions.dto.ErrorMessage;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {
    private ResponseEntity<ErrorMessage> buildResponse(
            HttpStatus status,
            String error,
            String message,
            HttpServletRequest request
    ) {
        ErrorMessage err = new ErrorMessage(
                Instant.now(),
                status.value(),
                error,
                message,
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(err);
    }
    // Error handling during execution
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorMessage> nullPointer (NullPointerException e, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, e.getMessage(), "Null Pointer Exception", request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessage> illegalArgument (IllegalArgumentException e, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, e.getMessage(), "Illegal Argument Exception", request);
    }

    // Persistence errors
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessage> entityNotFound (EntityNotFoundException e, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, e.getMessage(), "Entity Not Found Exception", request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public  ResponseEntity<ErrorMessage> dataIntegrityViolation (DataIntegrityViolationException e, HttpServletRequest request) {
        return  buildResponse(HttpStatus.CONFLICT, e.getMessage(), "Data Integrity Violation", request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorMessage> constraintViolation (ConstraintViolationException e, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, e.getMessage(), "Constraint Violation Exception", request);
    }

    // Web Errors/HTTP protocols
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> methodArgumentNotValid (MethodArgumentNotValidException e, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, e.getMessage(), "Method Argument Not Valid Exception", request);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorMessage> httpRequestMethodNotSupported (HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        return buildResponse(HttpStatus.METHOD_NOT_ALLOWED, e.getMessage(), "HTTP Request Method Not Supported Exception", request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessage> httpMessageNotReadable (HttpMessageNotReadableException e, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, e.getMessage(), "HTTP Message Not Readable Exception", request);
    }

    // Errors of security by Spring
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorMessage> badCredentials (BadCredentialsException e, HttpServletRequest request) {
        return buildResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), "Bad Credentials Exception", request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorMessage> accessDenied (AccessDeniedException e, HttpServletRequest request) {
        return  buildResponse(HttpStatus.FORBIDDEN, e.getMessage(), "Access Denied Exception", request);
    }

    // Standard security errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> exception (Exception e, HttpServletRequest request) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "Exception", request);
    }
}
