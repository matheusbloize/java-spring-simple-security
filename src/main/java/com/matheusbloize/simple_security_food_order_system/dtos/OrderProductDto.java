package com.matheusbloize.simple_security_food_order_system.dtos;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderProductDto(@NotNull UUID product_id, @NotNull @Min(value = 1) int quantity) {

}
