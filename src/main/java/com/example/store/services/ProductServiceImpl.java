package com.example.store.services;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    @Cacheable(value = "products:list", key = "#categoryId != null ? #categoryId : 'ALL'")
    public List<ProductDto> getAllProducts(Long categoryId) {
        List<Product> products;

        if (categoryId != null) {
            products = productRepository.findByCategoryId(categoryId);
        } else {
            products = productRepository.findAll();
        }

        return products.stream().map(productMapper::toDto).toList();
    }

    @Cacheable(value = "products:item", key = "#productId")
    public ProductDto getProductById(Long productId) {
        var product = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);

        return productMapper.toDto(product);
    }

    @CacheEvict(value = "products:list", allEntries = true)
    public ProductDto createProduct(ProductDto productRequest) {
        var product = productMapper.toEntity(productRequest);
        var category = categoryRepository.findById(productRequest.getCategoryID())
                .orElseThrow(CategoryNotFoundException::new);

        product.setCategory(category);

        productRepository.save(product);

        productRequest.setId(product.getId());

        return productRequest;
    }

    @Caching(
        put = { @CachePut(value = "products:item", key = "#productId") },
        evict = { @CacheEvict(value = "products:list", allEntries = true) }
    )
    public ProductDto updateProduct(Long productId, ProductDto productRequest) {
        var product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        var category = categoryRepository.findById(productRequest.getCategoryID())
                .orElseThrow(CategoryNotFoundException::new);

        productMapper.update(product, productRequest);
        product.setCategory(category);

        productRepository.save(product);

        productRequest.setId(product.getId());
        return productRequest;
    }


    @Caching(evict = {
        @CacheEvict(value = "products:item", key = "#productId"),
        @CacheEvict(value = "products:list", allEntries = true)
    })
    public void removeProduct(Long productId) {
        var product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        productRepository.delete(product);
    }

}
