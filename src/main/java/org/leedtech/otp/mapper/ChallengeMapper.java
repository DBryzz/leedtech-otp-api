package org.leedtech.otp.mapper;


import org.leedtech.otp.entity.AcademicLevelEntity;
import org.leedtech.otp.entity.ChallengeEntity;
import org.leedtech.otp.exceptions.Problems;
import org.leedtech.otp.repository.RepositoryFactory;
import org.leedtech.otp.utils.helperclasses.HelperDomain.AcademicLevel;
import org.leedtech.otp.utils.helperclasses.HelperDomain.Session;
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
        componentModel = "spring"
//        imports = {
//        Session.class
//}, uses = {
//        SessionMapper.class
//}
)
public interface ChallengeMapper extends
        org.leedtech.otp.utils.commons.Mapper<AcademicLevel, ChallengeEntity> {
    ChallengeMapper INSTANCE = Mappers.getMapper(ChallengeMapper.class);

    @Mappings({
        @Mapping(target = "sessions", expression = "java(ChallengeMapper.mapOnlySessionIdsAndName(entity))")
    })
    AcademicLevel asDomainObject(ChallengeEntity entity);

    @InheritInverseConfiguration
    @Mappings({
            @Mapping(target = "sessions", expression = "java(ChallengeMapper.mapEntireSessionEntities(domainObject))")
    })
    ChallengeEntity asEntity(AcademicLevel domainObject);

    List<AcademicLevel> asDomainObjects(List<ChallengeEntity> entities);

    List<ChallengeEntity> asEntities(List<AcademicLevel> domainObjects);

    static List<Session> mapOnlySessionIdsAndName(ChallengeEntity entity) {
        if (Objects.isNull(entity.getSessions()) || entity.getSessions().isEmpty()) {
            return null;
        }
        return entity.getSessions().stream()
                .map(sessionEntity -> Session.builder()
                        .id(sessionEntity.getId())
                        .name(sessionEntity.getName())
                        .description(sessionEntity.getDescription())
                        .status(sessionEntity.getStatus().name())
                        .build()).toList();
    }

    static List<AcademicLevelEntity> mapEntireSessionEntities(AcademicLevel domainObject) {
        if (Objects.isNull(domainObject.sessions()) || domainObject.sessions().isEmpty()) {
            return null;
        }
        return domainObject.sessions().stream()
                .map(session -> RepositoryFactory.getSessionRepository()
                        .findById(session.id())
                        .orElseThrow(() -> Problems.NOT_FOUND.withProblemError("session",
                                "session with id (%s) not found".formatted(session.id().toString()))
                                .toException()))
                .toList();
    }
}
