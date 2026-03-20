package org.leedtech.otp.utils.helperclasses;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.leedtech.otp.domain.MinimalUser;
import org.leedtech.otp.utils.commons.ExtendedEmailValidator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;


/**
 *
 * @author DB.Tech
 */
@RequiredArgsConstructor
public class HelperDomain {

    private static final String VALID_DATE_TIME = "^(\\d{4})(-(\\d{2}))(-(\\d{2}))??(T(\\d{2}):(\\d{2})(:(\\d{2})))$";


    @Builder
    public record RegisterRequest(String firstName, String lastName, @ExtendedEmailValidator String email, @Size(min=8) String password) {
    }

    @Builder
    public record AuthenticationRequest(@ExtendedEmailValidator String email,  String password) {
    }

    @Builder
    public record AuthenticationResponse(String token, String message, boolean success, MinimalUser user) {
        public AuthenticationResponse(String token, String message, boolean success) {
            this(token, message, success, null);
        }
    }

    @Builder
    public record ConfirmEmailResponse(String token, ResponseMessage responseMessage) {

    }

    @Builder
    public record UpdateUserRole(String email, String role) {
    }


    public record EmailDTO(@ExtendedEmailValidator String email) {
    }

    public record PasswordDTO(String oldPassword, @NotBlank String password, @NotBlank String confirmPassword) {
    }

    public record UpdateUserProfileRequest(String firstName, String lastName, String phoneNumber, String country,
                                           String region, String city, String language) {
    }

    public record EmailRequest(String email) {
    }


    /**
     * Enrollment Domain
     */
    @Builder
    public record EnrollmentRequest(BigDecimal fees, BigDecimal initialDeposit, UUID academicLevelId, String academicYear, LocalDate paymentDate) {
    }


    /**
     * PaymentHistory Domain
     */
    @Builder
    public record ChallengeReportRequest(int numberEvangelizedTo, int numberOfNewConverts,
                                         int numberFollowedUp, String difficulties, String remark) {
    }

    public record RequestProps(UUID id, String status, String type, String role, List<UUID> ids, boolean blocked, UUID challengeId) {
    }


}
