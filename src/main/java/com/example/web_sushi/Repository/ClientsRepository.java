package com.example.web_sushi.Repository;

import com.example.web_sushi.Entity.Clients;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientsRepository extends JpaRepository<Clients, Long> {

    boolean existsByClientPhoneNumber(String clientPhoneNumber);

}
