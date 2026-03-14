package org.leedtech.otp.service;

import org.leedtech.otp.utils.helperclasses.HelperDomain;
import org.leedtech.otp.utils.helperclasses.ResponseMessage;

import java.util.List;
import java.util.UUID;


/**
 * Ecomie Project.
 *
 * @author DB.Tech
 */
public interface AcademicLevel {
    ResponseMessage<HelperDomain.AcademicLevel> store(HelperDomain.AcademicLevel academicLevel);
    ResponseMessage<HelperDomain.AcademicLevel> changeType(UUID id, String status);

    ResponseMessage<HelperDomain.AcademicLevel> update(UUID id, HelperDomain.AcademicLevel academicLevel);
    HelperDomain.AcademicLevel getChallenge(UUID id);
    List<HelperDomain.AcademicLevel> getChallenges();

    ResponseMessage<HelperDomain.AcademicLevel> deleteChallenge(UUID id);
}
