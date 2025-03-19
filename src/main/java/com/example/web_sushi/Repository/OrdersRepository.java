package com.example.web_sushi.Repository;

import com.example.web_sushi.Entity.Orders;
import com.example.web_sushi.Enums.OrderStatus;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    Optional<Orders> findBySessionIdAndId(String sessionId, Long id);

    Orders findBySessionIdAndOrderTrackNumber(String sessionId, String trackNumber);

    List<Orders> findBySessionIdAndOrderStatusNot(String sessionId, OrderStatus status);


}
