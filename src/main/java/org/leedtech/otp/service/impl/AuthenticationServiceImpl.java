package org.leedtech.otp.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.leedtech.otp.config.AuthContext;
import org.leedtech.otp.constant.Role;
import org.leedtech.otp.domain.MinimalUser;
import org.leedtech.otp.entity.UserEntity;
import org.leedtech.otp.exceptions.Problems;
import org.leedtech.otp.mapper.UserMapper;
import org.leedtech.otp.repository.UserTokenRepository;
import org.leedtech.otp.repository.UserRepository;
import org.leedtech.otp.service.AuthenticationService;
import org.leedtech.otp.service.JwtService;
import org.leedtech.otp.utils.helperclasses.HelperDomain.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;


/**
 *
 * @author DB.Tech
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepo;
    private final UserTokenRepository tokenRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserTokenRepository verificationTokenRepo;
    private final UserMapper userMapper;
    private final AuthContext authContext;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        String msg = "user already exist";
        if (userRepo.findByEmail(request.email()).isPresent())
            throw Problems.UNIQUE_CONSTRAINT_VIOLATION_ERROR.withProblemError("RegisterRequest.email", "Email (%s) already in use".formatted(request.email())).toException();

        var user = UserEntity.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .accountBlocked(false)
                .accountEnabled(false)
                .accountSoftDeleted(false)
                .build();
        if(authContext.isAuthorized(Role.ADMIN)) {
            user.setAccountEnabled(true);
        }

        user = userRepo.save(user);
//        var userEntity = authContext.isAuthorized(Role.ADMIN) ? user : null;

        var jwtToken = jwtService.generateToken(user);
        msg = "user created";
        return getAuthenticationResponse(true, msg, jwtToken, user);
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        String message =  "user does not exist";
        var user = userRepo.findByEmail(request.email()).orElseThrow(
                () -> Problems.NOT_FOUND.withProblemError("UserEntity",
                        "User with email %s not found".formatted(request.email())).toException());

//        var user = userRepo.findByEmail(request.email()).get();

        if (!user.isEnabled()) {
            message = "Account not enabled";
            return getAuthenticationResponse(false, message, null, null);
        }

        if (!user.isAccountNonLocked()) {
            message = "Account is initialDeposit. Contact Administrator";
            return getAuthenticationResponse(false, message, null, null);
        }

        if (user.getAccountSoftDeleted()) {
            message = "Account is deleted. Contact Administrator";
            return getAuthenticationResponse(false, message, null, null);
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );
        } catch(AuthenticationException e) {
            throw Problems.UNAUTHORIZED.withDetail("Email and password do not match").toException();
        }

        var jwtToken = jwtService.generateToken(user);
//        var jwtToken = jwtService.gen(user);
        message = "authenticated";
        return getAuthenticationResponse(true, message, jwtToken, user);
    }


    private AuthenticationResponse getAuthenticationResponse(boolean success, String message, String jwtToken, UserEntity userEntity) {
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .message(message)
                .success(success)
                .user(Objects.nonNull(userEntity) ? MinimalUser.fromUser(userMapper.asDomainObject(userEntity)) : null)
                .build();
    }
}
