package org.leedtech.otp.controller;

import org.leedtech.otp.domain.PaymentHistory;
import org.leedtech.otp.domain.PaymentPayload;
import org.leedtech.otp.service.FeePaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/one-time-fee-payment")
public class FeePaymentController {

    private final FeePaymentService feePaymentService;

    public FeePaymentController(FeePaymentService feePaymentService) {
        this.feePaymentService = feePaymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentHistory> makePayment(@RequestBody PaymentPayload payload) {
        PaymentHistory paymentHistory = feePaymentService.processPayment(payload);
        return ResponseEntity.ok(paymentHistory);
    }
}