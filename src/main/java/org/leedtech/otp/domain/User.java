package org.leedtech.otp.domain;

import lombok.Builder;
import org.leedtech.otp.entity.UserEntity;
import org.leedtech.otp.utils.commons.Domain;

import java.time.LocalDateTime;
import java.util.UUID;


/**
 *
 * @author DB.Tech
 */
@Builder
public record User(UUID id, String firstName, String lastName, String email, String role, String phoneNumber,
                   String country, String region, String city, String language,
                   String profilePictureFileName, Boolean accountEnabled, Boolean accountBlocked,
                   Boolean accountSoftDeleted, LocalDateTime createdOn, LocalDateTime updatedOn, UUID createdBy, UUID updatedBy) implements Domain {


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


    public User justMinimal() {
        return new User(id, firstName, lastName, email, role, phoneNumber, country, region, city, language,
                profilePictureFileName, null, null, null,
                createdOn, updatedOn, createdBy, updatedBy);
    }
}