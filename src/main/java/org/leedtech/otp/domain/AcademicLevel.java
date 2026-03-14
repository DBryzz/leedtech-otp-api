package org.leedtech.otp.domain;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.leedtech.otp.entity.AcademicLevelEntity;
import org.leedtech.otp.utils.commons.Domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author DB.Tech
 */
@Builder
public record AcademicLevel(UUID id, @NotBlank String name, Double fees,
                            LocalDateTime createdOn, LocalDateTime updatedOn, UUID createdBy, UUID updatedBy) implements Domain {

    @Override
    public String alternateName() {
        return "";
    }

    @Override
    public String description() {
        return "";
    }


    public AcademicLevel justMinimal () {
        return new AcademicLevel(id, name, fees, createdOn,
                updatedOn, createdBy, updatedBy);
    }


}


