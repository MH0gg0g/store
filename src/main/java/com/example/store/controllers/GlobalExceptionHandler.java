package com.example.store.controllers;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.store.dtos.ErrorDto;
import com.example.store.exceptions.CartEmptyException;
import com.example.store.exceptions.CartNotFoundException;
import com.example.store.exceptions.DublicateEmailException;
import com.example.store.exceptions.InvalidPasswordException;
import com.example.store.exceptions.InvalidTokenException;
import com.example.store.exceptions.OrderNotFoundException;
import com.example.store.exceptions.ProductNotFoundException;
import com.example.store.exceptions.UserNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleUnreadableMessage(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", "Invalid request body"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDto> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDto("Access Denied"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<com.example.store.dtos.ErrorDto> handleValidationErrors(
            MethodArgumentNotValidException ex) {
        var erros = new HashMap<String, String>();

        ex.getBindingResult().getFieldErrors().forEach(er -> erros.put(er.getField(), er.getDefaultMessage()));

        return ResponseEntity.badRequest().body(new com.example.store.dtos.ErrorDto("Validation failed", erros));
    }

    @ExceptionHandler({ UserNotFoundException.class, CartNotFoundException.class, OrderNotFoundException.class })
    public ResponseEntity<ErrorDto> handleNotFoundException(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler({ InvalidPasswordException.class, BadCredentialsException.class })
    public ResponseEntity<ErrorDto> handleUnauthorizedException(Exception ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler({ DublicateEmailException.class, ProductNotFoundException.class, InvalidTokenException.class,
            CartEmptyException.class })
    public ResponseEntity<ErrorDto> handleBadRequestException(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleGeneralError(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto(ex.getMessage()));
    }

}
