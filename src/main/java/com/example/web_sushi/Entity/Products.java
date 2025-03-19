package com.example.web_sushi.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "info_id" , nullable = false)
    private ProductInfo info;

    @JsonIgnore
    @OneToMany(mappedBy = "products", cascade = CascadeType.ALL)
    private List<CartItems> cartItems;

    @JsonIgnore
    @OneToMany(mappedBy = "products", cascade = CascadeType.ALL)
    private List<OrderItems> orderItems;

}
