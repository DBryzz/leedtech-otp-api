package org.leedtech.otp.domain;

import lombok.Builder;
import org.leedtech.otp.utils.commons.Domain;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 *
 * @author DB.Tech
 */
@Builder
public record Enrollment(UUID id, Double fees, Double initialDeposit, User student, AcademicLevel academicLevel,
                         String academicYear, LocalDateTime createdOn, LocalDateTime updatedOn,
                         UUID createdBy, UUID updatedBy) implements Domain {

    @Override
    public String name() {
        return "";
    }

    @Override
    public String description() {
        return "";
    }

    @Override
    public String alternateName() {
        return "";
    }


    public Enrollment justMinimal () {
        return new Enrollment(id, fees, initialDeposit, student.justMinimal(), academicLevel.justMinimal(), academicYear,
                createdOn, updatedOn, createdBy, updatedBy);
    }
}