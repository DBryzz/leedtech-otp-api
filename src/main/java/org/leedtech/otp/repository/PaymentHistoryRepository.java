package org.leedtech.otp.repository;

import org.leedtech.otp.entity.PaymentHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 *
 * @author DB.Tech
 */
public interface PaymentHistoryRepository extends JpaRepository<PaymentHistoryEntity, UUID> {
    Optional<PaymentHistoryEntity> findFirstByEnrollment_AcademicYearAndEnrollment_Student_StudentNumberOrderByCreatedOnDesc(String academicYear, String studentNumber);
    Optional<PaymentHistoryEntity> findFirstByEnrollment_Student_StudentNumberOrderByPaymentDateDesc(String studentNumber);
    List<PaymentHistoryEntity> findAllByEnrollment_Student_Email(String studentEmail);
}