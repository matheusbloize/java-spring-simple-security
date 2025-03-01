package com.matheusbloize.simple_security_food_order_system.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.matheusbloize.simple_security_food_order_system.models.OrderProduct;

public interface OrderProductService {

    List<OrderProduct> listAll();

    Optional<OrderProduct> findById(UUID orderProduOrderProductId);

    OrderProduct save(OrderProduct orderProduOrderProduct);

    void delete(OrderProduct orderProduOrderProduct);
}
