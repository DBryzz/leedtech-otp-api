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
    List<PaymentHistoryEntity> findAllBySubscription(SubscriptionEntity subscriptionEntity);
    Optional<PaymentHistoryEntity> findFirstByEnrollment_AcademicYearAndEnrollment_Student_StudentNumberByOrderByPaymentDateDesc(String academicYear, String studentNumber);
//    Optional<PaymentHistoryEntity> findTopByOrderByPaymentDateDesc Enrollment_AcademicYearAndEnrollment_Student_StudentNumberByOrderByPaymentDateDesc()
    Optional<PaymentHistoryEntity> findByEnrollment_AcademicYearAndEnrollment_Student_StudentNumber(String academicYear, String studentNumber);
    List<PaymentHistoryEntity> findAllByUser_Email(String userEmail);
    List<PaymentHistoryEntity> findAllBySubscription_Challenge_Id(UUID subscriptionChallengeId);

    List<PaymentHistoryEntity> findAllByUser_EmailAndSubscription_Challenge_Id(String userEmail, UUID subscriptionChallengeId);

    //    int  countChallengeReportsBySessionAndEcomiestAndChallenge(SessionEntity sessionEntity, UserEntity userEntity, ChallengeEntity challengeEntity);
    int  countAllBySubscription(SubscriptionEntity subscriptionEntity);

    List<PaymentHistoryEntity> findAllBySubscription_Session_IdAndSubscription_Challenge_Id(UUID subscriptionSessionId, UUID subscriptionChallengeId);

    List<PaymentHistoryEntity> findAllBySubscription_Session_Id(UUID sessionId);

    List<PaymentHistoryEntity> findAllByUser_EmailAndSubscription_Session_Id(String userEmail, UUID subscriptionSessionId);

    List<PaymentHistoryEntity> findAllByUser_EmailAndSubscription_Session_IdAndSubscription_Challenge_Id(String userEmail, UUID subscriptionSessionId, UUID subscriptionChallengeId);
//    @Query(value = "SELECT sum(cr.numberEvangelizedTo) from PaymentHistoryEntity cr where cr.sessionEntity=:sessionEntity and cr.ecomiest=:userEntity and cr.challengeEntity=:challengeEntity" )
//    int numberAnEcomiestEvangelizedToViaAChallengeInASession(@Param("sessionEntity") SessionEntity sessionEntity, @Param("userEntity") UserEntity userEntity, @Param("challengeEntity") ChallengeEntity challengeEntity);
//    @Query(value = "SELECT sum(cr.numberEvangelizedTo) from PaymentHistoryEntity cr where cr.sessionEntity=:sessionEntity and cr.ecomiest=:userEntity")
//    int numberAnEcomiestEvangelizedToInASession(@Param("sessionEntity") SessionEntity sessionEntity, @Param("userEntity") UserEntity userEntity);
//    @Query(value = "SELECT sum(cr.numberEvangelizedTo) from PaymentHistoryEntity cr where cr.ecomiest=:userEntity")
//    int numberAnEcomiestEvangelizedTo(@Param("userEntity") UserEntity userEntity);
}
