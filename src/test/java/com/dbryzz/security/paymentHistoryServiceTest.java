package com.dbryzz.security;

import org.junit.jupiter.api.Test;
import org.leedtech.otp.service.FeePaymentService;

import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class paymentHistoryServiceTest {

    private final FeePaymentService service = new FeePaymentService();

    @Test
    public void testIncentiveCalculation() {
        BigDecimal paymentAmount = new BigDecimal("100000");
        BigDecimal incentiveRate = service.calculateIncentiveRate(paymentAmount);
        assertEquals(new BigDecimal("0.03"), incentiveRate);
    }

    @Test
    public void testNextDueDateAdjustment() {
        LocalDate paymentDate = LocalDate.of(2026, 7, 2); // Thursday
        LocalDate dueDate = service.calculateNextDueDate(paymentDate);
        assertEquals(LocalDate.of(2026, 10, 3), dueDate); // Expecting to fall on a weekend
    }
}
