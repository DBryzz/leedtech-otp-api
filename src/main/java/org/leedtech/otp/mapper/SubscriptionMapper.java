package org.leedtech.otp.mapper;


import org.leedtech.otp.entity.SubscriptionEntity;
import org.leedtech.otp.utils.helperclasses.HelperDomain;
import org.leedtech.otp.utils.helperclasses.HelperDomain.Session;
import org.leedtech.otp.utils.helperclasses.HelperDomain.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Ecomie Project.
 *
 * @author DB.Tech
 */
@Mapper(
        componentModel = "spring",
        imports = {
        User.class, Session.class, HelperDomain.AcademicLevel.class
}, uses = {
        UserMapper.class, SessionMapper.class, ChallengeMapper.class
})
public interface SubscriptionMapper extends
        org.leedtech.otp.utils.commons.Mapper<HelperDomain.Enrollment, SubscriptionEntity> {
    SubscriptionMapper INSTANCE = Mappers.getMapper(SubscriptionMapper.class);

    @Mappings({
//        @Mapping(source = "version", target = "revision"),
//        @Mapping(target = "fieldName", expression = "java(UserMapper.toFieldName(entity))"),
    })
    HelperDomain.Enrollment asDomainObject(SubscriptionEntity entity);

    @InheritInverseConfiguration
    @Mappings({
//        @Mapping(target = "fieldName", expression = "java(UserMapper.fromFieldName(domainObject))"),
    })
    SubscriptionEntity asEntity(HelperDomain.Enrollment domainObject);

    List<HelperDomain.Enrollment> asDomainObjects(List<SubscriptionEntity> entities);

    List<SubscriptionEntity> asEntities(List<HelperDomain.Enrollment> domainObjects);
}
