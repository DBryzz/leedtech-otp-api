package org.leedtech.otp.service;

import org.leedtech.otp.domain.PaymentHistory;
import org.leedtech.otp.domain.PaymentPayload;
import org.leedtech.otp.entity.EnrollmentEntity;
import org.leedtech.otp.entity.PaymentHistoryEntity;
import org.leedtech.otp.exceptions.Problems;
import org.leedtech.otp.mapper.PaymentHistoryMapper;
import org.leedtech.otp.repository.EnrollmentRepository;
import org.leedtech.otp.repository.PaymentHistoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


public interface FeePaymentService {
    PaymentHistory processPayment(PaymentPayload payload);

    List<PaymentHistory> getPayments();

    PaymentHistory getPayment(UUID id);
}