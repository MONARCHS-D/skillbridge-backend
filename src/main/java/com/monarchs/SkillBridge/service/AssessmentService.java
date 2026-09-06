package com.monarchs.SkillBridge.service;

import com.monarchs.SkillBridge.dto.AssessmentRequest;
import com.monarchs.SkillBridge.response.AssessmentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AssessmentService {

    AssessmentResponse submitAssessment(Long id, AssessmentRequest request);
    Page<AssessmentResponse> getAssessmentsForStudent(Long studentId, Pageable pageable);
    AssessmentResponse getAssessment(Long id);
}
