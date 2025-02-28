package com.matheusbloize.simple_security_food_order_system.dtos;

import java.util.List;
import java.util.UUID;

import com.matheusbloize.simple_security_food_order_system.models.OrderProduct;

import jakarta.validation.constraints.NotNull;

public record OrderDto(@NotNull List<OrderProduct> items, @NotNull UUID userId) {

}
