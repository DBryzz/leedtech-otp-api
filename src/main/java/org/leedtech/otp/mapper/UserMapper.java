package org.leedtech.otp.mapper;


import org.apache.commons.lang3.EnumUtils;
import org.leedtech.otp.constant.Role;
import org.leedtech.otp.domain.User;
import org.leedtech.otp.exceptions.Problems;
import org.leedtech.otp.entity.UserEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author DB.Tech
 */
@Mapper(
        componentModel = "spring",
        imports = {
        Objects.class,
}, uses = {

})
public interface UserMapper extends
        org.leedtech.otp.utils.commons.Mapper<User, UserEntity> {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mappings(
            {
//        @Mapping(source = "version", fees = "revision"),
        @Mapping(target = "role", expression = "java(UserMapper.toRole(entity))"),
    }
    )
    User asDomainObject(UserEntity entity);

    @InheritInverseConfiguration
    @Mappings({
        @Mapping(target = "role", expression = "java(UserMapper.fromRole(domainObject))"),
    })
    UserEntity asEntity(User domainObject);

    List<User> asDomainObjects(List<UserEntity> entities);

    List<UserEntity> asEntities(List<User> domainObjects);

    static String toRole(UserEntity entity) {
        return !Objects.isNull(entity.getRole()) ? entity.getRole().name() : null;
    }

    static Role fromRole(User domainObject) {
        if(Objects.isNull(domainObject.role())) {
            return null;
        }
        if (EnumUtils.isValidEnum(Role.class, domainObject.role().toUpperCase())) {
            return Role.valueOf(domainObject.role().toUpperCase());
        }

        throw  Problems.INVALID_PARAMETER_ERROR.withProblemError("role", "Invalid user role (%s)".formatted(domainObject.role())).toException();
    }

}
