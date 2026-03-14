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
public record UserToken(UUID id, String token, String type, User user, LocalDateTime expiryDate,
                        LocalDateTime createdOn, LocalDateTime updatedOn, UUID createdBy, UUID updatedBy) implements Domain {


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
}