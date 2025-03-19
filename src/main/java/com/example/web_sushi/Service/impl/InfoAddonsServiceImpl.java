package com.example.web_sushi.Service.impl;

import com.example.web_sushi.Entity.InfoAddons;
import com.example.web_sushi.GlobalException.Exceptions.AlreadyExistException;
import com.example.web_sushi.GlobalException.Exceptions.NotFoundException;
import com.example.web_sushi.Repository.InfoAddonsRepository;
import com.example.web_sushi.Repository.ProductsRepository;
import com.example.web_sushi.Service.interfaces.InfoAddonsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InfoAddonsServiceImpl implements InfoAddonsService {

    private final InfoAddonsRepository infoAddonsRepository;
    private final ProductsRepository productsRepository;

    @Autowired
    public InfoAddonsServiceImpl(InfoAddonsRepository infoAddonsRepository, ProductsRepository productsRepository) {
        this.infoAddonsRepository = infoAddonsRepository;
        this.productsRepository = productsRepository;
    }

    @Override
    public ResponseEntity<?> getAllAddons(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<InfoAddons> infoAddonsPage = infoAddonsRepository.findAll(pageable);
            return ResponseEntity.status(HttpStatus.OK).body(infoAddonsPage);
    }

    @Override
    public ResponseEntity<?> getAddonsById(Long addonId) {

        Optional<InfoAddons> infoAddons = infoAddonsRepository.findById(addonId);

            if (infoAddons.isEmpty()) {
                throw new NotFoundException("Наполнитель : " + addonId + " не найден.");
            }

        InfoAddons foundedInfoAddon = infoAddons.get();
            return ResponseEntity.status(HttpStatus.OK).body(foundedInfoAddon);
    }

    @Override
    public ResponseEntity<?> addAddon(InfoAddons addon) {

        if (infoAddonsRepository.existsByAddonName(addon.getAddonName())) {
            throw new AlreadyExistException("Наполнитель : " + addon.getAddonName() + " уже существует.");
        }

        infoAddonsRepository.save(addon);
            return ResponseEntity.status(HttpStatus.OK).body(addon);
    }

    @Override
    public ResponseEntity<?> addAddons(List<InfoAddons> addons) {

            for (InfoAddons addon : addons) {
                if (infoAddonsRepository.existsByAddonName(addon.getAddonName())) {
                    throw new AlreadyExistException("Наполнитель : " + addon.getAddonName() + " уже существует.");
                }

                infoAddonsRepository.save(addon);
            }

        return ResponseEntity.status(HttpStatus.OK).body(addons);
    }

    @Override
    public ResponseEntity<?> updateAddonById(Long addonId, InfoAddons addon) {

        Optional<InfoAddons> infoAddons = infoAddonsRepository.findById(addonId);

            if (infoAddons.isEmpty()) {
                throw new NotFoundException("Наполнитель : " + addonId + " не найден.");
            }

        InfoAddons addonToUpdate = infoAddons.get();

            if (addon.getAddonName() != null) {
                addonToUpdate.setAddonName(addon.getAddonName());
            }
            if (addon.getAddonPrice() != 0) {
                addonToUpdate.setAddonPrice(addon.getAddonPrice());
            }

        infoAddonsRepository.save(addonToUpdate);
            return ResponseEntity.status(HttpStatus.OK).body("Наполнитель успешно изменен.");

    }


    @Override
    public ResponseEntity<?> deleteAddonById(Long addonId) {

        Optional<InfoAddons> infoAddons = infoAddonsRepository.findById(addonId);

            if (infoAddons.isEmpty()) {
                throw new NotFoundException("Наполнитель : " + addonId + " не найден.");
            }

        InfoAddons addonToDelete = infoAddons.get();
            infoAddonsRepository.delete(addonToDelete);
        return ResponseEntity.status(HttpStatus.OK).body("Наполнитель : " + addonId + " успешно удален.");
    }
}
