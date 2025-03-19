package com.example.web_sushi.Enums;

import lombok.Getter;

@Getter
public enum OrderStatus {

    NEW("Новый"),
    PROCESSING("В обработке"),
    COOKING("Готовится"),
    READY_FOR_PICKUP("Готов к выдаче"),
    OUT_FOR_DELIVERY("В пути"),
    DELIVERED("Доставлен"),
    CANCELLED("Отменен");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }


}
