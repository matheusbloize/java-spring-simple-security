package com.matheusbloize.simple_security_food_order_system.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.matheusbloize.simple_security_food_order_system.models.Product;
import com.matheusbloize.simple_security_food_order_system.repositories.ProductRepository;
import com.matheusbloize.simple_security_food_order_system.services.ProductService;

import jakarta.transaction.Transactional;

@Service
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> listAll() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> findById(UUID productId) {
        return productRepository.findById(productId);
    }

    @Transactional
    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    @Override
    public void delete(Product product) {
        productRepository.delete(product);
    }

}
