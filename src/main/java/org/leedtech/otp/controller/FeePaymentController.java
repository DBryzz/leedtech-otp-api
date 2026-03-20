package org.leedtech.otp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.leedtech.otp.domain.PaymentHistory;
import org.leedtech.otp.domain.PaymentPayload;
import org.leedtech.otp.service.FeePaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@SecurityRequirement(name = "ApiKey")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "FeePaymentController", description = "This controller contains endpoints for payment")
public class FeePaymentController {

    private final FeePaymentService feePaymentService;

    @PostMapping(value = "/secure/student/one-time-fee-payment")
    @Operation(summary = "Make Payment", description = "Make payment", tags = { "USER", "ADMIN" })
    public ResponseEntity<PaymentHistory> makePayment(@Valid @RequestBody PaymentPayload payload) {
        var paymentHistory = feePaymentService.processPayment(payload);
        return new ResponseEntity<>(paymentHistory, HttpStatus.CREATED);
    }

    @GetMapping(value = "/secure/student/payments")
    @Operation(summary = "Make Payment", description = "Make payment", tags = { "USER", "ADMIN" })
    public ResponseEntity<List<PaymentHistory>> getPayments() {
        var paymentHistories = feePaymentService.getPayments();
        return ResponseEntity.ok(paymentHistories);
    }

    @GetMapping(value = "/secure/student/payments/{id}")
    @Operation(summary = "Make Payment", description = "Make payment", tags = { "USER", "ADMIN" })
    public ResponseEntity<PaymentHistory> makePayment(@PathVariable(name = "id") UUID id) {
        var paymentHistory = feePaymentService.getPayment(id);
        return ResponseEntity.ok(paymentHistory);
    }
}