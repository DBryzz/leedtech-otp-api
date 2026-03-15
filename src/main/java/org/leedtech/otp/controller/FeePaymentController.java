package org.leedtech.otp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.leedtech.otp.domain.PaymentHistory;
import org.leedtech.otp.domain.PaymentPayload;
import org.leedtech.otp.service.FeePaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@SecurityRequirement(name = "ApiKey")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "FeePaymentController", description = "This controller contains endpoints for payment")
public class FeePaymentController {

    FeePaymentService feePaymentService;

    @PostMapping(value = "/secure/student/one-time-fee-payment")
    @Operation(summary = "Make Payment", description = "Make payment", tags = { "USER", "ADMIN" })
    public ResponseEntity<PaymentHistory> makePayment(@RequestBody PaymentPayload payload) {
        PaymentHistory paymentHistory = feePaymentService.processPayment(payload);
        return ResponseEntity.ok(paymentHistory);
    }
}