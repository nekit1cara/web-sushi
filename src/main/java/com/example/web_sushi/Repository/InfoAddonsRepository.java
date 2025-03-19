package com.example.web_sushi.Repository;

import com.example.web_sushi.Entity.InfoAddons;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InfoAddonsRepository extends JpaRepository<InfoAddons, Long> {

    boolean existsByAddonName(String name);

}
