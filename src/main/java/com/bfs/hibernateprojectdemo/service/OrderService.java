package com.bfs.hibernateprojectdemo.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bfs.hibernateprojectdemo.dao.OrderDao;
import com.bfs.hibernateprojectdemo.dao.ProductDao;
import com.bfs.hibernateprojectdemo.dao.UserDao;
import com.bfs.hibernateprojectdemo.domain.Order;
import com.bfs.hibernateprojectdemo.domain.Order.Status;
import com.bfs.hibernateprojectdemo.domain.OrderItem;
import com.bfs.hibernateprojectdemo.domain.Product;
import com.bfs.hibernateprojectdemo.domain.User;
import com.bfs.hibernateprojectdemo.dto.OrderRequest;
import com.bfs.hibernateprojectdemo.exception.ResourceNotFoundException;

@Service
public class OrderService {

    private final OrderDao orderDao;
    private final UserDao userDao;
    private final ProductDao productDao;

    @Autowired
    public OrderService(OrderDao orderDao, UserDao userDao, ProductDao productDao) {
        this.orderDao = orderDao;
        this.userDao=userDao;
        this.productDao=productDao;
    }

    @Transactional
    public Order createOrder(OrderRequest orderRequest) {
        // Find the user
        User user = userDao.findUserById(orderRequest.getUserId().intValue())  // Adjusted to Integer
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + orderRequest.getUserId()));

        // Create Order entity
        Order order = new Order();
        order.setUser(user);
        order.setStatus(Order.Status.PROCESSING);

        List<OrderItem> orderItems = new ArrayList<>();

        // Process each order item in the request
        for (OrderRequest.OrderItemRequest itemRequest : orderRequest.getItems()) {
            Product product = productDao.findProductById(itemRequest.getProductId().intValue())  // Adjusted to Integer
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + itemRequest.getProductId()));

            if (product.getStockQuantity() < itemRequest.getQuantity()) {
                throw new IllegalArgumentException("Not enough stock for product ID: " + itemRequest.getProductId());
            }

            // Create OrderItem and set values
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPriceAtPurchase(BigDecimal.valueOf(product.getRetailPrice()));  // Adjusted to BigDecimal
            orderItems.add(orderItem);

            // Update product stock
            product.setStockQuantity(product.getStockQuantity() - itemRequest.getQuantity());
            productDao.updateProduct(product);
        }

        // Link order items to order
        order.setOrderItems(orderItems);  // Ensure this method exists in Order
        orderDao.createOrder(order);

        return order;
    }
    @Transactional
    public List<Order> findOrdersByUserId(int userId) {
    	 User user = userDao.findUserById(userId)
    	            .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
    	    
    	    List<Order> orders = orderDao.findOrdersByUserId(userId);
    	    orders.forEach(order -> order.getOrderItems().size());
    	    return orders;
    	}


    @Transactional
    public List<Order> findAllOrders() {
        List<Order> orders = orderDao.findAllOrders();
        orders.forEach(order -> order.getOrderItems().size()); // Forces initialization of orderItems
        return orders;
    }

    @Transactional
    public Order findOrderById(int id) {
        Order order = orderDao.findOrderById(id);
        if (order == null) {
            throw new ResourceNotFoundException("Order not found with ID: " + id);
        }
        order.getOrderItems().size();  // Initializes the orderItems collection
        return order;
    }

    @Transactional
    public Order cancelOrder(int id) {
        Order order = orderDao.findOrderById(id);
        if (order == null) {
            throw new ResourceNotFoundException("Order not found with ID: " + id);
        }
        if (order.getStatus() == Status.CANCELED) {
            throw new IllegalStateException("Order already canceled.");
        }

        if (order.getStatus() == Status.COMPLETED) {
            throw new IllegalStateException("Completed orders cannot be canceled.");
        }

        if (order.getStatus() == Status.PROCESSING) {
            order.setStatus(Status.CANCELED);

            // Restore stock for each order item
            for (OrderItem item : order.getOrderItems()) {
                Product product = item.getProduct();
                product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
                productDao.updateProduct(product);
            }

            orderDao.updateOrderStatus(order);
        }

        return order;
    }

    @Transactional
    public Order completeOrder(int id) {
        Order order = findOrderById(id);
        if (order == null) {
            throw new ResourceNotFoundException("Order not found with ID: " + id);
        }

        if (order.getStatus() == Status.COMPLETED) {
            throw new IllegalStateException("Order already completed.");
        }

        if (order.getStatus() == Status.CANCELED) {
            throw new IllegalStateException("Canceled orders cannot be completed.");
        }

        order.setStatus(Status.COMPLETED);
        orderDao.updateOrderStatus(order);
        return order;
    }
}
