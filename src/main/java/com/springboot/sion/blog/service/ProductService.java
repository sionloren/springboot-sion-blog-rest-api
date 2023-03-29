package com.springboot.sion.blog.service;

import com.springboot.sion.blog.dto.ProductDto;

import java.util.List;

public interface ProductService {
    List<ProductDto> searchProducts(String query);

    ProductDto createProduct(ProductDto productDto);
}
