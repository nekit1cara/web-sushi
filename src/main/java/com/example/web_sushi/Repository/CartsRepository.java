package com.example.web_sushi.Repository;

import com.example.web_sushi.Entity.Carts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartsRepository extends JpaRepository<Carts,Long> {

   Optional<Carts> findBySessionId(String sessionId);

}
