package org.leedtech.otp.domain;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PaymentHistory(
        @NotBlank String studentNumber,
        @NotNull Double previousAmount,
        @Min(value = 1, message = "Amount cannot be less than 1") Double paymentAmount,
        @NotNull float incentiveRate,
        @NotNull Double incentiveAmount,
        Double newBalance,
        LocalDate paymentDate,
        LocalDate nextPaymentDueDate


//        Student Number
//                Previous balance
//                Payment amount
//                Incentive rate
//                Incentive amount
//                New balance
//                Next payment due date
) {}