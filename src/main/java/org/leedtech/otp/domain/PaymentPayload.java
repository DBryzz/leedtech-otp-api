package org.leedtech.otp.domain;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record PaymentPayload(
        @NotBlank String studentNumber,
        @Min(value = 1, message = "Amount cannot be less than 1") Double paymentAmount,
        LocalDate paymentDate
) {}