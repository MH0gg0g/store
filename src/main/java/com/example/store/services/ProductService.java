package com.example.store.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.store.dtos.ProductDto;
import com.example.store.entities.Product;
import com.example.store.exceptions.CategoryNotFoundException;
import com.example.store.exceptions.ProductNotFoundException;
import com.example.store.mappers.ProductMapper;
import com.example.store.repositories.CategoryRepository;
import com.example.store.repositories.ProductRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    public List<ProductDto> getAllProducts(Long categoryId) {
        List<Product> products;

        if (categoryId != null) {
            products = productRepository.findByCategoryId(categoryId);
        } else {
            products = productRepository.findAll();
        }

        return products.stream().map(productMapper::toDto).toList();
    }

    public ProductDto getProductById(Long productId) {
        var product = productRepository.findById(productId).orElse(null);
        if (product == null)
            throw new ProductNotFoundException();
        else
            return productMapper.toDto(product);
    }

    public ProductDto createProduct(ProductDto productRequest) {
        var product = productMapper.toEntity(productRequest);
        var category = categoryRepository.findById(productRequest.getCategoryID()).orElse(null);

        if (category == null) {
            throw new CategoryNotFoundException();
        }

        product.setCategory(category);

        productRepository.save(product);

        productRequest.setId(product.getId());

        return productRequest;
    }

    public ProductDto updateProduct(Long productId, ProductDto productRequest) {
        var product = productRepository.findById(productId).orElse(null);
        var category = categoryRepository.findById(productRequest.getCategoryID()).orElse(null);

        if (product == null) {
            throw new ProductNotFoundException();
        }

        productMapper.update(product, productRequest);
        product.setCategory(category);

        productRepository.save(product);

        productRequest.setId(product.getId());
        return productRequest;
    }

    public void removeProduct(Long productId) {
        var product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            throw new ProductNotFoundException();
        }

        productRepository.delete(product);
    }

}
