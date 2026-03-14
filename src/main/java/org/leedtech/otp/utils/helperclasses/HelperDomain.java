package org.leedtech.otp.utils.helperclasses;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.csbf.ecomie.entity.*;
import org.leedtech.otp.entity.*;
import org.leedtech.otp.utils.commons.Domain;
import org.leedtech.otp.utils.commons.ExtendedEmailValidator;

import java.time.LocalDateTime;
import java.util.*;


/**
 * Ecomie Project.
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

    /**
     * User Token
     */
    @Builder
    public record UserToken(UUID id, String token, String type, User user, LocalDateTime expiryDate,
                            LocalDateTime createdOn, LocalDateTime updatedOn, UUID createdBy, UUID updatedBy) implements Domain {


        @Override
        public String name() {
            return "";
        }

        @Override
        public String description() {
            return "";
        }

        @Override
        public String alternateName() {
            return "";
        }
    }


    /**
     * User Domain
     */
    @Builder
    public record User(UUID id, String firstName, String lastName, String email, String role, String phoneNumber,
                       String country, String region, String city, String language,
                       String profilePictureFileName, Boolean accountEnabled, Boolean accountBlocked,
                       Boolean accountSoftDeleted, LocalDateTime createdOn, LocalDateTime updatedOn, UUID createdBy, UUID updatedBy) implements Domain {


        @Override
        public String name() {
            return "";
        }

        @Override
        public String description() {
            return "";
        }

        @Override
        public String alternateName() {
            return "";
        }

        public User withProfilePictureFileName(String profilePictureFileName) {
            return new User(id, firstName, lastName, email, role, phoneNumber, country, region, city, language,
                    profilePictureFileName, accountEnabled, accountBlocked, accountSoftDeleted, createdOn, updatedOn,
                    createdBy, updatedBy);
        }
        public static User justMinimal(UserEntity userEntity) {
           return new User(userEntity.getId(), userEntity.getFirstName(), userEntity.getLastName(), userEntity.getEmail(),
                    userEntity.getRole().name(), userEntity.getPhoneNumber(), userEntity.getCountry(),
                    userEntity.getRegion(), userEntity.getCity(), userEntity.getLanguage(),
                    userEntity.getProfilePictureFileName(), userEntity.getAccountEnabled(),
                    userEntity.getAccountBlocked(), userEntity.getAccountSoftDeleted(), userEntity.createdOn(), userEntity.updatedOn(),
                    userEntity.createdBy(), userEntity.updatedBy());
        }

        public User justMinimal() {
            return new User(id, firstName, lastName, email, role, phoneNumber, country, region, city, language,
                    profilePictureFileName, null, null, null,
                    createdOn, updatedOn, createdBy, updatedBy);
        }
    }

    @Builder
    public record MinimalUser(UUID id, String firstName, String lastName, String email, String role, String phoneNumber,
                       String country, String region, String city, String language,
                       String profilePictureFileName,LocalDateTime createdOn, LocalDateTime updatedOn, UUID createdBy, UUID updatedBy) implements Domain {

        @Override
        public String name() {
            return "";
        }

        @Override
        public String description() {
            return "";
        }

        @Override
        public String alternateName() {
            return "";
        }

        public static MinimalUser fromUser(User user) {
            return new MinimalUser(user.id(), user.firstName(), user.lastName(), user.email(), user.role(),
                    user.phoneNumber(), user.country(), user.region(), user.city(), user.language(),
                    user.profilePictureFileName(), user.createdOn(), user.updatedOn(), user.createdBy(), user.updatedBy());
        }
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
     * Session Domain
     */
    @Builder
//    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Session(UUID id, @NotBlank String name, String description, @Pattern(regexp =VALID_DATE_TIME) LocalDateTime startDate,
                          @Pattern(regexp =VALID_DATE_TIME) LocalDateTime endDate, String status, List<AcademicLevel> academicLevels,
                          LocalDateTime createdOn, LocalDateTime updatedOn, UUID createdBy, UUID updatedBy) implements Domain{


        @Override
        public String alternateName() {
            return "";
        }

        public static Session justMinimal(AcademicLevelEntity sessionEntity) {
            return new Session(sessionEntity.getId(), sessionEntity.getName(), sessionEntity.getDescription(),
                    sessionEntity.getStartDate(), sessionEntity.getEndDate(), sessionEntity.getStatus().name(), null,
                    sessionEntity.getCreatedOn(), sessionEntity.getUpdatedOn(), sessionEntity.getCreatedBy(), sessionEntity.getUpdatedBy());
        }

        public Session justMinimal() {
            return new Session (id, name, description, startDate, endDate, status, null, createdOn, updatedOn, createdBy, updatedBy);
        }
    }


    /**
     * AcademicLevel Domain
     */
    @Builder
    public record AcademicLevel(UUID id, @NotBlank String name, String description, @Min(value = 1) int target,
                                List<Session> sessions, String type,
                                LocalDateTime createdOn, LocalDateTime updatedOn, UUID createdBy, UUID updatedBy) implements Domain {

        @Override
        public String alternateName() {
            return "";
        }

        public static AcademicLevel justMinimal (AcademicLevelEntity academicLevelEntity) {
            return new AcademicLevel(academicLevelEntity.getId(), academicLevelEntity.getName(),  academicLevelEntity.getCreatedOn(),
                    academicLevelEntity.getUpdatedOn(), academicLevelEntity.getCreatedBy(), academicLevelEntity.getUpdatedBy());
        }

        public AcademicLevel justMinimal () {
            return new AcademicLevel(id, name, description, target, null, type, createdOn,
                    updatedOn, createdBy, updatedBy);
        }


    }

    /**
     * Enrollment Domain
     */
    @Builder
    public record EnrollmentRequest(int target, UUID challengeId) {
//        public SubscriptionCreateDto withUserId(UUID userId) {
//            return new SubscriptionCreateDto(target, userId, challengeId, sessionId);
//        }

    }

    @Builder
    public record Enrollment(UUID id, @Min(value = 1) int target, Boolean blocked, User user, AcademicLevel academicLevel,
                             Session session, LocalDateTime createdOn, LocalDateTime updatedOn,
                             UUID createdBy, UUID updatedBy) implements Domain {

        @Override
        public String name() {
            return "";
        }

        @Override
        public String description() {
            return "";
        }

        @Override
        public String alternateName() {
            return "";
        }

        public static Enrollment justMinimal (SubscriptionEntity subscriptionEntity) {
            return new Enrollment(subscriptionEntity.getId(), subscriptionEntity.getTarget(), subscriptionEntity.getBlocked(), null,
                    null, null,  subscriptionEntity.getCreatedOn(),
                    subscriptionEntity.getUpdatedOn(), subscriptionEntity.getCreatedBy(), subscriptionEntity.getUpdatedBy());
        }

        public Enrollment justMinimal () {
            return new Enrollment(id, target, blocked, user.justMinimal(), academicLevel.justMinimal(), session.justMinimal(),
                    createdOn, updatedOn, createdBy, updatedBy);
        }
    }


    /**
     * ChallengeReport Domain
     */
    @Builder
    public record ChallengeReportRequest(int numberEvangelizedTo, int numberOfNewConverts,
                                         int numberFollowedUp, String difficulties, String remark) {
    }

    public record RequestProps(UUID id, String status, String type, String role, List<UUID> ids, boolean blocked, UUID challengeId) {
    }


    @Builder
    public record ChallengeReport(UUID id, int numberEvangelizedTo, int numberOfNewConverts, int numberFollowedUp,
                                  String difficulties, String remark, User user, Enrollment enrollment,
                                  LocalDateTime createdOn, LocalDateTime updatedOn, UUID createdBy, UUID updatedBy) implements Domain {


        public static ChallengeReport justMinimal(ChallengeReportEntity challengeReportEntity) {
            return new ChallengeReport(challengeReportEntity.getId(), challengeReportEntity.getNumberEvangelizedTo(),
                    challengeReportEntity.getNumberOfNewConverts(), challengeReportEntity.getNumberFollowedUp(),
                    challengeReportEntity.getDifficulties(), challengeReportEntity.getRemark(), null, null, challengeReportEntity.getCreatedOn(),
                    challengeReportEntity.getUpdatedOn(), challengeReportEntity.getCreatedBy(), challengeReportEntity.getUpdatedBy());
        }

        public ChallengeReport justMinimal() {
            return new ChallengeReport(id,  numberEvangelizedTo, numberOfNewConverts, numberFollowedUp,
                    difficulties, remark, user.justMinimal(), enrollment.justMinimal(), createdOn, updatedOn, createdBy, updatedBy);
        }

        @Override
        public String name() {
            return "";
        }

        @Override
        public String description() {
            return "";
        }

        @Override
        public String alternateName() {
            return "";
        }
    }
}
