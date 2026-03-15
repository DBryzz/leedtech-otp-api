package org.leedtech.otp.repository;

import org.leedtech.otp.entity.EnrollmentEntity;
import org.leedtech.otp.entity.PaymentHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 *
 * @author DB.Tech
 */
public interface EnrollmentRepository extends JpaRepository<EnrollmentEntity, UUID> {
    List<EnrollmentEntity> findAllByStudent_Email(String userEmail);
    Optional<EnrollmentEntity> findFirstByStudent_StudentNumberByOrderByCreatedOnDesc(String studentNumber);

}
