package com.shopping.paymentservice.controller;

import com.shopping.paymentservice.dto.PaymentDto;
import com.shopping.paymentservice.dto.PaymentRequestDto;
import com.shopping.paymentservice.dto.PaymentResponseDto;
import com.shopping.paymentservice.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    public ResponseEntity<List<PaymentDto>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDto> getPaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<PaymentDto>> getPaymentsByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(paymentService.getPaymentsByOrderId(orderId));
    }

    @PostMapping("/process")
    public ResponseEntity<PaymentResponseDto> processPayment(
            @Valid @RequestBody PaymentRequestDto paymentRequestDto) {
        PaymentResponseDto responseDto = paymentService.processPayment(paymentRequestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/refund")
    public ResponseEntity<PaymentResponseDto> refundPayment(@PathVariable Long id) {
        PaymentResponseDto responseDto = paymentService.refundPayment(id);
        return ResponseEntity.ok(responseDto);
    }
}