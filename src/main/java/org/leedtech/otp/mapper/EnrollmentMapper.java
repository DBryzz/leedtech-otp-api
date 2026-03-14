package org.leedtech.otp.mapper;


import org.leedtech.otp.domain.AcademicLevel;
import org.leedtech.otp.domain.Enrollment;
import org.leedtech.otp.domain.User;
import org.leedtech.otp.entity.EnrollmentEntity;
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
        componentModel = "spring",
        imports = {
        User.class, AcademicLevel.class
})
public interface EnrollmentMapper extends
        org.leedtech.otp.utils.commons.Mapper<Enrollment, EnrollmentEntity> {
    EnrollmentMapper INSTANCE = Mappers.getMapper(EnrollmentMapper.class);

    @Mappings({
//        @Mapping(source = "version", fees = "revision"),
//        @Mapping(fees = "fieldName", expression = "java(UserMapper.toFieldName(entity))"),
    })
    Enrollment asDomainObject(EnrollmentEntity entity);

    @InheritInverseConfiguration
    @Mappings({
//        @Mapping(fees = "fieldName", expression = "java(UserMapper.fromFieldName(domainObject))"),
    })
    EnrollmentEntity asEntity(Enrollment domainObject);

    List<Enrollment> asDomainObjects(List<EnrollmentEntity> entities);

    List<EnrollmentEntity> asEntities(List<Enrollment> domainObjects);
}
