package com.matheusbloize.simple_security_food_order_system.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.matheusbloize.simple_security_food_order_system.models.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

}
