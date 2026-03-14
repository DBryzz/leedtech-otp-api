package org.leedtech.otp.service;

import org.leedtech.otp.domain.PaymentHistory;
import org.leedtech.otp.domain.PaymentPayload;
import org.leedtech.otp.repository.PaymentHistoryRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class FeePaymentService {

    PaymentHistoryRepository phRepo;

//    public FeePayment processPayment(String studentNumber, BigDecimal payload.paymentAmount(), LocalDate paymentDate) {
//        // Validate input
//        if (payload.paymentAmount().compareTo(BigDecimal.ZERO) <= 0) {
//            throw new IllegalArgumentException("Payment amount must be greater than 0");
//        }
//
//        // Calculate incentive based on the payment amount
//        BigDecimal incentiveRate = calculateIncentiveRate(payload.paymentAmount());
//        BigDecimal incentiveAmount = payload.paymentAmount().multiply(incentiveRate);
//        BigDecimal totalPayment = payload.paymentAmount().add(incentiveAmount);
//
//        // Here you would typically fetch the current account balance from the database.
//        BigDecimal currentBalance = getCurrentBalance(studentNumber); // Example method
//        BigDecimal newBalance = currentBalance.subtract(totalPayment);
//
//        // Calculate next payment due date
//        LocalDate nextPaymentDueDate = calculateNextDueDate(paymentDate);
//
//        // Create and return the FeePayment object
//        return new FeePayment(studentNumber, payload.paymentAmount(), paymentDate, incentiveRate, incentiveAmount, newBalance, nextPaymentDueDate);
//    }
//
//    private BigDecimal calculateIncentiveRate(BigDecimal payload.paymentAmount()) {
//        if (payload.paymentAmount().compareTo(new BigDecimal("500000")) >= 0) {
//            return new BigDecimal("0.05");
//        } else if (payload.paymentAmount().compareTo(new BigDecimal("100000")) >= 0) {
//            return new BigDecimal("0.03");
//        } else {
//            return new BigDecimal("0.01");
//        }
//    }


    public PaymentHistory processPayment(PaymentPayload payload) {
//        if (payload.paymentAmount().compareTo(BigDecimal.ZERO) <= 0) {
//            throw new IllegalArgumentException("Payment amount must be greater than 0");
//        }

        LocalDate dateToUse = (payload.paymentDate() != null) ? payload.paymentDate() : LocalDate.now();


        var paymentAmount = payload.paymentAmount();
        float incentiveRate = calculateIncentiveRate(paymentAmount);
//        Double incentiveAmount = paymentAmount.multiply(incentiveRate);
//        Double totalPayment = paymentAmount.add(incentiveAmount);
//
//        BigDecimal currentBalance = getCurrentBalance(studentNumber); // Example method
//        BigDecimal newBalance = currentBalance.subtract(totalPayment);
//        LocalDate nextPaymentDueDate = calculateNextDueDate(paymentDate);
//
//        return new PaymentHistory(studentNumber, paymentAmount, paymentDate, incentiveRate, incentiveAmount, newBalance, nextPaymentDueDate);
        return null;
    }

    private float calculateIncentiveRate(Double paymentAmount) {
//        return switch (paymentAmount.compareTo(Double.parseDouble("500000"))) {
//            case 1 -> Double.parseDouble("0.05");  // x >= 500K
//            case 0, -1 when paymentAmount.compareTo(Double.parseDouble("100000")) >= 0 -> Double.parseDouble("0.03"); // 100K ≤ x < 500K
//            default -> Double.parseDouble("0.01");  // 0 < x < 100K
//        };
        if (paymentAmount.compareTo(Double.parseDouble("500000")) >= 0) {
            return Float.parseFloat("0.05");
        } else if (paymentAmount.compareTo(Double.parseDouble("100000")) >= 0) {
            return Float.parseFloat("0.03");
        } else {
            return Float.parseFloat("0.01");
        }
    }


    private LocalDate calculateNextDueDate(LocalDate paymentDate) {
        LocalDate dueDate = paymentDate.plusDays(90);
        // Adjust for weekends
        if (dueDate.getDayOfWeek().getValue() == 6) {  // Saturday
            dueDate = dueDate.plusDays(2);
        } else if (dueDate.getDayOfWeek().getValue() == 7) {  // Sunday
            dueDate = dueDate.plusDays(1);
        }
        return dueDate;
    }

    private Double getCurrentBalance(String studentNumber) {
        // This method should interact with a database/repository to fetch the current balance



        return 800000.0; // Example value; replace with actual logic
    }
}