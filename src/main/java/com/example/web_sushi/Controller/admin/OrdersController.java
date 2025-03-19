package com.example.web_sushi.Controller.admin;

import com.example.web_sushi.Entity.Orders;
import com.example.web_sushi.Enums.OrderStatus;
import com.example.web_sushi.Service.interfaces.OrdersService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    private final OrdersService ordersService;

    @Autowired
    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @GetMapping
    public ResponseEntity<?> getAllOrders(HttpSession session) {
        return ordersService.getOrdersBySession(session);
    }

    @GetMapping("/track")
    public ResponseEntity<?> getOrderByTrackingNumber(HttpSession session,
                                                      @RequestParam(required = false) String trackingNumber) {
        return ordersService.getOrderBySessionAndTrackNumber(session, trackingNumber);
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> createOrder(HttpSession session, @RequestBody Orders order) {
        return ordersService.createOrder(session, order);
    }

    @PutMapping("/update/{orderId}")
    public ResponseEntity<?> updateOrderById(HttpSession session,
                                             @PathVariable Long orderId,
                                             @RequestBody Orders order) {
        return ordersService.updateOrderById(session,orderId, order);
    }

    @PutMapping("/update/status/{orderId}")
    public ResponseEntity<?> updateOrderStatus(HttpSession session,
                                               @PathVariable Long orderId,
                                               @RequestBody OrderStatus status) {
        return ordersService.updateOrderStatus(session, orderId, status);
    }

    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<?> deleteOrderById(HttpSession session, @PathVariable Long orderId) {
        return ordersService.deleteOrderById(session, orderId);
    }


}
