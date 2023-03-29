package com.springboot.sion.blog.service.impl;

import com.springboot.sion.blog.dto.ProductDto;
import com.springboot.sion.blog.model.Product;
import com.springboot.sion.blog.repository.ProductRepository;
import com.springboot.sion.blog.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private ModelMapper mapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ModelMapper mapper) {
        this.productRepository = productRepository;
        this.mapper = mapper;
    }

    @Override
    public List<ProductDto> searchProducts(String query) {
        List<Product> products = productRepository.searchProducts(query);

        List<ProductDto> productDtos = products.stream().map(product -> mapToDTO(product))
                .collect(Collectors.toList());

        return productDtos;
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = mapToEntity(productDto);
        Product newProduct = productRepository.save(product);

        ProductDto newProductDto = mapToDTO(newProduct);

        return newProductDto;
    }

    //Convert DTO to entity (json to pojo)
    private Product mapToEntity(ProductDto productDto) {
        Product product = mapper.map(productDto, Product.class);
        return product;
    }
    private ProductDto mapToDTO (Product pro) {
        ProductDto productDto = mapper.map(pro, ProductDto.class);
        return productDto;
    }
}
