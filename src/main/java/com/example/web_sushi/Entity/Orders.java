package com.example.web_sushi.Entity;

import com.example.web_sushi.Enums.OrderStatus;
import com.example.web_sushi.Enums.OrderType;
import com.example.web_sushi.Enums.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sessionId;

    private String orderTrackNumber;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreated;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private OrderType orderType;


    private int orderTotalPrice;

    private String comment;

    private LocalDateTime estimatedDeliveryTime; // Примерное время доставки


    @ManyToOne
    @JoinColumn(name = "clients_id", nullable = false)
    private Clients clients;

    @JsonManagedReference
    @OneToMany(mappedBy = "orders", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderItems> orderItems;


    @PrePersist
    protected void onCreate() {
        this.orderStatus = OrderStatus.NEW;
        this.dateCreated = LocalDateTime.now();
        this.estimatedDeliveryTime = LocalDateTime.now().plusMinutes(60);
    }


}
