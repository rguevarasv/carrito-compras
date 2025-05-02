package com.shopping.productservice.service.impl;

import com.shopping.productservice.dto.ProductDto;
import com.shopping.productservice.exception.ProductNotFoundException;
import com.shopping.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final RestTemplate restTemplate;

    @Value("${fakestore.api.base-url}")
    private String apiBaseUrl;

    @Override
    public List<ProductDto> getAllProducts() {
        String url = apiBaseUrl + "/products";

        ResponseEntity<List<ProductDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ProductDto>>() {}
        );

        return response.getBody();
    }

    @Override
    public ProductDto getProductById(Long id) {
        String url = apiBaseUrl + "/products/" + id;

        try {
            return restTemplate.getForObject(url, ProductDto.class);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ProductNotFoundException(id);
        }
    }

    @Override
    public List<ProductDto> getProductsByCategory(String category) {
        String url = apiBaseUrl + "/products/category/" + category;

        try {
            ResponseEntity<List<ProductDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<ProductDto>>() {}
            );

            return response.getBody();
        } catch (HttpClientErrorException ex) {
            throw new ProductNotFoundException("Category not found: " + category);
        }
    }

    @Override
    public List<String> getAllCategories() {
        String url = apiBaseUrl + "/products/categories";

        return Arrays.asList(restTemplate.getForObject(url, String[].class));
    }
}