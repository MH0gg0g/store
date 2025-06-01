package com.example.store.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.store.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByCategoryId(Byte categoryId);


    

}