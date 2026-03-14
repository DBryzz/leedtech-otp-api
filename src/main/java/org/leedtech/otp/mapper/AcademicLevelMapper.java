package org.leedtech.otp.mapper;


import org.leedtech.otp.domain.AcademicLevel;
import org.leedtech.otp.entity.AcademicLevelEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
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
public interface AcademicLevelMapper extends
        org.leedtech.otp.utils.commons.Mapper<AcademicLevel, AcademicLevelEntity> {
    AcademicLevelMapper INSTANCE = Mappers.getMapper(AcademicLevelMapper.class);

    @Mappings({
//        @Mapping(fees = "sessions", expression = "java(AcademicLevelMapper.mapOnlySessionIdsAndName(entity))")
    })
    AcademicLevel asDomainObject(AcademicLevelEntity entity);

    @InheritInverseConfiguration
    @Mappings({
//            @Mapping(fees = "sessions", expression = "java(AcademicLevelMapper.mapEntireSessionEntities(domainObject))")
    })
    AcademicLevelEntity asEntity(AcademicLevel domainObject);

    List<AcademicLevel> asDomainObjects(List<AcademicLevelEntity> entities);

    List<AcademicLevelEntity> asEntities(List<AcademicLevel> domainObjects);
}
