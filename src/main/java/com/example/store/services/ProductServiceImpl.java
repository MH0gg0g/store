package com.example.store.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.example.store.dtos.ProductDto;
import com.example.store.entities.Product;
import com.example.store.exceptions.CategoryNotFoundException;
import com.example.store.exceptions.ProductNotFoundException;
import com.example.store.mappers.ProductMapper;
import com.example.store.repositories.CategoryRepository;
import com.example.store.repositories.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    @Cacheable(value = "products:list", key = "#categoryId != null ? #categoryId : 'all'")
    public List<ProductDto> getAllProducts(Long categoryId) {
        List<Product> products;

        if (categoryId != null) {
            products = productRepository.findByCategoryId(categoryId);
        } else {
            products = productRepository.findAllWithCategory();
        }

        var dtos = products.stream().map(productMapper::toDto).toList();
        logger.debug("getAllProducts: returning {} products for categoryId={}", dtos.size(), categoryId);
        return dtos;
    }

    @Cacheable(value = "products:item", key = "#productId")
    public ProductDto getProductById(Long productId) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        logger.debug("getProductById: found id={}", productId);
        return productMapper.toDto(product);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = "products:list", allEntries = true)
    public ProductDto createProduct(ProductDto productRequest) {
        var product = productMapper.toEntity(productRequest);
        var category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(productRequest.getCategoryId()));

        product.setCategory(category);
        productRepository.save(product);
        productRequest.setId(product.getId());
        logger.info("createProduct: created product id={} name={}", product.getId(), product.getName());
        return productRequest;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Caching(put = { @CachePut(value = "products:item", key = "#productId") }, evict = {
            @CacheEvict(value = "products:list", allEntries = true) })
    public ProductDto updateProduct(Long productId, ProductDto productRequest) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        var category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(productRequest.getCategoryId()));

        productMapper.update(product, productRequest);
        product.setCategory(category);
        productRepository.save(product);
        productRequest.setId(product.getId());
        logger.info("updatProduct: updated product id={} name={}", product.getId(), product.getName());
        return productRequest;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Caching(evict = {
            @CacheEvict(value = "products:item", key = "#productId"),
            @CacheEvict(value = "products:list", allEntries = true)
    })
    public void removeProduct(Long productId) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        productRepository.delete(product);
        logger.info("removeProduct: deletd product id={} name={}", productId, product.getName());
    }

}
