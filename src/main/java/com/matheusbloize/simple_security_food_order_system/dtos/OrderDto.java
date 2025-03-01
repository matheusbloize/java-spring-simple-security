package com.matheusbloize.simple_security_food_order_system.dtos;

import java.util.List;
import java.util.UUID;

import com.matheusbloize.simple_security_food_order_system.enums.OrderStatus;

import jakarta.validation.constraints.NotNull;

public record OrderDto(@NotNull List<OrderProductDto> items, @NotNull UUID userId, OrderStatus status) {

    public OrderDto(@NotNull List<OrderProductDto> items, @NotNull UUID userId, OrderStatus status) {
        this.items = items;
        this.userId = userId;
        this.status = status != null ? status : OrderStatus.CREATED;
    }
}