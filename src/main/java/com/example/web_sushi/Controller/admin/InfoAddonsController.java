package com.example.web_sushi.Controller.admin;

import com.example.web_sushi.Entity.InfoAddons;
import com.example.web_sushi.Service.interfaces.InfoAddonsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addon")
public class InfoAddonsController {

    private final InfoAddonsService infoAddonsService;

    @Autowired
    public InfoAddonsController(InfoAddonsService infoAddonsService) {
        this.infoAddonsService = infoAddonsService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllAddons(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        return infoAddonsService.getAllAddons(page, size);
    }

    @GetMapping("/{addonId}")
    public ResponseEntity<?> getAddonById(@PathVariable Long addonId) {
        return infoAddonsService.getAddonsById(addonId);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAddon(@RequestBody InfoAddons infoAddons) {
        return infoAddonsService.addAddon(infoAddons);
    }

    @PostMapping("/create-addons")
    public ResponseEntity<?> createAddons(@RequestBody List<InfoAddons> infoAddons) {
        return infoAddonsService.addAddons(infoAddons);
    }

    @PutMapping("/update/{addonId}")
    public ResponseEntity<?> updateAddon(@PathVariable Long addonId,
                                         @RequestBody InfoAddons infoAddons) {
        return infoAddonsService.updateAddonById(addonId, infoAddons);
    }

    @DeleteMapping("/delete/{addonId}")
    public ResponseEntity<?> deleteAddon(@PathVariable Long addonId) {
        return infoAddonsService.deleteAddonById(addonId);
    }

}
