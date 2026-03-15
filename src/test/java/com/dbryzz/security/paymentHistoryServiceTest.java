package com.dbryzz.security;

import org.junit.jupiter.api.Test;
import org.leedtech.otp.service.impl.FeePaymentServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class paymentHistoryServiceTest {

    private final FeePaymentServiceImpl service = new FeePaymentServiceImpl();

    @Test
    public void testIncentiveCalculation() {
        Double paymentAmount = Double.parseDouble("100000");
        float incentiveRate = service.calculateIncentiveRate(paymentAmount);
        assertEquals(Float.parseFloat("0.03"), incentiveRate);
    }

    @Test
    public void testNextDueDateAdjustment() {
        LocalDate paymentDate = LocalDate.of(2026, 7, 2); // Thursday
        LocalDate dueDate = service.calculateNextDueDate(paymentDate);
        assertEquals(LocalDate.of(2026, 10, 3), dueDate); // Expecting to fall on a weekend
    }
}
