package com.matheusbloize.simple_security_food_order_system.dtos;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record OrderDto(@NotNull List<OrderProductDto> items, @NotNull UUID userId) {

}
