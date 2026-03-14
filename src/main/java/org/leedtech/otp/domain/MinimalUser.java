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
public record MinimalUser(UUID id, String firstName, String lastName, String email, String role, String phoneNumber,
                          String country, String region, String city, String language,
                          String profilePictureFileName, LocalDateTime createdOn, LocalDateTime updatedOn,
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

    public static MinimalUser fromUser(User user) {
        return new MinimalUser(user.id(), user.firstName(), user.lastName(), user.email(), user.role(),
                user.phoneNumber(), user.country(), user.region(), user.city(), user.language(),
                user.profilePictureFileName(), user.createdOn(), user.updatedOn(), user.createdBy(), user.updatedBy());
    }
}
