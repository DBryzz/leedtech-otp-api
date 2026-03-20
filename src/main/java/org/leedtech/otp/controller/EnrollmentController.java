package org.leedtech.otp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.leedtech.otp.domain.Enrollment;
import org.leedtech.otp.service.EnrollmentService;
import org.leedtech.otp.utils.helperclasses.HelperDomain.*;
import org.leedtech.otp.utils.helperclasses.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/secure")
@SecurityRequirement(name = "ApiKey")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "EnrollmentController", description = "This controller contains endpoints for enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    @PostMapping(value = "/student/enrollments")
    @Operation(summary = "Create Enrollments", description = "Create new enrollment", tags = { "STUDENT" })
    protected ResponseEntity<Enrollment> createEnrollment(@RequestBody EnrollmentRequest enrollmentRequest) {
        return new ResponseEntity<>(enrollmentService.enroll(enrollmentRequest), HttpStatus.CREATED);
    }

    @PostMapping(value = "/admin/enrollments/user/{userId}")
    @Operation(summary = "Subscribe User", description = "Add a user to a academicYear", tags = { "ADMIN" })
    public ResponseEntity<Enrollment> subscribeUser(@PathVariable(name = "userId") UUID userId, @RequestBody EnrollmentRequest enrollmentRequest) {
        return new ResponseEntity<>(enrollmentService.enrollUser(userId, enrollmentRequest), HttpStatus.PARTIAL_CONTENT);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/student/enrollments/{id}")
    @Operation(summary = "Get Enrollment", description = "Get enrollment", tags = { "STUDENT", "ADMIN" })
    public Enrollment getEnrollment(@PathVariable(name = "id") UUID id) {
        return enrollmentService.getEnrollment(id);
    }

    @GetMapping(value = "/student/enrollments")
    @Operation(summary = "Get Enrollments", description = "Get all enrollments", tags = { "STUDENT", "ADMIN" })
    public ResponseEntity<List<Enrollment>> getEnrollments(@RequestParam("isOngoing") boolean isOngoing) {
        return ResponseEntity.ok(enrollmentService.getEnrollments(isOngoing));
    }



}
