package org.leedtech.otp.mapper;


import org.leedtech.otp.domain.Enrollment;
import org.leedtech.otp.domain.PaymentHistory;
import org.leedtech.otp.domain.User;
import org.leedtech.otp.entity.PaymentHistoryEntity;
import org.leedtech.otp.utils.helperclasses.HelperDomain.*;
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
        User.class, Enrollment.class,
}, uses = {
        UserMapper.class, EnrollmentMapper.class
})
public interface PaymentHistoryMapper extends
        org.leedtech.otp.utils.commons.Mapper<PaymentHistory, PaymentHistoryEntity> {
    PaymentHistoryMapper INSTANCE = Mappers.getMapper(PaymentHistoryMapper.class);

    @Mappings({
    })
    PaymentHistory asDomainObject(PaymentHistoryEntity entity);

    @InheritInverseConfiguration
    @Mappings({
    })
    PaymentHistoryEntity asEntity(PaymentHistory domainObject);

    List<PaymentHistory> asDomainObjects(List<PaymentHistoryEntity> entities);

    List<PaymentHistoryEntity> asEntities(List<PaymentHistory> domainObjects);

}
