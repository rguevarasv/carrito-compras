package com.shopping.paymentservice.service.impl;

import com.shopping.paymentservice.dto.OrderDto;
import com.shopping.paymentservice.dto.PaymentDto;
import com.shopping.paymentservice.dto.PaymentRequestDto;
import com.shopping.paymentservice.dto.PaymentResponseDto;
import com.shopping.paymentservice.entity.Payment;
import com.shopping.paymentservice.exception.PaymentNotFoundException;
import com.shopping.paymentservice.exception.PaymentProcessingException;
import com.shopping.paymentservice.mapper.PaymentMapper;
import com.shopping.paymentservice.repository.PaymentRepository;
import com.shopping.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final RestTemplate restTemplate;

    @Value("${order.service.url}")
    private String orderServiceUrl;

    @Override
    public List<PaymentDto> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        return paymentMapper.toDtoList(payments);
    }

    @Override
    public PaymentDto getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException(id));
        return paymentMapper.toDto(payment);
    }

    @Override
    public List<PaymentDto> getPaymentsByOrderId(Long orderId) {
        List<Payment> payments = paymentRepository.findByOrderId(orderId);
        return paymentMapper.toDtoList(payments);
    }

    @Override
    @Transactional
    public PaymentResponseDto processPayment(PaymentRequestDto paymentRequestDto) {
        // Fetch order details
        OrderDto orderDto = restTemplate.getForObject(
                orderServiceUrl + "/" + paymentRequestDto.getOrderId(),
                OrderDto.class
        );

        if (orderDto == null) {
            throw new PaymentProcessingException("Order not found with id: " + paymentRequestDto.getOrderId());
        }

        // Validate credit card (basic validation simulation)
        validateCreditCardInfo(paymentRequestDto);

        // Process payment (simulation)
        String transactionId = UUID.randomUUID().toString();
        boolean paymentSuccess = simulatePaymentGateway(paymentRequestDto);

        if (!paymentSuccess) {
            throw new PaymentProcessingException("Payment failed for order: " + paymentRequestDto.getOrderId());
        }

        // Save payment record
        Payment payment = Payment.builder()
                .orderId(paymentRequestDto.getOrderId())
                .amount(BigDecimal.valueOf(orderDto.getTotalAmount()))
                .paymentDate(LocalDateTime.now())
                .status(Payment.PaymentStatus.COMPLETED)
                .paymentMethod(paymentRequestDto.getPaymentMethod())
                .transactionId(transactionId)
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        // Update order status (if necessary)
        updateOrderStatus(orderDto.getId(), "PROCESSING");

        // Return payment response
        return PaymentResponseDto.builder()
                .paymentId(savedPayment.getId())
                .orderId(savedPayment.getOrderId())
                .status(savedPayment.getStatus().name())
                .message("Payment processed successfully")
                .transactionId(savedPayment.getTransactionId())
                .amount(savedPayment.getAmount().doubleValue())
                .build();
    }

    @Override
    @Transactional
    public PaymentResponseDto refundPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));

        if (payment.getStatus() == Payment.PaymentStatus.REFUNDED) {
            throw new PaymentProcessingException("Payment has already been refunded");
        }

        // Process refund (simulation)
        boolean refundSuccess = simulateRefundGateway(payment);

        if (!refundSuccess) {
            throw new PaymentProcessingException("Refund failed for payment: " + paymentId);
        }

        // Update payment status
        payment.setStatus(Payment.PaymentStatus.REFUNDED);
        Payment updatedPayment = paymentRepository.save(payment);

        // Update order status (if necessary)
        updateOrderStatus(payment.getOrderId(), "CANCELLED");

        // Return payment response
        return PaymentResponseDto.builder()
                .paymentId(updatedPayment.getId())
                .orderId(updatedPayment.getOrderId())
                .status(updatedPayment.getStatus().name())
                .message("Payment refunded successfully")
                .transactionId(updatedPayment.getTransactionId())
                .amount(updatedPayment.getAmount().doubleValue())
                .build();
    }

    private void validateCreditCardInfo(PaymentRequestDto paymentRequestDto) {
        // Basic credit card validation simulation
        String cardNumber = paymentRequestDto.getCardNumber().replaceAll("\\s", "");

        // Check if card number is numeric and of valid length
        if (!cardNumber.matches("\\d{13,19}")) {
            throw new PaymentProcessingException("Invalid card number format");
        }

        // Check if expiry date is in valid format (MM/YY)
        if (!paymentRequestDto.getExpiryDate().matches("(0[1-9]|1[0-2])/[0-9]{2}")) {
            throw new PaymentProcessingException("Invalid expiry date format. Expected MM/YY");
        }

        // Check if CVV is valid
        if (!paymentRequestDto.getCvv().matches("\\d{3,4}")) {
            throw new PaymentProcessingException("Invalid CVV format");
        }

        // Check for expired card
        String[] expiryParts = paymentRequestDto.getExpiryDate().split("/");
        int expiryMonth = Integer.parseInt(expiryParts[0]);
        int expiryYear = Integer.parseInt(expiryParts[1]) + 2000; // Assuming 20xx

        LocalDateTime now = LocalDateTime.now();
        if (expiryYear < now.getYear() || (expiryYear == now.getYear() && expiryMonth < now.getMonthValue())) {
            throw new PaymentProcessingException("Card has expired");
        }
    }

    private boolean simulatePaymentGateway(PaymentRequestDto paymentRequestDto) {
        // This method simulates a payment gateway
        // In a real application, this would call an actual payment provider API

        // Let's simulate a success rate of 90%
        return Math.random() < 0.9;
    }

    private boolean simulateRefundGateway(Payment payment) {
        // This method simulates a refund gateway
        // In a real application, this would call an actual payment provider API

        // Let's simulate a success rate of 95%
        return Math.random() < 0.95;
    }

    private void updateOrderStatus(Long orderId, String status) {
        // Call order service to update order status
        try {
            restTemplate.patchForObject(
                    orderServiceUrl + "/" + orderId + "/status?status=" + status,
                    null,
                    OrderDto.class
            );
        } catch (Exception e) {
            // Log error but don't fail the transaction
            System.err.println("Failed to update order status: " + e.getMessage());
        }
    }
}