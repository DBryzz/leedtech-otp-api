package org.leedtech.otp.domain;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.leedtech.otp.entity.PaymentHistoryEntity;
import org.leedtech.otp.utils.commons.Domain;
import org.leedtech.otp.utils.helperclasses.HelperDomain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 *
 * @author DB.Tech
 */
@Builder
public record PaymentHistory(
        UUID id,
        @NotBlank String studentNumber,
        @NotNull BigDecimal previousAmount,
        @Min(value = 1, message = "Amount cannot be less than 1") BigDecimal paymentAmount,
        @NotNull float incentiveRate,
        @NotNull BigDecimal incentiveAmount,
        BigDecimal newBalance,
        LocalDate paymentDate,
        LocalDate nextPaymentDueDate,
        LocalDateTime createdOn, LocalDateTime updatedOn, UUID createdBy, UUID updatedBy
) implements Domain {

    public PaymentHistory justMinimal() {
        return new PaymentHistory(id, studentNumber, previousAmount, paymentAmount, incentiveRate, incentiveAmount,
                newBalance, paymentDate, nextPaymentDueDate, createdOn, updatedOn, createdBy, updatedBy);
    }

    @Override
    public String name() {
        return "";
    }

    @Override
    public String description() {
        return "";
    }

    @Override
    public String alternateName() {
        return "";
    }
}

