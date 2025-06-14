package com.example.store.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.store.dtos.ProductDto;
import com.example.store.entities.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "category.id", target = "categoryID")
    ProductDto toDto(Product Product);

    Product toEntity(ProductDto productDto);

    @Mapping(target = "id", ignore = true)
    void update(@MappingTarget Product product, ProductDto productDto);

}
