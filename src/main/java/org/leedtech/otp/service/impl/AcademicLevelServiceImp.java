package org.leedtech.otp.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.leedtech.otp.config.AuthContext;
import org.leedtech.otp.constant.ChallengeType;
import org.leedtech.otp.constant.Role;
import org.leedtech.otp.entity.AcademicLevelEntity;
import org.leedtech.otp.exceptions.Problems;
import org.leedtech.otp.entity.ChallengeEntity;
import org.leedtech.otp.mapper.ChallengeMapper;
import org.leedtech.otp.repository.ChallengeRepository;
import org.leedtech.otp.repository.SessionRepository;
import org.leedtech.otp.repository.SubscriptionRepository;
import org.leedtech.otp.repository.UserRepository;
import org.leedtech.otp.utils.commons.Mapper;
import org.leedtech.otp.utils.helperclasses.HelperDomain.*;
import org.leedtech.otp.utils.helperclasses.ResponseMessage;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * Ecomie Project.
 *
 * @author DB.Tech
 */
@Service
@RequiredArgsConstructor
public class AcademicLevelServiceImp implements org.leedtech.otp.service.AcademicLevel {

    private final ChallengeRepository challengeRepo;
    private final SessionRepository sessionRepo;
    private final UserRepository userRepo;
    private final SubscriptionRepository subscriptionRepo;
    private final AuthContext authContext;
    private final ChallengeMapper mapper;


    @Override
    @Transactional
    public ResponseMessage<AcademicLevel> store(AcademicLevel academicLevel) {

        if (!EnumUtils.isValidEnum(ChallengeType.class, academicLevel.type().toUpperCase())) {
            throw Problems.BAD_REQUEST.withProblemError("challengeEntity.type",
                    "Invalid challenge type (%s)".formatted(academicLevel.type())).toException();
        }
        var type = academicLevel.type().toUpperCase();
        challengeRepo.findByName(academicLevel.name()).ifPresent(_ -> {
            throw Problems.UNIQUE_CONSTRAINT_VIOLATION_ERROR.withProblemError("challengeEntity.name",
                    "Challenge with name (%s) already exist".formatted(academicLevel.name())).toException();
        });
        var challengeEntity = ChallengeEntity.builder()
                .name(academicLevel.name())
                .description(academicLevel.description())
                .type(ChallengeType.valueOf(type))
                .target(academicLevel.target())
                .build();

        if (academicLevel.sessions() != null) {
            addSessionsToChallenge(academicLevel.sessions(), challengeEntity);
        }
        ChallengeEntity savedChallenge = challengeRepo.save(challengeEntity);
        return new ResponseMessage.SuccessResponseMessage<>("Challenge created. Type: " + type,
                mapper.asDomainObject(savedChallenge));

    }

    @Override
    public ResponseMessage<AcademicLevel> changeType(UUID id, String type) {
        var challenge = challengeRepo.findById(id).orElseThrow(
                () -> Problems.NOT_FOUND.withProblemError("challengeEntity",
                        "Challenge with id (%s) not found".formatted(id.toString())).toException());
        if (!EnumUtils.isValidEnum(ChallengeType.class, type.toUpperCase())) {
            throw Problems.BAD_REQUEST.withProblemError("challengeEntity.type",
                    "Invalid challenge type (%s)".formatted(type)).toException();
        }
        challenge.setType(ChallengeType.valueOf(type.toUpperCase()));

        var updatedChallenge = challengeRepo.save(challenge);
        return new ResponseMessage.SuccessResponseMessage<>("Challenge type changed. Type: " + updatedChallenge.getType(),
                mapper.asDomainObject(updatedChallenge));
    }

