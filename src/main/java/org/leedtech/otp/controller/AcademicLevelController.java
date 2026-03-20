package org.leedtech.otp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.leedtech.otp.domain.AcademicLevel;
import org.leedtech.otp.service.AcademicLevelService;
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
@Tag(name = "AcademicLevelController", description = "This controller contains endpoints for academic level")
public class AcademicLevelController {

    private final AcademicLevelService academicLevel;

    @PostMapping(value = "/secure/admin/academic-levels")
    @Operation(summary = "CreateAcademic Levels", description = "Create new academic level", tags = { "ADMIN" })
    protected ResponseEntity<ResponseMessage<AcademicLevel>> createAcademicLevel(@RequestBody AcademicLevel academicLevel) {
        return new ResponseEntity<>(this.academicLevel.store(academicLevel), HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.PARTIAL_CONTENT)
    @PutMapping(value = "/secure/admin/academic-levels/{id}")
    @Operation(summary = "Update Academic Level", description = "Update academic level", tags = { "USER", "ADMIN" })
    public ResponseMessage<AcademicLevel> updateAcademicLevel(@PathVariable(name = "id") UUID id, @Valid @RequestBody AcademicLevel academicLevel) {
        return this.academicLevel.update(id, academicLevel);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/secure/user/academic-levels/{id}")
    @Operation(summary = "GetAcademic Level", description = "Get academic level", tags = { "USER", "ADMIN" })
    public AcademicLevel getAcademicLevel(@PathVariable(name = "id") UUID id) {
        return academicLevel.getAcademicLevel(id);
    }

   @GetMapping(value = "/academic-levels")
    @Operation(summary = "GetAcademic Levels", description = "Get all academic levels", tags = { "UNAUTHENTICATED" })
    public ResponseEntity<List<AcademicLevel>> getAcademicLevels() {
        return ResponseEntity.ok(academicLevel.getAcademicLevels());
    }

    @DeleteMapping(value = "/secure/admin/academic-levels/{id}")
    @Operation(summary = "DeleteAcademic Level", description = "Delete academic Level", tags = { "ADMIN" })
    public ResponseEntity<ResponseMessage<AcademicLevel>> deleteAcademicLevel(@PathVariable(name = "id") UUID id) {
        return ResponseEntity.ok(academicLevel.deleteAcademicLevel(id));
    }
}
