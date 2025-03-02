package com.matheusbloize.simple_security_food_order_system.controllers;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matheusbloize.simple_security_food_order_system.dtos.OrderDto;
import com.matheusbloize.simple_security_food_order_system.enums.OrderStatus;
import com.matheusbloize.simple_security_food_order_system.enums.RoleType;
import com.matheusbloize.simple_security_food_order_system.models.Order;
import com.matheusbloize.simple_security_food_order_system.models.OrderProduct;
import com.matheusbloize.simple_security_food_order_system.models.Product;
import com.matheusbloize.simple_security_food_order_system.models.User;
import com.matheusbloize.simple_security_food_order_system.services.OrderProductService;
import com.matheusbloize.simple_security_food_order_system.services.OrderService;
import com.matheusbloize.simple_security_food_order_system.services.ProductService;
import com.matheusbloize.simple_security_food_order_system.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private OrderService orderService;
    private ProductService productService;
    private UserService userService;
    private OrderProductService orderProductService;

    public OrderController(OrderService orderService, ProductService productService, UserService userService, OrderProductService orderProductService) {
        this.orderService = orderService;
        this.productService = productService;
        this.userService = userService;
        this.orderProductService = orderProductService;
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @GetMapping
    ResponseEntity<List<Order>> listAll() {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.listAll());
    }

    @PreAuthorize(value = "hasRole('ROLE_USER')")
    @PostMapping
    ResponseEntity<Object> save(@RequestBody @Valid OrderDto orderDto, Principal principal) {
        Optional<User> userOpt = userService.findById(orderDto.userId());
        if (!userOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found.");
        }

        Order order = new Order();
        order.setUser(userOpt.get());

        Optional<Product> productOpt = null;
        BigDecimal totalPrice = BigDecimal.ZERO;
        BigDecimal actualProductPrice = BigDecimal.ZERO;
        List<OrderProduct> orderProducts = new ArrayList<>();
        for (int i = 0; i < orderDto.items().size(); i++) {
            productOpt = productService.findById(orderDto.items().get(i).product_id());
            if (!productOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product Not Found.");
            }

            actualProductPrice = productOpt.get().getPrice().multiply(BigDecimal.valueOf(orderDto.items().get(i).quantity()));
            totalPrice = totalPrice.add(actualProductPrice);

            OrderProduct actualOrderProduct = new OrderProduct();
            actualOrderProduct.setProduct(productOpt.get());
            actualOrderProduct.setQuantity(orderDto.items().get(i).quantity());

            orderProducts.add(actualOrderProduct);
        }
        order.setTotalPrice(totalPrice);
        order.setStatus(orderDto.status() != null ? orderDto.status() : OrderStatus.CREATED);

        orderService.save(order);

        orderProducts.stream().forEach(orderProd -> {
            orderProd.setOrder(order);
            orderProductService.save(orderProd);
        });
        return findById(order.getId(), principal);
    }

    @PreAuthorize(value = "hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/{id}")
    ResponseEntity<Object> findById(@PathVariable(value = "id") UUID id, Principal principal) {
        Optional<Order> orderOpt = orderService.findById(id);
        if (!orderOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order Not Found.");
        }
        Optional<User> userOpt = userService.findByName(principal.getName());
        if (!userOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found.");
        }
        var isAdmin = userOpt.get().getAuthorities().stream().map(role -> role.getAuthority() == RoleType.ROLE_ADMIN.toString());
        if (!isAdmin.anyMatch(t -> t) && orderOpt.get().getUser().getName() != userOpt.get().getName()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("This Order Isn't Yours.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(orderOpt.get());
    }

    @PreAuthorize(value = "hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PutMapping("/{id}")
    ResponseEntity<Object> update(@PathVariable(value = "id") UUID id, @RequestBody @Valid OrderDto orderDto, Principal principal) {
        Optional<Order> orderOpt = orderService.findById(id);
        if (!orderOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order Not Found.");
        }
        Optional<User> userOpt = userService.findByName(principal.getName());
        if (!userOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found.");
        }
        var isAdmin = userOpt.get().getAuthorities().stream().map(role -> role.getAuthority() == RoleType.ROLE_ADMIN.toString());
        if (!isAdmin.anyMatch(t -> t) && orderOpt.get().getUser().getName() != userOpt.get().getName()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("This Order Isn't Yours.");
        }

        Order order = new Order();
        BeanUtils.copyProperties(orderOpt.get(), order);
        BeanUtils.copyProperties(orderDto, order);
        order.setId(orderOpt.get().getId());

        return ResponseEntity.status(HttpStatus.OK).body(orderService.save(order));
    }

    @PreAuthorize(value = "hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    ResponseEntity<Object> delete(@PathVariable(value = "id") UUID id, Principal principal) {
        Optional<Order> orderOpt = orderService.findById(id);
        if (!orderOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order Not Found.");
        }

        Optional<User> userOpt = userService.findByName(principal.getName());
        if (!userOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found.");
        }
        var isAdmin = userOpt.get().getAuthorities().stream().map(role -> role.getAuthority() == RoleType.ROLE_ADMIN.toString());
        if (!isAdmin.anyMatch(t -> t) && orderOpt.get().getUser().getName() != userOpt.get().getName()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("This Order Isn't Yours.");
        }

        List<OrderProduct> orderProducts = orderProductService.listAllWithOrderId(id);
        orderProducts.forEach(orderProd -> orderProductService.delete(orderProd));

        orderService.delete(orderOpt.get());
        return ResponseEntity.status(HttpStatus.OK).body("Order Deleted Successfully.");
    }

}
