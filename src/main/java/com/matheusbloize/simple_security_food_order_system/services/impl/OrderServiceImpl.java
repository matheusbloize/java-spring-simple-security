package com.matheusbloize.simple_security_food_order_system.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.matheusbloize.simple_security_food_order_system.models.Order;
import com.matheusbloize.simple_security_food_order_system.repositories.OrderRepository;
import com.matheusbloize.simple_security_food_order_system.services.OrderService;

import jakarta.transaction.Transactional;

@Service
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> listAll() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> findById(UUID orderId) {
        return orderRepository.findById(orderId);
    }

    @Transactional
    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Transactional
    @Override
    public void delete(Order order) {
        orderRepository.delete(order);
    }

}
