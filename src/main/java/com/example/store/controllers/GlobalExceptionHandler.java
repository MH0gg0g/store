package com.example.store.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.store.dtos.ErrorDto;
import com.example.store.entities.User;
import com.example.store.exceptions.CartEmptyException;
import com.example.store.exceptions.CartNotFoundException;
import com.example.store.exceptions.DublicateEmailException;
import com.example.store.exceptions.InvalidPasswordException;
import com.example.store.exceptions.InvalidTokenException;
import com.example.store.exceptions.OrderNotFoundException;
import com.example.store.exceptions.ProductNotFoundException;
import com.example.store.exceptions.UserNotFoundException;
import com.example.store.services.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final AuthService authService;

    @ExceptionHandler({ UserNotFoundException.class, CartNotFoundException.class, OrderNotFoundException.class })
    public ResponseEntity<ErrorDto> handleNotFoundException(Exception ex) {
        var errorDto = new ErrorDto(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }

    @ExceptionHandler({ InvalidPasswordException.class, BadCredentialsException.class })
    public ResponseEntity<ErrorDto> handleUnauthorizedException(Exception ex) {
        var errorDto = new ErrorDto(HttpStatus.UNAUTHORIZED, ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDto);
    }

    @ExceptionHandler({ DublicateEmailException.class, ProductNotFoundException.class, InvalidTokenException.class,
            CartEmptyException.class })
    public ResponseEntity<ErrorDto> handleBadRequestException(Exception ex) {
        var errorDto = new ErrorDto(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleValidationErrors(MethodArgumentNotValidException ex) {
        var errors = new HashMap<String, String>();
        for (var er : ex.getBindingResult().getFieldErrors()) {
            errors.put(er.getField(), er.getDefaultMessage());
        }

        ErrorDto errorDto = new ErrorDto(HttpStatus.BAD_REQUEST, "Validation failed", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDto> handleIllegalArgumentException(IllegalArgumentException ex) {
        var errorDto = new ErrorDto(HttpStatus.BAD_REQUEST, ex.getMessage(),
                Map.of("Localized Message", ex.getLocalizedMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorDto> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        var errorDto = new ErrorDto(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorDto);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDto> handleMessageNotReadableException(HttpMessageNotReadableException ex) {
        var errorDto = new ErrorDto(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDto> handleAccessDenied(AccessDeniedException ex) {
        User user = authService.getCurrentUser();
        String email = (user != null) ? user.getEmail() : "Anonymous";
        log.warn("Access denied for user: {}. Reason {} " + email, ex.getMessage());

        var errorDto = new ErrorDto(HttpStatus.FORBIDDEN, ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleGeneralErrors(Exception ex) {
        var errorDto = new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
    }
}