package com.example.store.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.store.dtos.ProductDto;
import com.example.store.services.ProductService;

import lombok.AllArgsConstructor;
import lombok.var;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public List<ProductDto> getAllProducts(@RequestParam(name = "categoryId", required = false) Long categoryId) {
        return productService.getAllProducts(categoryId);

    }

    @GetMapping("/{productId}")
    public ProductDto getProductById(@PathVariable Long productId) {
        return productService.getProductById(productId);
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productRequest,
            UriComponentsBuilder uriBuilder) {

        var ProductDto = productService.createProduct(productRequest);
        var uri = uriBuilder.path("/products/{id}").buildAndExpand(ProductDto.getId()).toUri();

        return ResponseEntity.created(uri).body(ProductDto);
    }

    @PutMapping("/{productId}")
    public ProductDto updateProduct(@PathVariable Long productId,
            @RequestBody ProductDto productRequest) {
        return productService.updateProduct(productId, productRequest);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.removeProduct(productId);
        return ResponseEntity.noContent().build();
    }

}
