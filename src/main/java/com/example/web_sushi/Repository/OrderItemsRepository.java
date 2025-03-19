package com.example.web_sushi.Repository;

import com.example.web_sushi.Entity.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsRepository extends JpaRepository<OrderItems, Long> {
}
