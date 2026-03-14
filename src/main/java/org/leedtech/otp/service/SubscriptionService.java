package org.leedtech.otp.service;

import org.leedtech.otp.utils.helperclasses.HelperDomain;
import org.leedtech.otp.utils.helperclasses.HelperDomain.*;
import org.leedtech.otp.utils.helperclasses.ResponseMessage;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


/**
 * Ecomie Project.
 *
 * @author DB.Tech
 */
public interface SubscriptionService {
    Enrollment subscribe(EnrollmentRequest enrollmentRequest);
    Enrollment subscribeUser(UUID userId, EnrollmentRequest enrollmentRequest);
    ResponseMessage<Enrollment> unSubscribeUser(UUID subscriptionId);

    @Transactional
    ResponseMessage<Enrollment> removeUserFromSession(UUID sessionId, UUID userId);

    Enrollment update(UUID subscriptionId, @NotNull HelperDomain.EnrollmentRequest enrollmentRequest);
    Enrollment getSubscription(UUID subscriptionId);
    List<Enrollment> getSubscriptions(boolean isOngoing);
    List<Enrollment> getSessionSubscription(UUID sessionId);

    ResponseMessage<Enrollment> toggleBlock(UUID id);
}
