package com.example.web_sushi.Service.interfaces;

import com.example.web_sushi.Entity.CartItems;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;

public interface CartService {

    ResponseEntity<?> getItemsInCart(HttpSession session);

    ResponseEntity<?> addProductToCart(HttpSession session, CartItems cartItems);

    ResponseEntity<?> removeProductFromCart(HttpSession session, Long productId);

    ResponseEntity<?> clearCart(HttpSession session);



}
