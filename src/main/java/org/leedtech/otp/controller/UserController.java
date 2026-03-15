package org.leedtech.otp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.leedtech.otp.config.AuthContext;
import org.leedtech.otp.domain.User;
import org.leedtech.otp.service.AuthenticationService;
import org.leedtech.otp.service.UserService;
import org.leedtech.otp.utils.helperclasses.HelperDomain.*;
import org.leedtech.otp.utils.helperclasses.ResponseMessage;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


/**
 *
 * @author DB.Tech
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/secure")
@SecurityRequirement(name = "ApiKey")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "UserController", description = "This controller contains endpoints for users")
public class UserController {

    private final UserService userService;
    private final AuthenticationService authService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final AuthContext authContext;

    @ResponseStatus(HttpStatus.PARTIAL_CONTENT)
    @PutMapping(value = "/admin/update-user-role")
    @Operation(summary = "Assign New Role", description = "Change user's role using email", tags = { "ADMIN" })
    public ResponseMessage<User> updateUserRole(@RequestBody UpdateUserRole user) { return userService.changeUserRole(user.email(), user.role()); }

    @ResponseStatus(HttpStatus.PARTIAL_CONTENT)
    @PutMapping(value = "/admin/enable/users/{id}")
    @Operation(summary = "Enable Account", description = "Enable account", tags = { "ADMIN" })
    public ResponseMessage<User> enableUser(@PathVariable(name = "id") UUID id) { return userService.enableUser(id); }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/admin/users/{id}")
    @Operation(summary = "Get User Profile", description = "Get user's profile information using userId", tags = { "ADMIN" })
    public User getUserProfile(@PathVariable(name = "id") UUID id) { return userService.getUserProfile(id); }


    @GetMapping("/admin/users")
    @Operation(summary = "Get All Users", description = "Get all users in the system", tags = { "ADMIN" })
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @PostMapping("/admin/users")
    @Operation(summary = "Create User", description = "Create a new user", tags = { "ADMIN" })
    public ResponseEntity<AuthenticationResponse> creatUser(@RequestBody RegisterRequest request) {
        return new ResponseEntity<>(authService.register(request), HttpStatus.CREATED);
    }
}
