package com.example.web_sushi.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class CartItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sessionId;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "carts_id")
    private Carts carts;

    private int quantity;

    @ManyToOne
    @JoinColumn(name = "products_id")
    private Products products;

    @ManyToMany
    @JoinTable(
        name = "addons",
        joinColumns = @JoinColumn(name = "cartItems_id"),
        inverseJoinColumns = @JoinColumn(name = "addons_id")
    )
    private List<InfoAddons> addons;




}
