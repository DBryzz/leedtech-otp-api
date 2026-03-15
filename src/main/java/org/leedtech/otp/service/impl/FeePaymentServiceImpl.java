package org.leedtech.otp.service.impl;

import org.leedtech.otp.domain.PaymentHistory;
import org.leedtech.otp.domain.PaymentPayload;
import org.leedtech.otp.entity.EnrollmentEntity;
import org.leedtech.otp.entity.PaymentHistoryEntity;
import org.leedtech.otp.exceptions.Problems;
import org.leedtech.otp.mapper.PaymentHistoryMapper;
import org.leedtech.otp.repository.EnrollmentRepository;
import org.leedtech.otp.repository.PaymentHistoryRepository;
import org.leedtech.otp.service.FeePaymentService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class FeePaymentServiceImpl implements FeePaymentService {

    PaymentHistoryRepository phRepo;
    EnrollmentRepository enRepo;
    PaymentHistoryMapper mapper;

    public PaymentHistory processPayment(PaymentPayload payload) {
        var recentEnrollment = enRepo.findFirstByStudent_StudentNumberByOrderByCreatedOnDesc(payload.studentNumber())
                .orElseThrow(() -> Problems.NOT_FOUND.withDetail("Student not enrolled").toException());
        var lastPayment = phRepo.findFirstByEnrollment_AcademicYearAndEnrollment_Student_StudentNumberByOrderByPaymentDateDesc(recentEnrollment.getAcademicYear(), payload.studentNumber())
                .orElseThrow(() -> Problems.NOT_FOUND.withDetail("Student not enrolled").toException());

        if (lastPayment.isInGoodStanding()) {
            throw Problems.PAYMENT_REQUIRED.withProblemError("Is In Good Standing",
                    "Student has completed payment").toException();
        }

        var currentBalance = lastPayment.getNewBalance();

        return getPaymentHistory(payload, currentBalance, recentEnrollment);
    }


    public PaymentHistory processPayment(PaymentPayload payload, EnrollmentEntity enrollment) {
        var currentBalance = enrollment.getInitialDeposit();

        return getPaymentHistory(payload, currentBalance, enrollment);
    }

    private PaymentHistory getPaymentHistory(PaymentPayload payload, Double currentBalance, EnrollmentEntity recentEnrollment) {
        LocalDate dateToUse = (payload.paymentDate() != null) ? payload.paymentDate() : LocalDate.now();

        var paymentAmount = payload.paymentAmount();
        float incentiveRate = calculateIncentiveRate(paymentAmount);
        Double incentiveAmount = paymentAmount*incentiveRate;
        Double totalPayment = paymentAmount + incentiveAmount;

        var newBalance = currentBalance - totalPayment;
        LocalDate nextPaymentDueDate = calculateNextDueDate(dateToUse);

        var phEntity = PaymentHistoryEntity.builder()
                .studentNumber(payload.studentNumber())
                .incentiveRate(incentiveRate)
                .incentiveAmount(incentiveAmount)
                .paymentAmount(paymentAmount)
                .totalPaymentAmount(totalPayment)
                .newBalance(newBalance)
                .paymentDate(dateToUse)
                .nextPaymentDueDate(nextPaymentDueDate)
                .enrollment(recentEnrollment)
                .build();

        phEntity = phRepo.save(phEntity);

        return mapper.asDomainObject(phEntity);
    }


    public float calculateIncentiveRate(Double paymentAmount) {
        if (paymentAmount.compareTo(Double.parseDouble("500000")) >= 0) {
            return Float.parseFloat("0.05");
        } else if (paymentAmount.compareTo(Double.parseDouble("100000")) >= 0) {
            return Float.parseFloat("0.03");
        } else {
            return Float.parseFloat("0.01");
        }
    }


    public LocalDate calculateNextDueDate(LocalDate paymentDate) {
        LocalDate dueDate = paymentDate.plusDays(90);
        // Adjust for weekends
        if (dueDate.getDayOfWeek().getValue() == 6) {  // Saturday
            dueDate = dueDate.plusDays(2);
        } else if (dueDate.getDayOfWeek().getValue() == 7) {  // Sunday
            dueDate = dueDate.plusDays(1);
        }
        return dueDate;
    }
}