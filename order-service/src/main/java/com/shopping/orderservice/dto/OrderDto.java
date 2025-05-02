package com.shopping.orderservice.dto;

import com.shopping.orderservice.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private Long id;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    private LocalDateTime orderDate;

    private String status;

    private Double totalAmount;

    @NotEmpty(message = "Order must have at least one item")
    @Valid
    private List<OrderDetailDto> orderDetails;

    private String shippingAddress;
}