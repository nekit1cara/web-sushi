package com.example.web_sushi.Controller.admin;

import com.example.web_sushi.Entity.CartItems;
import com.example.web_sushi.Service.interfaces.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartsController {

    private final CartService cartService;

    @Autowired
    public CartsController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<?> getProductsInCart(HttpSession session) {
        return cartService.getItemsInCart(session);
    }

    @PostMapping("/add-to-cart")
    public ResponseEntity<?> addProductToCart(HttpSession session , @RequestBody CartItems cartItems) {
        return cartService.addProductToCart(session, cartItems);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeProductFromCart(HttpSession session , @RequestParam Long itemId) {
        return cartService.removeProductFromCart(session, itemId);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(HttpSession session) {
        return cartService.clearCart(session);
    }


}
