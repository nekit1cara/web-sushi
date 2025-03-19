package com.example.web_sushi.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Carts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sessionId;

    private LocalDateTime createdCartDate;

    @JsonManagedReference
    @OneToMany(mappedBy = "carts", cascade = CascadeType.ALL)
    private List<CartItems> cartItems;



    @PrePersist
    protected void onCreate() {
        this.createdCartDate = LocalDateTime.now();
    }

}
