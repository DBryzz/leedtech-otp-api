package org.leedtech.otp.repository;

import org.leedtech.otp.constant.ChallengeType;
import org.leedtech.otp.constant.SessionStatus;
import org.leedtech.otp.entity.AcademicLevelEntity;
import org.leedtech.otp.entity.ChallengeEntity;
import org.leedtech.otp.entity.SubscriptionEntity;
import org.leedtech.otp.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * Ecomie Project.
 *
 * @author DB.Tech
 */
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, UUID> {
    Optional<SubscriptionEntity> findByIdAndUser(UUID subscriptionId, UserEntity userEntity);
    List<SubscriptionEntity> findAllByUserAndChallenge(UserEntity userEntity, ChallengeEntity challengeEntity);
    List<SubscriptionEntity> findAllBySession_id(UUID sessionId);
    List<SubscriptionEntity> findAllByUser_Email(String userEmail);
    List<SubscriptionEntity> findAllBySession_StatusAndUser_Email(SessionStatus sessionStatus, String userEmail);
    List<SubscriptionEntity> findAllBySession_Status(SessionStatus sessionStatus);
    Optional<SubscriptionEntity> findBySession(AcademicLevelEntity sessionEntity);
    Optional<SubscriptionEntity> findBySessionAndBlocked(AcademicLevelEntity sessionEntity, boolean blocked);
    List<SubscriptionEntity> findAllBySessionAndUser(AcademicLevelEntity sessionEntity, UserEntity userEntity);
    List<SubscriptionEntity> findAllBySession_IdAndUser_Id(UUID sessionId, UUID userId);
    Optional<SubscriptionEntity> findBySessionAndUserAndBlocked(AcademicLevelEntity sessionEntity, UserEntity userEntity, boolean blocked);
    Optional<SubscriptionEntity> findBySessionAndChallenge(AcademicLevelEntity sessionEntity, ChallengeEntity challengeEntity);
    Optional<SubscriptionEntity> findBySessionAndChallengeAndUser(AcademicLevelEntity sessionEntity, ChallengeEntity challengeEntity, UserEntity userEntity);
    Optional<SubscriptionEntity> findBySessionAndChallengeAndUserAndBlocked(AcademicLevelEntity sessionEntity, ChallengeEntity challengeEntity, UserEntity userEntity, boolean blocked);
    boolean existsBySession_Id(UUID sessionId);

    boolean existsBySession_Id_AndUser_Id(UUID sessionId, UUID userId);

    boolean existsBySession_Status_AndUser_IdAndChallenge_Type(SessionStatus sessionStatus, UUID userId, ChallengeType challengeType);

    @Query(value = "SELECT sub.session from SubscriptionEntity sub where sub.user=:user")
    List<AcademicLevelEntity> selectAllSessionsThisUserHasSubscribedTo(@Param("user") UserEntity userEntity);
    @Query(value = "SELECT sub.session from SubscriptionEntity sub where sub.session=:session and sub.blocked=true")
    List<UserEntity> selectAllUsersBlockedInSession(@Param("session") AcademicLevelEntity sessionEntity);
    @Query(value = "SELECT sub.user from SubscriptionEntity sub where sub.session=:session and sub.challenge=:challenge")
    List<UserEntity> selectAllUsersSubscribedToSessionViaChallenge(@Param("session") AcademicLevelEntity sessionEntity, @Param("challenge") ChallengeEntity challengeEntity);
    @Query(value = "SELECT sub.user from SubscriptionEntity sub where sub.session=:session")
    List<UserEntity> selectAllUsersSubscribedToSession(@Param("session") AcademicLevelEntity sessionEntity);
    @Query(value = "SELECT sub.user from SubscriptionEntity sub where sub.session.status='ONGOING'")
    List<UserEntity> selectAllUsersSubscribedToOngoingSession();

    List<SubscriptionEntity> findAllByUser_Id(UUID userId);
}