    @Override
    @Transactional
    public ResponseMessage<AcademicLevel> update(UUID challengeId, AcademicLevel academicLevel) {
        var challengeEntity = challengeRepo.findById(challengeId).orElseThrow(
                () -> Problems.NOT_FOUND.withProblemError("challengeEntity",
                        "Challenge with id (%s) not found".formatted(challengeId.toString())).toException());

        if(!challengeEntity.getName().equalsIgnoreCase(academicLevel.name())) {
            challengeRepo.findByName(academicLevel.name()).ifPresent(chal -> {
                throw Problems.UNIQUE_CONSTRAINT_VIOLATION_ERROR.withProblemError("challengeEntity.name",
                        "Challenge with the name (%s) already exist".formatted(chal.getName())).toException();
            });
        }

        if (!EnumUtils.isValidEnum(ChallengeType.class, academicLevel.type().toUpperCase())) {
            throw Problems.BAD_REQUEST.withProblemError("challengeEntity.type",
                    "Invalid challenge type (%s)".formatted(academicLevel.type())).toException();
        }

        if (academicLevel.sessions() != null) {
            addSessionsToChallenge(academicLevel.sessions(), challengeEntity);
        }

        List<AcademicLevelEntity> sessions = null;
        if (challengeEntity.getSessions() != null) {
            sessions = challengeEntity.getSessions();
            challengeEntity.setSessions(null);
        }
        var oldChallenge = mapper.asDomainObject(challengeEntity);
        var oldJsonChallenge = Mapper.toJsonObject(oldChallenge);
        var newJsonChallenge = Mapper.toJsonObject(academicLevel);

        academicLevel = Mapper.withUpdateValuesOnly(oldJsonChallenge, newJsonChallenge, AcademicLevel.class);

        challengeEntity = mapper.asEntity(academicLevel);
        if (sessions != null) {
            challengeEntity.setSessions(sessions);
        }
        var updatedChallenge = challengeRepo.save(challengeEntity);

        return new ResponseMessage.SuccessResponseMessage<>("ChallengeEntity updated. Type: " + updatedChallenge.getType(),
                mapper.asDomainObject(updatedChallenge));
    }

    private ChallengeEntity addSessionsToChallenge(List<Session> sessions, ChallengeEntity challengeEntity) {
        var sessionIds = sessions.stream().map(Session::id).toList();
        if (!sessionIds.isEmpty()) {
            sessionIds.forEach(id -> {
                var session = sessionRepo.findById(id).orElseThrow(
                        () -> Problems.NOT_FOUND.withProblemError("sessionEntity",
                                "Session with id (%s) not found".formatted(id.toString())).toException());
                challengeEntity.addSession(session);
            });
        }
        return challengeEntity;
    }

    @Override
    public AcademicLevel getChallenge(UUID challengeId) {
        Authentication authUser = authContext.getAuthUser();
        var challengeEntity = challengeRepo.findById(challengeId).orElseThrow(
                () -> Problems.NOT_FOUND.withProblemError("challengeEntity",
                        "Challenge with id (%s) not found".formatted(challengeId.toString())).toException());
        var challenge = mapper.asDomainObject(challengeEntity);
        if (authUser.getAuthorities().stream().noneMatch(authority
                -> authority.getAuthority().contains(Role.ADMIN.name()))){
            return challenge.justMinimal();
        }

        return challenge;
    }

    @Override
    public List<AcademicLevel> getChallenges() {
        Authentication authUser = authContext.getAuthUser();

        var challengeEntities = challengeRepo.findAll();
        var challenges = mapper.asDomainObjects(challengeEntities);

        if (authUser.getAuthorities().stream().noneMatch(authority
                -> authority.getAuthority().contains(Role.ADMIN.name()))){
            return challenges.stream().map(AcademicLevel::justMinimal).collect(Collectors.toList());

        }

        return challenges;
    }

    @Override
    public ResponseMessage<AcademicLevel> deleteChallenge(UUID id) {
        challengeRepo.findById(id).orElseThrow(() -> Problems.NOT_FOUND.withProblemError("challengeEntity",
                "Challenge with id (%s) not found".formatted(id.toString())).toException());
        challengeRepo.deleteById(id);
        return new ResponseMessage.SuccessResponseMessage<>("Challenge deleted successfully");
    }

}
