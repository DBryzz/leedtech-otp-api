package org.leedtech.otp.service.impl;

import lombok.RequiredArgsConstructor;
import org.leedtech.otp.config.AuthContext;
import org.leedtech.otp.constant.Role;
import org.leedtech.otp.domain.AcademicLevel;
import org.leedtech.otp.entity.AcademicLevelEntity;
import org.leedtech.otp.exceptions.Problems;
import org.leedtech.otp.mapper.AcademicLevelMapper;
import org.leedtech.otp.repository.AcademicLevelRepository;
import org.leedtech.otp.repository.EnrollmentRepository;
import org.leedtech.otp.repository.UserRepository;
import org.leedtech.otp.service.AcademicLevelService;
import org.leedtech.otp.utils.commons.Mapper;
import org.leedtech.otp.utils.helperclasses.ResponseMessage;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 *
 * @author DB.Tech
 */
@Service
@RequiredArgsConstructor
public class AcademicLevelServiceImp implements AcademicLevelService {

    private final AcademicLevelRepository academicLevelRepository;
    private final UserRepository userRepo;
    private final EnrollmentRepository subscriptionRepo;
    private final AuthContext authContext;
    private final AcademicLevelMapper mapper;


    @Override
    @Transactional
    public ResponseMessage<AcademicLevel> store(AcademicLevel academicLevel) {

        academicLevelRepository.findByName(academicLevel.name()).ifPresent(_ -> {
            throw Problems.UNIQUE_CONSTRAINT_VIOLATION_ERROR.withProblemError("academicLevelEntity.name",
                    "AcademicLevel with name (%s) already exist".formatted(academicLevel.name())).toException();
        });
        var academicLevelEntity = AcademicLevelEntity.builder()
                .name(academicLevel.name())
                .build();

        AcademicLevelEntity savedAcademicLevel = academicLevelRepository.save(academicLevelEntity);
        return new ResponseMessage.SuccessResponseMessage<>("Academic level created.",
                mapper.asDomainObject(savedAcademicLevel));

    }

    @Override
    @Transactional
    public ResponseMessage<AcademicLevel> update(UUID challengeId, AcademicLevel academicLevel) {
        var academicLevelEntity = academicLevelRepository.findById(challengeId).orElseThrow(
                () -> Problems.NOT_FOUND.withProblemError("AcademicLevel",
                        "Academic Level with id (%s) not found".formatted(challengeId.toString())).toException());

        if(!academicLevelEntity.getName().equalsIgnoreCase(academicLevel.name())) {
            academicLevelRepository.findByName(academicLevel.name()).ifPresent(chal -> {
                throw Problems.UNIQUE_CONSTRAINT_VIOLATION_ERROR.withProblemError("academicLevelEntity.name",
                        "AcademicLevel with the name (%s) already exist".formatted(chal.getName())).toException();
            });
        }




        var oldAcademicLevel = mapper.asDomainObject(academicLevelEntity);
        var oldJsonAcademicLevel = Mapper.toJsonObject(oldAcademicLevel);
        var newJsonAcademicLevel = Mapper.toJsonObject(academicLevel);

        academicLevel = Mapper.withUpdateValuesOnly(oldJsonAcademicLevel, newJsonAcademicLevel, AcademicLevel.class);

        academicLevelEntity = mapper.asEntity(academicLevel);

        var updatedAcademicLevel = academicLevelRepository.save(academicLevelEntity);

        return new ResponseMessage.SuccessResponseMessage<>("Academic level updated.",
                mapper.asDomainObject(updatedAcademicLevel));
    }

    @Override
    public AcademicLevel getAcademicLevel(UUID challengeId) {
        var academicLevelEntity = academicLevelRepository.findById(challengeId).orElseThrow(
                () -> Problems.NOT_FOUND.withProblemError("AcademicLevel",
                        "Academic Level with id (%s) not found".formatted(challengeId.toString())).toException());

        return mapper.asDomainObject(academicLevelEntity);
    }

    @Override
    public List<AcademicLevel> getAcademicLevels() {
        Authentication authUser = authContext.getAuthUser();

        var challengeEntities = academicLevelRepository.findAll();
        var challenges = mapper.asDomainObjects(challengeEntities);

        if (authUser.getAuthorities().stream().noneMatch(authority
                -> authority.getAuthority().contains(Role.ADMIN.name()))){
            return challenges.stream().map(AcademicLevel::justMinimal).collect(Collectors.toList());

        }

        return challenges;
    }

    @Override
    public ResponseMessage<AcademicLevel> deleteAcademicLevel(UUID id) {
        academicLevelRepository.findById(id).orElseThrow(() -> Problems.NOT_FOUND.withProblemError("academicLevelEntity",
                "AcademicLevel with id (%s) not found".formatted(id.toString())).toException());
        academicLevelRepository.deleteById(id);
        return new ResponseMessage.SuccessResponseMessage<>("Academic Level deleted successfully");
    }

}
