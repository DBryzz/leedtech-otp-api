package org.leedtech.otp.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.leedtech.otp.constant.Role;
import org.leedtech.otp.exceptions.Problems;
import org.leedtech.otp.mapper.UserMapper;
import org.leedtech.otp.entity.UserEntity;
import org.leedtech.otp.repository.UserRepository;
import org.leedtech.otp.service.UserService;
import org.leedtech.otp.utils.helperclasses.HelperDomain.*;
import org.leedtech.otp.utils.helperclasses.ResponseMessage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


/**
 *
 * @author DB.Tech
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper mapper;


    @Override
    public ResponseMessage<User> changeUserRole(String email, String role) {
        log.info("UserServiceImpl.changeUserRole");
        var user = userRepository.findByEmail(email).orElseThrow(
                () -> Problems.NOT_FOUND.withProblemError("userEntity",
                        "User with email (%s) not found".formatted(email)).toException());
        if (!EnumUtils.isValidEnum(Role.class, role.toUpperCase()))
            throw Problems.BAD_REQUEST.withProblemError("role", "Invalid user role").toException();
        if(!user.getRole().name().equals(role.toUpperCase())) {
            user.setRole(Role.valueOf(role.toUpperCase()));
            user = userRepository.save(user);
        }
        return new ResponseMessage.SuccessResponseMessage<>("User role updated - " + user.getRole(),
                mapper.asDomainObject(user));
    }


    @Override
    public User getUserProfile(UUID userId) {
        log.info("UserServiceImpl.getUserProfile");

        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> Problems.NOT_FOUND.withProblemError("userEntity", "User with id (%s) not found".formatted(userId.toString())).toException());
        return mapper.asDomainObject(userEntity);
    }

    @Override
    public List<User> getAllUsers() {
        return mapper.asDomainObjects(userRepository.findAll());
    }


    @Override
    public ResponseMessage<User> enableUser(UUID id) {
        log.info("UserServiceImpl.enableUser");
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> Problems.NOT_FOUND.withProblemError("userEntity", "User with id (%s) not found".formatted(id.toString())).toException());

        UserEntity updatedUser = userEntity;

        if (!userEntity.isEnabled()) {
            userEntity.setAccountEnabled(true) ;
            updatedUser = userRepository.save(userEntity);
        }


        return new ResponseMessage.SuccessResponseMessage<>("Account Enabled",
                mapper.asDomainObject(updatedUser));
    }

}
