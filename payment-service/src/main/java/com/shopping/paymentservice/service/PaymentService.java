package com.shopping.paymentservice.service;

import com.shopping.paymentservice.dto.PaymentDto;
import com.shopping.paymentservice.dto.PaymentRequestDto;
import com.shopping.paymentservice.dto.PaymentResponseDto;

import java.util.List;

public interface PaymentService {
    List<PaymentDto> getAllPayments();
    PaymentDto getPaymentById(Long id);
    List<PaymentDto> getPaymentsByOrderId(Long orderId);
    PaymentResponseDto processPayment(PaymentRequestDto paymentRequestDto);
    PaymentResponseDto refundPayment(Long paymentId);
}