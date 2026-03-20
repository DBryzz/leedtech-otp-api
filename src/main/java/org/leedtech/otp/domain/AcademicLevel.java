package org.leedtech.otp.domain;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.leedtech.otp.entity.AcademicLevelEntity;
import org.leedtech.otp.exceptions.Problems;
import org.leedtech.otp.utils.commons.Domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author DB.Tech
 */
@Builder
public record AcademicLevel(UUID id, @NotBlank String name,
                            @DecimalMin(value = "1.0", message = "Fees cannot be less than 1") BigDecimal fees,
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


