package com.monarchs.SkillBridge.service;

import com.monarchs.SkillBridge.dto.ApplyRequest;
import com.monarchs.SkillBridge.response.ApplicationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApplicationService {

    String applyService(Long studentId, ApplyRequest request);

    Page<ApplicationResponse> listApplicationsForStudents(Long studentId, Pageable pageable);

    Page<ApplicationResponse> listApplicationsForCompany(Long companyId,Pageable pageable);

    String updateStatus(Long applicationId,String newStatus);
}
