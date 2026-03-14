package org.leedtech.otp.service;

import org.leedtech.otp.domain.Enrollment;
import org.leedtech.otp.utils.helperclasses.HelperDomain.*;

import java.util.List;
import java.util.UUID;


/**
 * Ecomie Project.
 *
 * @author DB.Tech
 */
public interface EnrollmentService {
    Enrollment enroll(EnrollmentRequest enrollmentRequest);
    Enrollment enrollUser(UUID userId, EnrollmentRequest enrollmentRequest);
    Enrollment getEnrollment(UUID enrollmentId);
    List<Enrollment> getEnrollments(boolean isOngoing);
}
