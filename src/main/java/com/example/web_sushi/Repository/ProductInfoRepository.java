package com.example.web_sushi.Repository;

import com.example.web_sushi.Entity.ProductInfo;
import com.example.web_sushi.Enums.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductInfoRepository extends JpaRepository<ProductInfo,Long> {

    ProductInfo findByProductName(String productName);

    Page<ProductInfo> findByProductCategory(ProductCategory productCategory, Pageable pageable);

    boolean existsByProductName(String productName);

}
