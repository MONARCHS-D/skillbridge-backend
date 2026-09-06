package com.monarchs.SkillBridge.serviceimpl;

import com.monarchs.SkillBridge.dto.ApplyRequest;
import com.monarchs.SkillBridge.entities.Application;
import com.monarchs.SkillBridge.entities.Posting;
import com.monarchs.SkillBridge.entities.Student;
import com.monarchs.SkillBridge.repository.ApplicationRepository;
import com.monarchs.SkillBridge.repository.PostingRepository;
import com.monarchs.SkillBridge.repository.StudentRepository;
import com.monarchs.SkillBridge.response.ApplicationResponse;
import com.monarchs.SkillBridge.service.ApplicationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepo;

    private final PostingRepository postingRepo;

    private final StudentRepository studentRepo;

    public ApplicationServiceImpl(ApplicationRepository applicationRepo, PostingRepository postingRepo, StudentRepository studentRepo) {
        this.applicationRepo = applicationRepo;
        this.postingRepo = postingRepo;
        this.studentRepo = studentRepo;
    }

    @Override
    public String applyService(Long studentId, ApplyRequest request) {
        Student student=studentRepo.findByUserId(studentId)
                .orElseThrow(()->new RuntimeException("Student id not found"));

        Posting posting=postingRepo.findById(request.getPostingId())
                .orElseThrow(()->new RuntimeException("Posting id not found"));

        if(applicationRepo.existsByPostingIdAndStudentId(studentId, request.getPostingId())){
            throw new IllegalStateException("Student already applied to this posting!");
        }

        Application application=Application.builder()
                .student(student)
                .posting(posting)
                .resume_url(request.getResume())
                .build();
        applicationRepo.save(application);
        return "Applied successfully";
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ApplicationResponse> listApplicationsForStudents(Long studentId, Pageable pageable) {
        return applicationRepo.findByStudentId(studentId,pageable)
                .map(this::toDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ApplicationResponse> listApplicationsForCompany(Long companyId, Pageable pageable) {
        return applicationRepo.findByPostingCompanyId(companyId, pageable)
                .map(this::toDto);
    }


    @Override
    public String updateStatus(Long applicationId, String newStatus) {
        Application application=applicationRepo.findById(applicationId)
                .orElseThrow(()->new EntityNotFoundException("Application not found"));

        application.setStatus(newStatus);
        applicationRepo.save(application);
        return "Application status updated";
    }

    private ApplicationResponse toDto(Application a) {
        return ApplicationResponse.builder()
                .id(a.getId())
                .postingId(a.getPosting().getId())
                .studentId(a.getStudent().getId())
                .status(a.getStatus())
                .resumeUrl(a.getResume_url())
                .createdAt(a.getCreatedAt())
                .updatedAt(a.getUpdatedAt())
                .build();
    }
}
