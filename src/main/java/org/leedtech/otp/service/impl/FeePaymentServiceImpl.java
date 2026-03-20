package org.leedtech.otp.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.leedtech.otp.config.AuthContext;
import org.leedtech.otp.constant.Role;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author DB.Tech
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FeePaymentServiceImpl implements FeePaymentService {

    private final PaymentHistoryRepository phRepo;
    private final EnrollmentRepository enRepo;
    private final PaymentHistoryMapper mapper;
    private final AuthContext authContext;


    public PaymentHistory processPayment(PaymentPayload payload) {
        if (payload.paymentAmount().compareTo(BigDecimal.valueOf(0)) <= 0) {
            throw Problems.INVALID_PARAMETER_ERROR.withProblemError("paymentAmount", "paymentAmount cannot be less than 1").toException();
        }
        var recentEnrollment = enRepo.findFirstByStudent_StudentNumberOrderByCreatedOnDesc(payload.studentNumber())
                .orElseThrow(() -> Problems.NOT_FOUND.withDetail("Student not enrolled").toException());
        var lastPayment = phRepo.findFirstByEnrollment_AcademicYearAndEnrollment_Student_StudentNumberOrderByCreatedOnDesc(recentEnrollment.getAcademicYear(), payload.studentNumber())
                .orElseThrow(() -> Problems.NOT_FOUND.withDetail("Student not enrolled").toException());

        if (lastPayment.isInGoodStanding()) {
            throw Problems.PAYMENT_REQUIRED.withProblemError("Is In Good Standing",
                    "Student has completed payment").toException();
        }

        var currentBalance = lastPayment.getNewBalance();

        return getPaymentHistory(payload, currentBalance, recentEnrollment, false);
    }

    @Override
    public List<PaymentHistory> getPayments() {
        var payments = phRepo.findAll();
        if (!authContext.isAuthorized(Role.ADMIN)) {
            payments = phRepo.findAllByEnrollment_Student_Email(authContext.getAuthUser().getName());
        }
        return mapper.asDomainObjects(payments);
    }

    @Override
    public PaymentHistory getPayment(UUID id) {
        var payment = phRepo.findById(id).orElseThrow(() -> Problems.NOT_FOUND.withProblemError("PaymentHistory",
                "Payment %s not found".formatted(id.toString())).toException());
        if (!authContext.isAuthorized(Role.ADMIN) &&
                payment.getEnrollment().getStudent().getEmail().equals(authContext.getAuthUser().getName())) {
            throw Problems.NOT_FOUND.withProblemError("PaymentHistory",
                    "Payment %s not found".formatted(id.toString())).toException();
        }

        return mapper.asDomainObject(payment);
    }


    public PaymentHistory processPayment(PaymentPayload payload, EnrollmentEntity enrollment) {
        var currentBalance = enrollment.getFees();

        return getPaymentHistory(payload, currentBalance, enrollment, true);
    }

    private PaymentHistory getPaymentHistory(PaymentPayload payload, BigDecimal currentBalance, EnrollmentEntity recentEnrollment, boolean isEnrollment) {

        LocalDate dateToUse = (payload.paymentDate() != null) ? payload.paymentDate() : LocalDate.now();

        var paymentAmount = payload.paymentAmount();
        float incentiveRate = calculateIncentiveRate(paymentAmount);
        BigDecimal incentiveAmount = paymentAmount.multiply(BigDecimal.valueOf(incentiveRate));
        BigDecimal totalPayment = paymentAmount.add(incentiveAmount);

        if (!isEnrollment) {
            if (currentBalance.compareTo(totalPayment) < 0) {
                incentiveRate = calculateIncentiveRate(paymentAmount);
                totalPayment = currentBalance;
                incentiveAmount = totalPayment.subtract(paymentAmount);
            }

            if (currentBalance.compareTo(paymentAmount) <= 0) {
                paymentAmount = currentBalance;
                incentiveRate = calculateIncentiveRate(paymentAmount);
                incentiveAmount = BigDecimal.valueOf(0);
                totalPayment = paymentAmount;
            }
        }

        var newBalance = currentBalance.subtract(totalPayment);
        if(isEnrollment) {
            newBalance = currentBalance.subtract(totalPayment);
        }
        LocalDate nextPaymentDueDate = calculateNextDueDate(dateToUse);

        var phEntity = PaymentHistoryEntity.builder()
                .studentNumber(payload.studentNumber())
                .previousAmount(currentBalance)
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


    public float calculateIncentiveRate(BigDecimal paymentAmount) {
        if (paymentAmount.compareTo(BigDecimal.valueOf(500000)) >= 0) {
            return Float.parseFloat("0.05");
        } else if (paymentAmount.compareTo(BigDecimal.valueOf(100000)) >= 0) {
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