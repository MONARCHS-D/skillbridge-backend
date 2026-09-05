package com.monarchs.SkillBridge.strategy;

import com.monarchs.SkillBridge.dto.AcademicianRegistrationDto;
import com.monarchs.SkillBridge.dto.BaseUserDto;
import com.monarchs.SkillBridge.entities.Academician;
import com.monarchs.SkillBridge.entities.Institution;
import com.monarchs.SkillBridge.entities.User;
import com.monarchs.SkillBridge.enums.UserRole;
import com.monarchs.SkillBridge.repository.AcademicianRepository;
import com.monarchs.SkillBridge.repository.InstitutionRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AcademicianRegistration implements RegistrationStrategy{

    private final InstitutionRepository institutionRepo;

    private final AcademicianRepository academicianRepo;

    public AcademicianRegistration(InstitutionRepository institutionRepo, AcademicianRepository academicianRepo) {
        this.institutionRepo = institutionRepo;
        this.academicianRepo = academicianRepo;
    }

    @Override
    public UserRole roleResolver() {
        return UserRole.ACADEMICIAN;
    }

    @Transactional
    @Override
    public void createProfile(BaseUserDto baseUserDto, User user) {
        AcademicianRegistrationDto dto=(AcademicianRegistrationDto) baseUserDto;

        Institution institution=institutionRepo.findById(dto.getInstitutionId())
                .orElseThrow(()->new RuntimeException("Invalid institution id"));

        Academician academician= Academician.builder()
                .user(user)
                .institution(institution)
                .name(dto.getName())
                .areaOfInterest(dto.getAreaOfInterest())
                .designation(dto.getDesignation())
                .build();
        academicianRepo.save(academician);
    }
}
