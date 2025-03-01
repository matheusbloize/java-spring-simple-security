package com.matheusbloize.simple_security_food_order_system.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.matheusbloize.simple_security_food_order_system.models.OrderProduct;
import com.matheusbloize.simple_security_food_order_system.repositories.OrderProductRepository;
import com.matheusbloize.simple_security_food_order_system.services.OrderProductService;

import jakarta.transaction.Transactional;

@Service
public class OrderProductServiceImpl implements OrderProductService {
    private OrderProductRepository orderProductRepository;

    public OrderProductServiceImpl(OrderProductRepository orderProductRepository) {
        this.orderProductRepository = orderProductRepository;
    }

    @Override
    public List<OrderProduct> listAll() {
        return orderProductRepository.findAll();
    }

    @Override
    public List<OrderProduct> listAllWithOrderId(UUID orderId) {
        return orderProductRepository.findByOrderId(orderId);
    }

    @Override
    public Optional<OrderProduct> findById(UUID orderProductId) {
        return orderProductRepository.findById(orderProductId);
    }

    @Transactional
    @Override
    public OrderProduct save(OrderProduct orderProduct) {
        return orderProductRepository.save(orderProduct);
    }

    @Transactional
    @Override
    public void delete(OrderProduct orderProduct) {
        orderProductRepository.delete(orderProduct);
    }
}
