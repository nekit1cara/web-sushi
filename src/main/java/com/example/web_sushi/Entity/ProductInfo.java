package com.example.web_sushi.Entity;

import com.example.web_sushi.Enums.ProductCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProductInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ProductCategory productCategory;

    private String productName;
    private String productWeight;
    private String productDescription;
    private int productPrice;

    @JsonIgnore
    @OneToOne(mappedBy = "info", cascade = CascadeType.ALL)
    private Products products;


}
