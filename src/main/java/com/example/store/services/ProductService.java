package com.example.store.services;

import java.util.List;

import com.example.store.dtos.ProductDto;

public interface ProductService {
    List<ProductDto> getAllProducts(Long categoryId);
    ProductDto getProductById(Long productId);
    ProductDto createProduct(ProductDto productRequest);
    ProductDto updateProduct(Long productId, ProductDto productRequest);
    void removeProduct(Long productId);
    
}
