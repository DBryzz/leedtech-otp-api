package org.leedtech.otp.domain;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * @author DB.Tech
 */
@Builder
public record PaymentPayload(
        @NotBlank String studentNumber,
        @DecimalMin(value = "1", message = "Amount cannot be less than 1") BigDecimal paymentAmount,
        LocalDate paymentDate
) {}