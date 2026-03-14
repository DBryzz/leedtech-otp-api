package org.leedtech.otp.mapper;


import org.apache.commons.lang3.EnumUtils;
import org.leedtech.otp.constant.SessionStatus;
import org.leedtech.otp.entity.AcademicLevelEntity;
import org.leedtech.otp.exceptions.Problems;
import org.leedtech.otp.utils.helperclasses.HelperDomain.*;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Objects;

/**
 * Ecomie Project.
 *
 * @author DB.Tech
 */
@Mapper(
        componentModel = "spring",
        imports = {
        AcademicLevel.class
}, uses = {
        ChallengeMapper.class
})
public interface SessionMapper extends
        org.leedtech.otp.utils.commons.Mapper<Session, AcademicLevelEntity> {
    SessionMapper INSTANCE = Mappers.getMapper(SessionMapper.class);

    @Mappings({
//        @Mapping(source = "version", target = "revision"),
        @Mapping(target = "status", expression = "java(SessionMapper.toStatus(entity))"),
    })
    Session asDomainObject(AcademicLevelEntity entity);

    @InheritInverseConfiguration
    @Mappings({
        @Mapping(target = "status", expression = "java(SessionMapper.fromStatus(domainObject))"),
    })
    AcademicLevelEntity asEntity(Session domainObject);

    List<Session> asDomainObjects(List<AcademicLevelEntity> entities);

    List<AcademicLevelEntity> asEntities(List<Session> domainObjects);


    static String toStatus(AcademicLevelEntity entity) {
        return !Objects.isNull(entity.getStatus()) ? entity.getStatus().name() : null;
    }

    static SessionStatus fromStatus(Session domainObject) {
        if(Objects.isNull(domainObject.status())) {
            return null;
        }
        if (EnumUtils.isValidEnum(SessionStatus.class, domainObject.status().toUpperCase())) {
            return SessionStatus.valueOf(domainObject.status().toUpperCase());
        }

        throw  Problems.INVALID_PARAMETER_ERROR.withProblemError("status", "Invalid session status (%s)".formatted(domainObject.status())).toException();
    }

//    static String toChallenges(SessionEntity entity) {
//        return entity.getStatus().name();
//    }
//
//    static Role fromChallenges(Session domainObject) {
//        if (EnumUtils.isValidEnum(Role.class, domainObject.status().toUpperCase())) {
//            return Role.valueOf(domainObject.status().toUpperCase());
//        }
//
//        throw  Problems.INVALID_PARAMETER_ERROR.withProblemError("status", "Invalid session status (%s)".formatted(domainObject.status())).toException();
//    }

}
