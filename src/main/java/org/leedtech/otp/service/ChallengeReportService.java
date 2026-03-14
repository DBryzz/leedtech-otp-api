package org.leedtech.otp.service;

import org.leedtech.otp.utils.helperclasses.HelperDomain.*;
import org.leedtech.otp.utils.helperclasses.ResponseMessage;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * Ecomie Project.
 *
 * @author DB.Tech
 */
public interface ChallengeReportService {
    ResponseMessage<ChallengeReport> storeReport(UUID subscriptionId, ChallengeReportRequest challengeReportRequest);

    ResponseMessage<ChallengeReport> storeUserReport(UUID userId, UUID subscriptionId, ChallengeReportRequest challengeReportRequest);

    ResponseMessage<ChallengeReport> updateChallengeReport(UUID reportId, ChallengeReportRequest challengeReportRequest);

    ResponseMessage<ChallengeReport> updateChallengeReportForUser(UUID reportId, ChallengeReportRequest challengeReportRequest);

    ChallengeReport getChallengeReport(UUID reportId);

    List<ChallengeReport> getChallengeReports(Optional<UUID> sessionId, Optional<UUID> challengeId);

    List<ChallengeReport> getChallengeReportsOfAChallenge(UUID challengeId);

    ResponseMessage<ChallengeReport> deleteChallengeReport(UUID id);
}

