package com.bfs.hibernateprojectdemo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bfs.hibernateprojectdemo.domain.Order;
import com.bfs.hibernateprojectdemo.dto.DataResponse;
import com.bfs.hibernateprojectdemo.dto.OrderRequest;
import com.bfs.hibernateprojectdemo.exception.ResourceNotFoundException;
import com.bfs.hibernateprojectdemo.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public DataResponse placeNewOrder(@RequestBody OrderRequest orderRequest) {
        Order order = orderService.createOrder(orderRequest);
        return DataResponse.builder()
                .success(true)
                .message("Order placed successfully")
                .data(order)
                .build();
    }

    @GetMapping("/all")
    public DataResponse getAllOrders() {
        List<Order> orders = orderService.findAllOrders();
        return DataResponse.builder()
                .success(true)
                .message("Orders retrieved successfully")
                .data(orders)
                .build();
    }
    @GetMapping("/user/{id}")
    public DataResponse getOrdersByUserId(@PathVariable int id) {
        List<Order> orders = orderService.findOrdersByUserId(id);
        return DataResponse.builder()
                .success(true)
                .message("User Orders retrieved successfully")
                .data(orders)
                .build();
    }

    @GetMapping("/{id}")
    public DataResponse getOrderDetail(@PathVariable int id) {
        Order order = orderService.findOrderById(id);
        return DataResponse.builder()
                .success(true)
                .message("Order details retrieved successfully")
                .data(order)
                .build();
    }

    @PatchMapping("/{id}/cancel")
    public DataResponse cancelOrder(@PathVariable int id) {
        Order order = orderService.cancelOrder(id);
        return DataResponse.builder()
                .success(true)
                .message("Order canceled successfully")
                .data(order)
                .build();
    }


}

