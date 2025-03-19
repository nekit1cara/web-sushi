package com.example.web_sushi.Repository;

import com.example.web_sushi.Entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepository extends JpaRepository<Products,Long> {
}
