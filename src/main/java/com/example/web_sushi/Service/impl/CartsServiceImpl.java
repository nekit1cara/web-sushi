package com.example.web_sushi.Service.impl;

import com.example.web_sushi.Entity.CartItems;
import com.example.web_sushi.Entity.Carts;
import com.example.web_sushi.Entity.InfoAddons;
import com.example.web_sushi.Entity.Products;
import com.example.web_sushi.GlobalException.Exceptions.CartExceptions;
import com.example.web_sushi.GlobalException.Exceptions.NotFoundException;
import com.example.web_sushi.Repository.CartItemsRepository;
import com.example.web_sushi.Repository.CartsRepository;
import com.example.web_sushi.Repository.InfoAddonsRepository;
import com.example.web_sushi.Repository.ProductsRepository;
import com.example.web_sushi.Service.interfaces.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartsServiceImpl implements CartService {

    private final ProductsRepository productsRepository;
    private final CartItemsRepository cartItemsRepository;
    private final CartsRepository cartsRepository;

    @Autowired
    public CartsServiceImpl(ProductsRepository productsRepository,
                            CartItemsRepository cartItemsRepository,
                            CartsRepository cartsRepository) {
        this.productsRepository = productsRepository;
        this.cartItemsRepository = cartItemsRepository;
        this.cartsRepository = cartsRepository;
    }

    @Override
    @Transactional
    public ResponseEntity<?> getItemsInCart(HttpSession session) {

        List<CartItems> productsInCart = getOrCreateCart(session).getCartItems();

            if (productsInCart.isEmpty()) {
                throw new NotFoundException("No products found in cart");
            }
        return ResponseEntity.status(HttpStatus.OK).body(productsInCart);

    }

    @Override
    @Transactional
    public ResponseEntity<?> addProductToCart(HttpSession session, CartItems cartItems) {

        //Создаем или же берем существующую корзину по Сессии
        Carts cart = getOrCreateCart(session);

            if (!cart.getSessionId().equals(session.getId())) {
                throw new CartExceptions("Session id mismatch");
            }

        //Устанавливаем сущность которая хранит продукты и их количество (cartItems) в корзину
        cartItems.setCarts(cart);

        //Вытаскиваем конкретный продукт по ид который был указан
        Products product = cartItems.getProducts();
        Long productId = product.getId();
        product = productsRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product " + productId + " not found"));

        //Если количество продуктов не было указанно точнее ровно 0 выбрасываем исключение
            if (product == null || cartItems.getQuantity() <= 0) {
                throw new CartExceptions("Quantity mismatch");
            }

        //Устанавливаем существующий продукт в нашу корзину cart -> cartItems(сюда)
        cartItems.setProducts(product);

        //Берем существующих список наполнителей продукта
        List<InfoAddons> addons = cartItems.getAddons();

            //Если же наши наполнители указаны , тогда к продукту из корзины мы присваеваем наполнитель
            if (addons != null) {
                cartItems.setAddons(addons);
            }

        cartItemsRepository.save(cartItems);
        cartsRepository.save(cart);

        return ResponseEntity.status(HttpStatus.OK).body(cartItems);
    }

    @Override
    @Transactional
    public ResponseEntity<?> removeProductFromCart(HttpSession session, Long productId) {

        Carts cart = getOrCreateCart(session);

        List<CartItems> cartItems = cart.getCartItems();

            for (CartItems cartItem : cartItems) {
                cartItem.setSessionId(cart.getSessionId());
                cartItemsRepository.save(cartItem);
            }

            CartItems productInCart = cartItemsRepository.findBySessionIdAndId(cart.getSessionId(), productId);
            cart.getCartItems().remove(productInCart);
            cartItemsRepository.delete(productInCart);
            cartsRepository.save(cart);
            return ResponseEntity.status(HttpStatus.OK).body("Product " + productId + " removed from cart");

    }

    @Override
    @Transactional
    public ResponseEntity<?> clearCart(HttpSession session) {

        Carts cart = getOrCreateCart(session);
        cartItemsRepository.deleteAll(cart.getCartItems());
            return ResponseEntity.status(HttpStatus.OK).body(cart); //"Cart was cleared."
    }




    private Carts getOrCreateCart(HttpSession session) {

        String sessionId = session.getId();

        return cartsRepository.findBySessionId(sessionId).orElseGet(() -> {
            Carts newCart = new Carts();
            newCart.setSessionId(sessionId);
            return cartsRepository.save(newCart);
        });
    }
}

