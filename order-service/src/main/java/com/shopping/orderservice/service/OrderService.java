package com.shopping.orderservice.service;

import com.shopping.orderservice.dto.OrderDto;

import java.util.List;

public interface OrderService {
    List<OrderDto> getAllOrders();
    OrderDto getOrderById(Long id);
    List<OrderDto> getOrdersByCustomerId(Long customerId);
    OrderDto createOrder(OrderDto orderDto);
    OrderDto updateOrder(Long id, OrderDto orderDto);
    void deleteOrder(Long id);
    OrderDto updateOrderStatus(Long id, String status);
}