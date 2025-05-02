package com.shopping.productservice.service;

import com.shopping.productservice.dto.ProductDto;

import java.util.List;

public interface ProductService {
    List<ProductDto> getAllProducts();
    ProductDto getProductById(Long id);
    List<ProductDto> getProductsByCategory(String category);
    List<String> getAllCategories();
}