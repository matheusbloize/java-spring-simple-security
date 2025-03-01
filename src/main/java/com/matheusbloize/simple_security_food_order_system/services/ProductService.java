package com.matheusbloize.simple_security_food_order_system.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.matheusbloize.simple_security_food_order_system.models.Product;

public interface ProductService {

    List<Product> listAll();

    Optional<Product> findById(UUID productId);

    Product save(Product product);

    void delete(Product product);

}
