package com.monarchs.SkillBridge.controller;

import com.monarchs.SkillBridge.dto.*;
import com.monarchs.SkillBridge.enums.UserRole;
import com.monarchs.SkillBridge.response.ApiResponse;
import com.monarchs.SkillBridge.service.UserRegistrationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/skillbridge/user")
public class UserRegistrationController {

    private final UserRegistrationService userRegistrationService;

    public UserRegistrationController(UserRegistrationService userRegistrationService) {
        this.userRegistrationService = userRegistrationService;
    }

    @PostMapping("/registration/student")
    public ResponseEntity<ApiResponse<String>> initiateStudentRegistrationController(@RequestBody @Valid StudentRegistrationDto dto){
        String response= userRegistrationService.initiateUserRegistrationService(dto, UserRole.STUDENT);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,response));
    }

    @PostMapping("/registration/academician")
    public ResponseEntity<ApiResponse<String>> initiateAcademicianRegistrationController(@RequestBody @Valid AcademicianRegistrationDto dto){
        String response= userRegistrationService.initiateUserRegistrationService(dto, UserRole.ACADEMICIAN);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,response));
    }

    @PostMapping("/registration/company")
    public ResponseEntity<ApiResponse<String>> initiateCompanyRegistrationController(@RequestBody @Valid CompanyRegistrationDto dto){
        String response= userRegistrationService.initiateUserRegistrationService(dto, UserRole.COMPANY);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,response));
    }

    @PostMapping("/registration/institution")
    public ResponseEntity<ApiResponse<String>> initiateInstitutionRegistrationController(@RequestBody @Valid InstitutionRegistrationDto dto){
        String response= userRegistrationService.initiateUserRegistrationService(dto, UserRole.INSTITUTION);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,response));
    }

    @PostMapping("/verification")
    public ResponseEntity<ApiResponse<String>> finalUserRegistrationController(@RequestBody @Valid OtpVerifyDto dto){
        String response= userRegistrationService.finalUserRegistrationService(dto);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,response));
    }
}
