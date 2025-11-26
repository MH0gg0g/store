package com.example.store.services;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.example.store.aop.Loggable;
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

        private final ProductRepository productRepository;
        private final ProductMapper productMapper;
        private final CategoryRepository categoryRepository;

        @Loggable
        @Cacheable(value = "products:list", key = "#categoryId != null ? #categoryId : 'all'")
        public List<ProductDto> getAllProducts(Long categoryId) {
                List<Product> products;

                products = (categoryId != null) ? productRepository.findByCategoryId(categoryId)
                                : productRepository.findAllWithCategory();

                var dtos = products.stream().map(productMapper::toDto).toList();

                return dtos;
        }

        // @Loggable
        @Cacheable(value = "products:item", key = "#productId")
        public ProductDto getProductById(Long productId) {
                var product = productRepository.findById(productId)
                                .orElseThrow(() -> new ProductNotFoundException(productId));

                return productMapper.toDto(product);
        }

        // @Loggable
        @PreAuthorize("hasRole('ADMIN')")
        @CacheEvict(value = "products:list", allEntries = true)
        public ProductDto createProduct(ProductDto request) {
                var product = productMapper.toEntity(request);
                var category = categoryRepository.findById(request.getCategoryId())
                                .orElseThrow(() -> new CategoryNotFoundException(request.getCategoryId()));

                product.setCategory(category);
                productRepository.save(product);
                request.setId(product.getId());

                return request;
        }

        @Loggable
        @PreAuthorize("hasRole('ADMIN')")
        @Caching(put = { @CachePut(value = "products:item", key = "#productId") }, evict = {
                        @CacheEvict(value = "products:list", allEntries = true) })
        public ProductDto updateProduct(Long productId, ProductDto request) {
                var product = productRepository.findById(productId)
                                .orElseThrow(() -> new ProductNotFoundException(productId));

                var category = categoryRepository.findById(request.getCategoryId())
                                .orElseThrow(() -> new CategoryNotFoundException(request.getCategoryId()));

                productMapper.update(product, request);
                product.setCategory(category);
                productRepository.save(product);
                request.setId(product.getId());

                return request;
        }

        @Loggable
        @PreAuthorize("hasRole('ADMIN')")
        @Caching(evict = {
                        @CacheEvict(value = "products:item", key = "#productId"),
                        @CacheEvict(value = "products:list", allEntries = true)
        })
        public void removeProduct(Long productId) {
                var product = productRepository.findById(productId)
                                .orElseThrow(() -> new ProductNotFoundException(productId));

                productRepository.delete(product);

        }

}
