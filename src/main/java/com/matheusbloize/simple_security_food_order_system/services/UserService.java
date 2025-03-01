package com.matheusbloize.simple_security_food_order_system.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.matheusbloize.simple_security_food_order_system.models.User;

public interface UserService {

    List<User> listAll();

    Optional<User> findById(UUID userId);

    User save(User user);

    void delete(User user);

}
