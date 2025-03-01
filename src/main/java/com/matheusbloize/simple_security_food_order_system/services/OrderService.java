package com.matheusbloize.simple_security_food_order_system.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.matheusbloize.simple_security_food_order_system.models.Order;

public interface OrderService {

    List<Order> listAll();

    Optional<Order> findById(UUID orderId);

    Order save(Order order);

    void delete(Order order);

}
