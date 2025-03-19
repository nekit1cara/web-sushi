package com.example.web_sushi.Service.interfaces;

import com.example.web_sushi.Entity.Orders;
import com.example.web_sushi.Enums.OrderStatus;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;

public interface OrdersService {

    ResponseEntity<?> getOrderBySessionAndTrackNumber(HttpSession session, String trackNumber);

    ResponseEntity<?> getOrdersBySession(HttpSession session);

    ResponseEntity<?> createOrder(HttpSession session, Orders order);

    ResponseEntity<?> updateOrderById(HttpSession session, Long orderId, Orders order);

    ResponseEntity<?> updateOrderStatus(HttpSession session, Long orderId, OrderStatus orderStatus);

    ResponseEntity<?> deleteOrderById(HttpSession session, Long orderId);


}
