package org.leedtech.otp.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.leedtech.otp.config.AuthContext;
import org.leedtech.otp.constant.Role;
import org.leedtech.otp.domain.Enrollment;
import org.leedtech.otp.domain.PaymentPayload;
import org.leedtech.otp.entity.AcademicLevelEntity;
import org.leedtech.otp.entity.EnrollmentEntity;
import org.leedtech.otp.exceptions.Problems;
import org.leedtech.otp.entity.UserEntity;
import org.leedtech.otp.mapper.EnrollmentMapper;
import org.leedtech.otp.repository.AcademicLevelRepository;
import org.leedtech.otp.repository.EnrollmentRepository;
import org.leedtech.otp.repository.UserRepository;
import org.leedtech.otp.service.EnrollmentService;
import org.leedtech.otp.utils.helperclasses.HelperDomain.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 *
 * @author DB.Tech
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepo;
    private final AcademicLevelRepository academicLevelRepo;
    private final FeePaymentServiceImpl paymentService;
    private final UserRepository userRepo;
    private final AuthContext authContext;
    private final EnrollmentMapper mapper;


    @Override
    @Transactional
    public Enrollment enroll(EnrollmentRequest enrollmentRequest) {
        UserEntity userEntity = userRepo.findByEmail(authContext.getAuthUser().getName()).orElseThrow(
                () -> Problems.NOT_FOUND.withProblemError("userEntity",
                        "User with email (%s) not found".formatted(authContext.getAuthUser().getName())).toException());
        return createEnrollment(enrollmentRequest, userEntity);

    }

    @Override
    @Transactional
    public Enrollment enrollUser(UUID id, EnrollmentRequest enrollmentRequest) {
        UserEntity userEntity = userRepo.findById(id).orElseThrow(
                () -> Problems.NOT_FOUND.withProblemError("userEntity",
                        "User with id (%s) not found".formatted(id.toString())).toException());
        return createEnrollment(enrollmentRequest, userEntity);
    }


    @Override
    public Enrollment getEnrollment(UUID subscriptionId) {
        var enrollment = enrollmentRepo.findById(subscriptionId).orElseThrow(
                () -> Problems.NOT_FOUND.withProblemError("subscriptionEntity",
                        "Subscription with id (%s) not found".formatted(subscriptionId.toString())).toException());
        if (!authContext.isAuthorized(Role.ADMIN)) {
            if (!enrollment.getStudent().getEmail().equals(authContext.getAuthUser().getName())) {
                throw Problems.INCONSISTENT_STATE_ERROR.withDetail("User not subscribed").toException();
            }
        }

        return mapper.asDomainObject(enrollment);
    }

    @Override
    public List<Enrollment> getEnrollments(boolean isOngoing) {
        List<EnrollmentEntity> subscriptionEntities = new ArrayList<>();
        if(authContext.isAuthorized(Role.ADMIN)) {
            subscriptionEntities = enrollmentRepo.findAll();
        } else {
            String userEmail = authContext.getAuthUser().getName();
            subscriptionEntities = enrollmentRepo.findAllByStudent_Email(userEmail);
        }

        return mapper.asDomainObjects(subscriptionEntities);
    }



    private Enrollment createEnrollment(EnrollmentRequest enrollmentRequest, @NotNull UserEntity userEntity) {
        if(!userEntity.getRole().equals(Role.STUDENT)) {
            throw Problems.FORBIDDEN_OPERATION_ERROR.withProblemError("userEntity",
                    "User not an STUDENT").toException();
        }
//        SessionEntity sessionEntity = sessionRepo.findByStatus(SessionStatus.ONGOING).orElseThrow(() -> Problems.NOT_FOUND.withProblemError("sessionEntity", "No ongoing Session found").toException());

        AcademicLevelEntity academicLevelEntity = academicLevelRepo.findById(enrollmentRequest.academicLevelId()).orElseThrow(
                () -> Problems.NOT_FOUND.withProblemError("academicLevelId",
                        "Academic level with id (%s) does not exist"
                                .formatted(enrollmentRequest.academicLevelId().toString())).toException());



        var enrollment = EnrollmentEntity.builder()
                .fees(enrollmentRequest.fees())
                .initialDeposit(enrollmentRequest.initialDeposit())
                .student(userEntity)
                .academicLevel(academicLevelEntity)
                .academicYear(enrollmentRequest.academicYear())
                .build();

        enrollment = enrollmentRepo.save(enrollment);

        var paymentPayload = PaymentPayload.builder()
                .paymentAmount(enrollmentRequest.initialDeposit())
                .studentNumber(userEntity.getEmail())
                .paymentDate(enrollmentRequest.paymentDate())
                .build();

        var ph = paymentService.processPayment(paymentPayload);

        return authContext.isAuthorized(Role.ADMIN)
                ? mapper.asDomainObject(enrollment)
                : mapper.asDomainObject(enrollment).justMinimal();
    }


}
