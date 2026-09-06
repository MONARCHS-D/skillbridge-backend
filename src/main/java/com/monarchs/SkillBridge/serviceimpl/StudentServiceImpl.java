package com.monarchs.SkillBridge.serviceimpl;

import com.monarchs.SkillBridge.dto.StudentUpdateDto;
import com.monarchs.SkillBridge.entities.Institution;
import com.monarchs.SkillBridge.entities.Student;
import com.monarchs.SkillBridge.repository.InstitutionRepository;
import com.monarchs.SkillBridge.repository.StudentRepository;
import com.monarchs.SkillBridge.response.StudentResponse;
import com.monarchs.SkillBridge.service.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepo;

    private final InstitutionRepository institutionRepo;

    public StudentServiceImpl(StudentRepository studentRepo, InstitutionRepository institutionRepo) {
        this.studentRepo = studentRepo;
        this.institutionRepo = institutionRepo;
    }

    @Override
    public StudentResponse getStudentDetailsService(Long id) {
        Student student=studentRepo.findById(id)
                .orElseThrow(()->new RuntimeException("Student details not available"));

        return StudentResponse.builder()
                .name(student.getName())
                .phone(student.getUser().getPhone())
                .institution(student.getInstitution().getName())
                .resume(student.getResumeUrl())
                .skills(student.getSkills())
                .certifications(student.getCertifications())
                .projects(student.getProjects())
                .build();
    }

    @Transactional
    @Override
    public String updateStudentService(Long id, StudentUpdateDto dto) {
        Student student=studentRepo.findById(id)
                .orElseThrow(()->new RuntimeException("Student details not available"));

        Institution institution=institutionRepo.findById(dto.getInstitution())
                        .orElseThrow(()->new RuntimeException("Institution not found"));

        student.getUser().setPhone(dto.getPhone());
        student.setInstitution(institution);
        student.setResumeUrl(dto.getResume());
        student.setSkills(dto.getSkills());
        student.setCertifications(dto.getCertifications());
        student.setProjects(dto.getProjects());

        studentRepo.save(student);

        return "Student data updated";
    }
}
