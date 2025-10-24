
package com.example.store.dtos;

import java.util.Map;

import lombok.Data;

@Data
public class ErrorDto {
    private String error;

    private Map<String, String> details;

    public ErrorDto(String error) {
        this.error = error;
    }

    public ErrorDto(String error, Map<String, String> details) {
        this.error = error;
        this.details = details;
    }

}

