package org.leedtech.otp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.leedtech.otp.event.OnRegistrationCompleteEvent;
import org.leedtech.otp.service.AuthenticationService;
import org.leedtech.otp.service.EmailService;
import org.leedtech.otp.utils.commons.Domain;
import org.leedtech.otp.utils.commons.ExtendedEmailValidator;
import org.leedtech.otp.utils.helperclasses.HelperDomain.*;
import org.leedtech.otp.utils.helperclasses.ResponseMessage;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;


/**
 *
 * @author DB.Tech
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "AuthenticationController", description = "This controller contains endpoints for authentication")
public class AuthenticationController {
    private final AuthenticationService service;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final EmailService emailService;


    @PostMapping("/register")
    @Operation(summary = "Register", description = "Create account", tags = { "UNAUTHENTICATED" })
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request, HttpServletRequest servletRequest) {
        /**For Production */
        /*
        * if (EmailValidator.getInstance().isValid(request.email()))
        *    throw new BadRequestException("Invalid '" + request.email() + "' email address");
        * */

        AuthenticationResponse response = service.register(request);
        log.info("{}", response.toString());
        if (response.success()) {
            applicationEventPublisher.publishEvent(new OnRegistrationCompleteEvent(servletRequest.getHeader("host"), request.email()));
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/authenticate")
    @Operation(summary = "Authenticate", description = "Authenticate user using email and password", tags = { "UNAUTHENTICATED" })
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request, HttpServletRequest servletRequest) {
        String appUrl = servletRequest.getContextPath();

        /**For Production */
        /*
         * if (EmailValidator.getInstance().isValid(request.email()))
         *    throw new BadRequestException("Invalid '" + request.email() + "' email address");
         * */

        AuthenticationResponse response = service.authenticate(request);
        var user = response.user();
        return ResponseEntity.ok(response);
    }
}
