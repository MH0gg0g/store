package com.example.store.exceptions;

public class CategoryNotFoundException extends RuntimeException{
    public CategoryNotFoundException(Long categoryId) {
        super("Category with ID " + categoryId + " not found.");
    }
}
