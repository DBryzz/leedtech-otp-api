package org.leedtech.otp.service;

import org.leedtech.otp.domain.AcademicLevel;
import org.leedtech.otp.utils.helperclasses.ResponseMessage;

import java.util.List;
import java.util.UUID;


/**
 *
 * @author DB.Tech
 */
public interface AcademicLevelService {
    ResponseMessage<AcademicLevel> store(AcademicLevel academicLevel);
    ResponseMessage<AcademicLevel> update(UUID id, AcademicLevel academicLevel);
    AcademicLevel getAcademicLevel(UUID id);
    List<AcademicLevel> getAcademicLevels();

    ResponseMessage<AcademicLevel> deleteAcademicLevel(UUID id);
}
