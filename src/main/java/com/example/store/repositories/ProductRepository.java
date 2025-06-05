package com.example.store.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.store.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryId(Long categoryId);


    

}