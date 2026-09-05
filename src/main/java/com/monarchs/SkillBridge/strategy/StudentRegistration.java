package com.monarchs.SkillBridge.strategy;

import com.monarchs.SkillBridge.dto.BaseUserDto;
import com.monarchs.SkillBridge.dto.StudentRegistrationDto;
import com.monarchs.SkillBridge.entities.Institution;
import com.monarchs.SkillBridge.entities.Student;
import com.monarchs.SkillBridge.entities.User;
import com.monarchs.SkillBridge.enums.UserRole;
import com.monarchs.SkillBridge.repository.EmbeddingVectorRepository;
import com.monarchs.SkillBridge.repository.InstitutionRepository;
import com.monarchs.SkillBridge.repository.StudentRepository;
import com.monarchs.SkillBridge.serviceimpl.EmbeddingService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class StudentRegistration implements RegistrationStrategy {

    private final InstitutionRepository institutionRepo;

    private final StudentRepository studentRepo;

    private final EmbeddingVectorRepository embeddingVectorRepo;

    private final EmbeddingService embeddingService;

    public StudentRegistration(InstitutionRepository institutionRepo, StudentRepository studentRepo, EmbeddingVectorRepository embeddingVectorRepo, EmbeddingService embeddingService) {
        this.institutionRepo = institutionRepo;
        this.studentRepo = studentRepo;
        this.embeddingVectorRepo = embeddingVectorRepo;
        this.embeddingService = embeddingService;
    }

    @Override
    public UserRole roleResolver() {
        return UserRole.STUDENT;
    }

    @Transactional
    @Override
    public void createProfile(BaseUserDto baseUserDto, User user) {
        StudentRegistrationDto dto=(StudentRegistrationDto) baseUserDto;

        Institution institution=institutionRepo.findById(dto.getInstitutionId())
                .orElseThrow(()->new RuntimeException("Invalid institution id"));

        Student student=Student.builder()
                .user(user)
                .institution(institution)
                .name(dto.getName())
                .resumeUrl(dto.getResumeUrl())
                .skills(dto.getSkills())
                .certifications(dto.getCertifications())
                .projects(dto.getProjects())
                .build();
        studentRepo.save(student);

        String profileText=embeddingService.buildStudentProfileText(
                student.getName(),
                student.getSkills(),
                student.getCertifications(),
                student.getProjects()

        );

        float[] embedding= embeddingService.generateEmbedding(profileText);

        embeddingVectorRepo.saveStudentEmbedding(student.getId(),embedding);
    }
}
