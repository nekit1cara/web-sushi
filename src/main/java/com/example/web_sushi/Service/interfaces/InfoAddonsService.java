package com.example.web_sushi.Service.interfaces;

import com.example.web_sushi.Entity.InfoAddons;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface InfoAddonsService {

    ResponseEntity<?> getAllAddons(int page, int size);

    ResponseEntity<?> getAddonsById(Long addonId);

    ResponseEntity<?> addAddon(InfoAddons addon);

    ResponseEntity<?> addAddons(List<InfoAddons> addons);

    ResponseEntity<?> updateAddonById(Long addonId, InfoAddons addon);

    ResponseEntity<?> deleteAddonById(Long addonId);

}
