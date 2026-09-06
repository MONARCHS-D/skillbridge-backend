package com.monarchs.SkillBridge.controller;

import com.monarchs.SkillBridge.dto.AssessmentRequest;
import com.monarchs.SkillBridge.response.ApiResponse;
import com.monarchs.SkillBridge.response.AssessmentResponse;
import com.monarchs.SkillBridge.service.AssessmentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v8/skillbridge/assesssment")
public class AssessmentController {

    private final AssessmentService assessmentService;

    public AssessmentController(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }

    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<AssessmentResponse>> submitAssessmentController(Authentication authentication, @RequestBody @Valid AssessmentRequest request){
        Long id=Long.valueOf(authentication.getName());
        AssessmentResponse response=assessmentService.submitAssessment(id,request);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,response));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Page<AssessmentResponse>>> myAssessmentsController(Authentication authentication,
                                                                               @RequestParam(defaultValue = "0") int page,
                                                                               @RequestParam(defaultValue = "10") int size){
        Long id=Long.valueOf(authentication.getName());
        Page<AssessmentResponse> responses=assessmentService.getAssessmentsForStudent(id, PageRequest.of(page,size));
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,responses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AssessmentResponse>> getAssessmentController(@PathVariable Long id){
        AssessmentResponse response=assessmentService.getAssessment(id);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,response));
    }
}
