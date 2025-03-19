package com.example.web_sushi.Repository;

import com.example.web_sushi.Entity.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemsRepository extends JpaRepository<CartItems, Long> {

    CartItems findBySessionIdAndId(String sessionId, Long id);

}
