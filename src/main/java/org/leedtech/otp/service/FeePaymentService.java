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


public interface FeePaymentService {
    PaymentHistory processPayment(PaymentPayload payload);
}