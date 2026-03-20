package com.leedtech.otp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.leedtech.otp.config.AuthContext;
import org.leedtech.otp.exceptions.LeedTechException;
import org.leedtech.otp.mapper.PaymentHistoryMapper;
import org.leedtech.otp.repository.EnrollmentRepository;
import org.leedtech.otp.repository.PaymentHistoryRepository;
import org.leedtech.otp.service.impl.FeePaymentServiceImpl;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class FeePaymentServiceImplTest {

    @Mock
    private PaymentHistoryRepository phRepo;

    @Mock
    private EnrollmentRepository enRepo;

    @Mock
    private PaymentHistoryMapper mapper;

    @Mock
    private AuthContext authContext;

    private FeePaymentServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new FeePaymentServiceImpl(phRepo, enRepo, mapper, authContext);
    }

    @Test
    void calculateIncentiveRate_tier1_returnsOnePercent() {
        BigDecimal paymentAmount = BigDecimal.valueOf(50000);
        float incentiveRate = service.calculateIncentiveRate(paymentAmount);
        assertEquals(0.01f, incentiveRate);
    }

    @Test
    void calculateIncentiveRate_tier2_returnsThreePercent() {
        BigDecimal paymentAmount = BigDecimal.valueOf(100000);
        float incentiveRate = service.calculateIncentiveRate(paymentAmount);
        assertEquals(0.03f, incentiveRate);
    }

    @Test
    void calculateIncentiveRate_tier3_returnsFivePercent() {
        BigDecimal paymentAmount = BigDecimal.valueOf(500000);
        float incentiveRate = service.calculateIncentiveRate(paymentAmount);
        assertEquals(0.05f, incentiveRate);
    }

    @ParameterizedTest
    @CsvSource({
            "50000, 0.01",
            "99999, 0.01",
            "100000, 0.03",
            "250000, 0.03",
            "499999, 0.03",
            "500000, 0.05",
            "1000000, 0.05"
    })
    void calculateIncentiveRate_boundaryValues(BigDecimal amount, float expectedRate) {
        float actualRate = service.calculateIncentiveRate(amount);
        assertEquals(expectedRate, actualRate);
    }

    @Test
    void calculateNextDueDate_weekday_returns90DaysLater() {
        LocalDate paymentDate = LocalDate.of(2026, 1, 2);
        LocalDate dueDate = service.calculateNextDueDate(paymentDate);
        assertEquals(LocalDate.of(2026, 4, 2), dueDate);
    }

    @Test
    void calculateNextDueDate_fallsOnSaturday_adjustsToMonday() {
        LocalDate paymentDate = LocalDate.of(2026, 4, 5);
        LocalDate dueDate = service.calculateNextDueDate(paymentDate);
        assertEquals(LocalDate.of(2026, 7, 6), dueDate);
    }

    @Test
    void calculateNextDueDate_fallsOnSunday_adjustsToMonday() {
        LocalDate paymentDate = LocalDate.of(2026, 4, 6);
        LocalDate dueDate = service.calculateNextDueDate(paymentDate);
        assertEquals(LocalDate.of(2026, 7, 6), dueDate);
    }

    @Test
    void calculateNextDueDate_friday_staysFriday() {
        LocalDate paymentDate = LocalDate.of(2026, 4, 3);
        LocalDate dueDate = service.calculateNextDueDate(paymentDate);
        assertEquals(LocalDate.of(2026, 7, 2), dueDate);
    }

    @Test
    void calculateNextDueDate_saturday_adjustedToMonday() {
        LocalDate paymentDate = LocalDate.of(2026, 1, 4);
        LocalDate dueDate = service.calculateNextDueDate(paymentDate);
        assertEquals(LocalDate.of(2026, 4, 6), dueDate);
    }

    @Test
    void calculateNextDueDate_sunday_adjustedToMonday() {
        LocalDate paymentDate = LocalDate.of(2026, 1, 5);
        LocalDate dueDate = service.calculateNextDueDate(paymentDate);
        assertEquals(LocalDate.of(2026, 4, 6), dueDate);
    }

    @Test
    void calculateNextDueDate_boundaryValues() {
        LocalDate friday = LocalDate.of(2026, 6, 5);
        LocalDate saturday = LocalDate.of(2026, 6, 8);
        LocalDate sunday = LocalDate.of(2026, 6, 9);

        assertEquals(LocalDate.of(2026, 9, 3), service.calculateNextDueDate(friday));
        assertEquals(LocalDate.of(2026, 9, 7), service.calculateNextDueDate(saturday));
        assertEquals(LocalDate.of(2026, 9, 7), service.calculateNextDueDate(sunday));
    }

    @Test
    void processPayment_zeroAmount_throwsInvalidParameterError() {
        org.leedtech.otp.domain.PaymentPayload payload = new org.leedtech.otp.domain.PaymentPayload(
                "STU001", BigDecimal.ZERO, LocalDate.now()
        );

        LeedTechException exception = assertThrows(LeedTechException.class, () -> {
            service.processPayment(payload);
        });

        assertEquals(400, exception.getProblem().statusCode());
        assertTrue(exception.getProblem().title().contains("Bad Parameters"));
    }

    @Test
    void processPayment_negativeAmount_throwsInvalidParameterError() {
        org.leedtech.otp.domain.PaymentPayload payload = new org.leedtech.otp.domain.PaymentPayload(
                "STU001", BigDecimal.valueOf(-100), LocalDate.now()
        );

        LeedTechException exception = assertThrows(LeedTechException.class, () -> {
            service.processPayment(payload);
        });

        assertEquals(400, exception.getProblem().statusCode());
    }

    @Test
    void processPayment_exactlyZero_throwsInvalidParameterError() {
        org.leedtech.otp.domain.PaymentPayload payload = new org.leedtech.otp.domain.PaymentPayload(
                "STU001", BigDecimal.valueOf(0), LocalDate.now()
        );

        LeedTechException exception = assertThrows(LeedTechException.class, () -> {
            service.processPayment(payload);
        });

        assertEquals(400, exception.getProblem().statusCode());
    }

    @Test
    void processPayment_boundaryOneCent_throwsInvalidParameterError() {
        org.leedtech.otp.domain.PaymentPayload payload = new org.leedtech.otp.domain.PaymentPayload(
                "STU001", BigDecimal.valueOf(0.01), LocalDate.now()
        );

        assertThrows(LeedTechException.class, () -> {
            service.processPayment(payload);
        });
    }

    @Test
    void processPayment_oneThousand_passesValidation() {
        when(enRepo.findFirstByStudent_StudentNumberOrderByCreatedOnDesc(any()))
                .thenReturn(java.util.Optional.empty());

        org.leedtech.otp.domain.PaymentPayload payload = new org.leedtech.otp.domain.PaymentPayload(
                "LT26P588", BigDecimal.valueOf(1000), LocalDate.now()
        );

        LeedTechException exception = assertThrows(LeedTechException.class, () -> {
            service.processPayment(payload);
        });

        assertEquals(404, exception.getProblem().statusCode());
    }

    @Test
    void processPayment_studentNotEnrolled_throwsNotFoundError() {
        when(enRepo.findFirstByStudent_StudentNumberOrderByCreatedOnDesc("STU001"))
                .thenReturn(java.util.Optional.empty());

        org.leedtech.otp.domain.PaymentPayload payload = new org.leedtech.otp.domain.PaymentPayload(
                "STU001", BigDecimal.valueOf(50000), LocalDate.now()
        );

        LeedTechException exception = assertThrows(LeedTechException.class, () -> {
            service.processPayment(payload);
        });

        assertEquals(404, exception.getProblem().statusCode());
        assertTrue(exception.getProblem().detail().contains("Student not enrolled"));
    }
}
