package com.example.store.dtos;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class ErrorDto {
    private HttpStatus httpStatus;
    private String message;
    private Map<String, String> details;
    private LocalDateTime timestamp;

    public ErrorDto() {
        super();
        this.timestamp = LocalDateTime.now();
    }

    public ErrorDto(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public ErrorDto(HttpStatus httpStatus, String message, Map<String, String> details) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }
}