package com.shopping.orderservice.service.impl;

import com.shopping.orderservice.dto.OrderDto;
import com.shopping.orderservice.dto.ProductDto;
import com.shopping.orderservice.entity.Customer;
import com.shopping.orderservice.entity.Order;
import com.shopping.orderservice.entity.OrderDetail;
import com.shopping.orderservice.exception.OrderNotFoundException;
import com.shopping.orderservice.mapper.OrderDetailMapper;
import com.shopping.orderservice.mapper.OrderMapper;
import com.shopping.orderservice.repository.CustomerRepository;
import com.shopping.orderservice.repository.OrderDetailRepository;
import com.shopping.orderservice.repository.OrderRepository;
import com.shopping.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CustomerRepository customerRepository;
    private final OrderMapper orderMapper;
    private final OrderDetailMapper orderDetailMapper;
    private final RestTemplate restTemplate;

    @Value("${product.service.url}")
    private String productServiceUrl;

    @Override
    public List<OrderDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orderMapper.toDtoList(orders);
    }

    @Override
    public OrderDto getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderDto> getOrdersByCustomerId(Long customerId) {
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        return orderMapper.toDtoList(orders);
    }

    @Override
    @Transactional
    public OrderDto createOrder(OrderDto orderDto) {
        Customer customer = customerRepository.findById(orderDto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Order order = Order.builder()
                .customer(customer)
                .orderDate(LocalDateTime.now())
                .status(Order.OrderStatus.CREATED)
                .totalAmount(BigDecimal.ZERO)
                .shippingAddress(orderDto.getShippingAddress())
                .build();

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (var detailDto : orderDto.getOrderDetails()) {
            // Fetch product details from product service
            ProductDto productDto = restTemplate.getForObject(
                    productServiceUrl + "/" + detailDto.getProductId(),
                    ProductDto.class
            );

            BigDecimal unitPrice = BigDecimal.valueOf(productDto.getPrice());
            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(detailDto.getQuantity()));

            OrderDetail detail = OrderDetail.builder()
                    .order(order)
                    .productId(detailDto.getProductId())
                    .productName(productDto.getTitle())
                    .quantity(detailDto.getQuantity())
                    .unitPrice(unitPrice)
                    .subtotal(subtotal)
                    .build();

            order.addOrderDetail(detail);
            totalAmount = totalAmount.add(subtotal);
        }

        order.setTotalAmount(totalAmount);
        Order savedOrder = orderRepository.save(order);

        return orderMapper.toDto(savedOrder);
    }

    @Override
    @Transactional
    public OrderDto updateOrder(Long id, OrderDto orderDto) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        // Clear existing order details
        existingOrder.getOrderDetails().clear();

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (var detailDto : orderDto.getOrderDetails()) {
            // Fetch product details from product service
            ProductDto productDto = restTemplate.getForObject(
                    productServiceUrl + "/" + detailDto.getProductId(),
                    ProductDto.class
            );

            BigDecimal unitPrice = BigDecimal.valueOf(productDto.getPrice());
            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(detailDto.getQuantity()));

            OrderDetail detail = OrderDetail.builder()
                    .order(existingOrder)
                    .productId(detailDto.getProductId())
                    .productName(productDto.getTitle())
                    .quantity(detailDto.getQuantity())
                    .unitPrice(unitPrice)
                    .subtotal(subtotal)
                    .build();

            existingOrder.addOrderDetail(detail);
            totalAmount = totalAmount.add(subtotal);
        }

        existingOrder.setTotalAmount(totalAmount);
        existingOrder.setShippingAddress(orderDto.getShippingAddress());

        Order updatedOrder = orderRepository.save(existingOrder);
        return orderMapper.toDto(updatedOrder);
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        orderRepository.delete(order);
    }

    @Override
    @Transactional
    public OrderDto updateOrderStatus(Long id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        try {
            Order.OrderStatus newStatus = Order.OrderStatus.valueOf(status.toUpperCase());
            order.setStatus(newStatus);
            Order updatedOrder = orderRepository.save(order);
            return orderMapper.toDto(updatedOrder);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid order status: " + status);
        }
    }
}