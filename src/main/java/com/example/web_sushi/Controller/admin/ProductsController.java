package com.example.web_sushi.Controller.admin;

import com.example.web_sushi.Entity.Products;
import com.example.web_sushi.Enums.ProductCategory;
import com.example.web_sushi.Service.interfaces.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class ProductsController {

    private final ProductsService productsService;

    @Autowired
    public ProductsController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @GetMapping
    public ResponseEntity<?> getAllProducts(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        return productsService.getAllProducts(page, size);
    }

    @GetMapping("product/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable Long productId) {
        return productsService.getProductById(productId);
    }

    @GetMapping("/search")
    public ResponseEntity<?> getProductByName(@RequestParam String name) {
        return productsService.getProductByName(name);
    }

    @GetMapping("/catalog/")
    public ResponseEntity<?> getProductByCategory(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  @RequestParam ProductCategory category) {
        return productsService.getProductsByCategory(page, size, category);
    }

    @PostMapping("/product/create")
    public ResponseEntity<?> createProduct(@RequestBody Products product) {
        return productsService.createProduct(product);
    }

    @PostMapping("/product/create-products")
    public ResponseEntity<?> createProducts(@RequestBody List<Products> products) {
        return productsService.createProducts(products);
    }

    @PutMapping("/product/update/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable Long productId,
                                           @RequestBody Products product) {
        return productsService.updateProductById(productId, product);
    }

    @DeleteMapping("/product/delete/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        return productsService.deleteProductById(productId);
    }

}
