package org.leedtech.otp.repository;

import org.leedtech.otp.constant.SessionStatus;
import org.leedtech.otp.entity.AcademicLevelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * Ecomie Project.
 *
 * @author DB.Tech
 */
public interface SessionRepository extends JpaRepository<AcademicLevelEntity, UUID> {
    Optional<AcademicLevelEntity> findByName(String name);
    Optional<AcademicLevelEntity> findByStatus(SessionStatus status);
    List<AcademicLevelEntity> findSessionsByChallenges_Id(UUID challengeId);
    boolean existsByStatus(SessionStatus status);
}
