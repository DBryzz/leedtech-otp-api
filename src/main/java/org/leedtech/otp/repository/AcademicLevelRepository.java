package org.leedtech.otp.repository;

import org.leedtech.otp.entity.AcademicLevelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


/**
 *
 * @author DB.Tech
 */
public interface AcademicLevelRepository extends JpaRepository<AcademicLevelEntity, UUID> {
    Optional<AcademicLevelEntity> findByName(String name);
}
