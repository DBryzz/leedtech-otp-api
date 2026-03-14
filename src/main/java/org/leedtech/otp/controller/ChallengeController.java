package org.leedtech.otp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/api/v1")
@SecurityRequirement(name = "ApiKey")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "ChallengeController", description = "This controller contains endpoints for challenges")
public class ChallengeController {

    private final org.leedtech.otp.service.AcademicLevel academicLevel;
    @PostMapping(value = "/secure/admin/challenges")
    @Operation(summary = "Create Challenges", description = "Create new challenge", tags = { "ADMIN" })
    protected ResponseEntity<ResponseMessage<AcademicLevel>> createChallenge(@RequestBody AcademicLevel academicLevel) {
        return new ResponseEntity<>(this.academicLevel.store(academicLevel), HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.PARTIAL_CONTENT)
    @PutMapping(value = "/secure/admin/challenges/{id}")
    @Operation(summary = "Update Challenge", description = "Update challenge", tags = { "USER", "ADMIN" })
    public ResponseMessage<AcademicLevel> updateChallenge(@PathVariable(name = "id") UUID id, @RequestBody AcademicLevel academicLevel) {
        return this.academicLevel.update(id, academicLevel);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/secure/user/challenges/{id}")
    @Operation(summary = "Get Challenge", description = "Get challenge", tags = { "USER", "ADMIN" })
    public AcademicLevel getChallenge(@PathVariable(name = "id") UUID id) {
        return academicLevel.getChallenge(id);
    }

   @GetMapping(value = "/challenges")
    @Operation(summary = "Get Challenges", description = "Get all challenges", tags = { "UNAUTHENTICATED" })
    public ResponseEntity<List<AcademicLevel>> getChallenges() {
        return ResponseEntity.ok(academicLevel.getChallenges());
    }


    @PutMapping(value = "/secure/admin/challenges/{id}/type")
    @Operation(summary = "Update Challenge Type", description = "Update challenge's Type. valid values {NORMAL, EVENT}", tags = { "ADMIN" })
    public ResponseEntity<ResponseMessage<AcademicLevel>> changeChallengeStatus(@PathVariable(name = "id") UUID id, @RequestBody RequestProps props) {
        return new ResponseEntity<>(academicLevel.changeType(id, props.type()), HttpStatus.PARTIAL_CONTENT);
    }

    @DeleteMapping(value = "/secure/admin/challenges/{id}")
    @Operation(summary = "Delete Challenge", description = "Delete Challenge", tags = { "ADMIN" })
    public ResponseEntity<ResponseMessage<AcademicLevel>> deleteChallenge(@PathVariable(name = "id") UUID id) {
        return ResponseEntity.ok(academicLevel.deleteChallenge(id));
    }
}
