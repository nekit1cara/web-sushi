package com.example.web_sushi.Service.interfaces;


import com.example.web_sushi.Entity.Products;
import com.example.web_sushi.Enums.ProductCategory;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface ProductsService {

    ResponseEntity<?> getAllProducts(int page, int size);

    ResponseEntity<?> getProductById(Long productId);

    ResponseEntity<?> getProductByName(String productName);

    ResponseEntity<?> getProductsByCategory(int page, int size, ProductCategory category);

    ResponseEntity<?> createProduct(Products product);

    ResponseEntity<?> createProducts(List<Products> products);

    ResponseEntity<?> updateProductById(Long id, Products product);

    ResponseEntity<?> deleteProductById(Long id);



}
