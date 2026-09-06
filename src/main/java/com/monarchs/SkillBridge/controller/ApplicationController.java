package com.monarchs.SkillBridge.controller;

import com.monarchs.SkillBridge.dto.ApplicationStatusUpdateRequest;
import com.monarchs.SkillBridge.dto.ApplyRequest;
import com.monarchs.SkillBridge.response.ApiResponse;
import com.monarchs.SkillBridge.response.ApplicationResponse;
import com.monarchs.SkillBridge.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v7/skillbridge/application")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping("/apply")
    public ResponseEntity<ApiResponse<String>> applyController(Authentication authentication,@RequestBody @Valid ApplyRequest request){
        Long id=Long.valueOf(authentication.getName());
        String response= applicationService.applyService(id,request);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,response));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Page<ApplicationResponse>>> myApplications(Authentication authentication,
                                                                           @RequestParam(defaultValue = "0") int page,
                                                                           @RequestParam(defaultValue = "10") int size){
        Long id=Long.valueOf(authentication.getName());
        Page<ApplicationResponse> response=applicationService.listApplicationsForStudents(id, PageRequest.of(page,size));
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,response));
    }

    @GetMapping("/company")
    public ResponseEntity<ApiResponse<Page<ApplicationResponse>>> applicationsForCompany(Authentication authentication,
                                                                                         @RequestParam(defaultValue = "0") int page,
                                                                                         @RequestParam(defaultValue = "10") int size){
        Long id=Long.valueOf(authentication.getName());
        Page<ApplicationResponse> responses=applicationService.listApplicationsForCompany(id,PageRequest.of(page,size));
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,responses));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<String>> updateApplicationController(@PathVariable Long id,@RequestBody ApplicationStatusUpdateRequest request){
        String response= applicationService.updateStatus(id,request.getStatus());
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,response));
    }
}
