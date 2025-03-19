package com.example.web_sushi.Service.impl;

import com.example.web_sushi.Entity.*;
import com.example.web_sushi.Enums.OrderStatus;
import com.example.web_sushi.Enums.OrderType;
import com.example.web_sushi.Enums.PaymentMethod;
import com.example.web_sushi.GlobalException.Exceptions.NotFoundException;
import com.example.web_sushi.GlobalException.Exceptions.PaymentMethodException;
import com.example.web_sushi.Repository.*;
import com.example.web_sushi.Service.interfaces.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.servlet.http.HttpSession;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrdersService {

    private final OrdersRepository ordersRepository;
    private final ClientsRepository clientsRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final CartsRepository cartsRepository;
    private final CartItemsRepository cartItemsRepository;

    @Autowired
    public OrderServiceImpl(OrdersRepository ordersRepository,
                            ClientsRepository clientsRepository,
                            OrderItemsRepository orderItemsRepository,
                            CartsRepository cartsRepository,
                            CartItemsRepository cartItemsRepository) {
        this.ordersRepository = ordersRepository;
        this.clientsRepository = clientsRepository;
        this.orderItemsRepository = orderItemsRepository;
        this.cartsRepository = cartsRepository;
        this.cartItemsRepository = cartItemsRepository;
    }


    @Override
    public ResponseEntity<?> getOrderBySessionAndTrackNumber(HttpSession session, String trackNumber) {
        // Получаем заказ по номеру отслеживания и сессии
        Orders existingOrder = getOrder(session, trackNumber);

            // Если заказ не найден, выбрасываем исключение
            if (existingOrder == null) {
                throw new NotFoundException("Order with track number : " + trackNumber + " not found");
            }

        // Возвращаем найденный заказ с успешным статусом
        return ResponseEntity.status(HttpStatus.OK).body(existingOrder);
    }

    @Override
    public ResponseEntity<?> getOrdersBySession(HttpSession session) {
        // Получаем список всех заказов по сессии
        List<Orders> existingOrders = getOrders(session);

            // Если заказы не найдены, выбрасываем исключение
            if (existingOrders.isEmpty()) {
                throw new NotFoundException("No orders found");
            }

        // Возвращаем список заказов с успешным статусом
        return ResponseEntity.status(HttpStatus.OK).body(existingOrders);
    }

    @Override
    @Transactional
    public ResponseEntity<?> createOrder(HttpSession session, Orders order) {
        // Получаем или создаем корзину для текущей сессии
        Carts cart = getOrCreateCart(session);

        // Получаем элементы корзины
        List<CartItems> cartItems = cart.getCartItems();

        // Устанавливаем sessionId в заказ
        order.setSessionId(cart.getSessionId());

            // Если корзина пуста, выбрасываем исключение
            if (cartItems.isEmpty()) {
                throw new NotFoundException("No cart items found");
            }

        // Генерируем уникальный номер отслеживания для заказа
        order.setOrderTrackNumber(generateOrderTrackNumber());

        // Создаем нового клиента на основе данных из заказа
        Clients client =  new Clients();
        OrderType orderType = order.getOrderType();

        // В зависимости от типа заказа (самовывоз или доставка), заполняем данные о клиенте
        switch (orderType) {
            case PICKUP -> {
                order.setPaymentMethod(PaymentMethod.PICKUP);
                client.setClientFullName(order.getClients().getClientFullName());
                client.setClientPhoneNumber(order.getClients().getClientPhoneNumber());
                client.setDeliveryAddress(null);
            }
            case DELIVERY ->{
                    // Если метод оплаты PICKUP выбран для доставки, выбрасываем исключение
                    if (order.getPaymentMethod() == PaymentMethod.PICKUP) {
                        throw new PaymentMethodException("Метод оплаты на месте при доставки не работает");
                    }

                client.setClientFullName(order.getClients().getClientFullName());
                client.setClientPhoneNumber(order.getClients().getClientPhoneNumber());
                client.setDeliveryAddress(order.getClients().getDeliveryAddress());
            }
        }

        // Сохраняем клиента в базу данных
        clientsRepository.save(client);

        // Создаем список элементов для заказа, основываясь на данных из корзины
        List<OrderItems> orderItems = new ArrayList<>();
            for (CartItems cartItem : cartItems) {
                OrderItems orderItem = new OrderItems();
                orderItem.setOrders(order);
                orderItem.setProducts(cartItem.getProducts());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setPrice(cartItem.getProducts().getInfo().getProductPrice() * cartItem.getQuantity());
                orderItems.add(orderItem);
            }

            // Если заказ содержит комментарий, добавляем его
            if (order.getComment() != null) {
                order.setComment(order.getComment());
            }

        // Устанавливаем клиента и элементы заказа в сам заказ
        order.setClients(client);
        order.setOrderItems(orderItems);

        // Рассчитываем общую стоимость заказа
        order.setOrderTotalPrice(calculateOrderTotalPrice(cart)); //добавь к цене допинги

        // Сохраняем заказ и элементы заказа в базе данных
        ordersRepository.save(order);
        orderItemsRepository.saveAll(orderItems);

        // Удаляем элементы из корзины
        cartItemsRepository.deleteAll(cartItems);

        // Возвращаем успешно созданный заказ
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateOrderById(HttpSession session, Long orderId, Orders order) {
        // Получаем заказ по его ID
        Optional<Orders> existingOrder = getOrderById(session, orderId);

            // Если заказ не найден, выбрасываем исключение
            if (existingOrder.isEmpty()) {
                throw new NotFoundException("Order with id : " + orderId + " not found");
            }

        // Обновляем заказ новыми значениями
        Orders orderToUpdate = updateOrderNewValues(order, existingOrder);

        // Возвращаем обновленный заказ
        return ResponseEntity.status(HttpStatus.OK).body(orderToUpdate);
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateOrderStatus(HttpSession session, Long orderId, OrderStatus orderStatus) {
        // Получаем заказ по его ID
        Optional<Orders> existingOrder = getOrderById(session, orderId);

            // Если заказ не найден, выбрасываем исключение
            if (existingOrder.isEmpty()) {
                throw new NotFoundException("Order with id : " + orderId + " not found");
            }

        // Обновляем статус заказа
        Orders orderToUpdateStatus = existingOrder.get();
        orderToUpdateStatus.setOrderStatus(orderStatus);

        // Сохраняем измененный заказ
        ordersRepository.save(orderToUpdateStatus);

        // Возвращаем обновленный заказ
        return ResponseEntity.status(HttpStatus.OK).body(orderToUpdateStatus);
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteOrderById(HttpSession session, Long orderId) {
        // Получаем заказ по его ID
        Optional<Orders> existingOrder = getOrderById(session, orderId);

            // Если заказ не найден, выбрасываем исключение
            if (existingOrder.isEmpty()) {
                throw new NotFoundException("Order with id : " + orderId + " not found");
            }

        // Удаляем заказ и связанные с ним элементы
        Orders orderToDelete = existingOrder.get();
            List<OrderItems> orderItems = orderToDelete.getOrderItems();
            orderToDelete.getOrderItems().clear();
            orderItemsRepository.deleteAll(orderItems);
            orderToDelete.setClients(null);
            ordersRepository.delete(orderToDelete);

        // Возвращаем сообщение об успешном удалении
        return ResponseEntity.status(HttpStatus.OK).body("Order with ID " + orderId + " has been deleted.");
    }

    // Приватные методы для работы с заказами и корзинами

    private Optional<Orders> getOrderById(HttpSession session, Long orderId) {
        String sessionId = session.getId();
        return ordersRepository.findBySessionIdAndId(sessionId, orderId);
    }

    private Orders getOrder(HttpSession session, String trackNumber) {
        String sessionId = session.getId();
        return ordersRepository.findBySessionIdAndOrderTrackNumber(sessionId, trackNumber);
    }

    private List<Orders> getOrders(HttpSession session) {
        String sessionId = session.getId();
        return ordersRepository.findBySessionIdAndOrderStatusNot(sessionId, OrderStatus.CANCELLED);
    }

    private Carts getOrCreateCart(HttpSession session) {
        String sessionId = session.getId();
        return cartsRepository.findBySessionId(sessionId).orElseGet(() -> {
            Carts newCart = new Carts();
            newCart.setSessionId(sessionId);
            return cartsRepository.save(newCart);
        });
    }

    private String generateOrderTrackNumber() {
        SecureRandom random = new SecureRandom();
        long number = Math.abs(random.nextLong() % 1_000_000_0000L); // Генерируем 10-значное число
        return String.format("MD%010dCH", number); // Форматируем строку с ведущими нулями
    }

    private int calculateOrderTotalPrice(Carts cart) {
        return cart.getCartItems().stream()
                .mapToInt(cartItems -> cartItems.getQuantity() *
                        cartItems.getProducts().getInfo().getProductPrice())
                .sum();
    }

    private Orders updateOrderNewValues(Orders order, Optional<Orders> existingOrder) {

        Orders orderToUpdate = existingOrder.get();

            // Обновляем различные поля заказа, если они не равны null
            if (order.getOrderTrackNumber() != null) {
                orderToUpdate.setOrderTrackNumber(generateOrderTrackNumber());
            }
            if (order.getDateCreated() != null) {
                orderToUpdate.setDateCreated(order.getDateCreated());
            }
            if (order.getOrderStatus() != null) {
                orderToUpdate.setOrderStatus(order.getOrderStatus());
            }
            if (order.getPaymentMethod() != null) {
                orderToUpdate.setPaymentMethod(order.getPaymentMethod());
            }
            if (order.getOrderType() != null) {
                orderToUpdate.setOrderType(order.getOrderType());
            }
            if (order.getOrderTotalPrice() != 0) {
                orderToUpdate.setOrderTotalPrice(order.getOrderTotalPrice());
            }
            if (order.getComment() != null) {
                orderToUpdate.setComment(order.getComment());
            }
            if (order.getEstimatedDeliveryTime() != null) {
                orderToUpdate.setEstimatedDeliveryTime(order.getEstimatedDeliveryTime());
            }
            if (order.getClients() != null) {
                orderToUpdate.setClients(order.getClients());
                clientsRepository.save(order.getClients());
            }
            if (order.getOrderItems() != null) {
                orderToUpdate.setOrderItems(order.getOrderItems());
                orderItemsRepository.saveAll(order.getOrderItems());
            }

        // Сохраняем обновленный заказ
        ordersRepository.save(orderToUpdate);
        return orderToUpdate;
    }

}