package com.monarchs.SkillBridge.strategy;

import com.monarchs.SkillBridge.dto.BaseUserDto;
import com.monarchs.SkillBridge.dto.CompanyRegistrationDto;
import com.monarchs.SkillBridge.entities.Company;
import com.monarchs.SkillBridge.entities.User;
import com.monarchs.SkillBridge.enums.UserRole;
import com.monarchs.SkillBridge.repository.CompanyRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CompanyRegistration implements RegistrationStrategy {

    private final CompanyRepository companyRepo;

    public CompanyRegistration(CompanyRepository companyRepo) {
        this.companyRepo = companyRepo;
    }

    @Override
    public UserRole roleResolver() {
        return UserRole.COMPANY;
    }

    @Transactional
    @Override
    public void createProfile(BaseUserDto baseUserDto, User user) {
        CompanyRegistrationDto dto=(CompanyRegistrationDto) baseUserDto;
        Company company= Company.builder()
                .user(user)
                .name(dto.getName())
                .website(dto.getWebsite())
                .contactEmail(dto.getContactEmail())
                .contactPhone(dto.getContactPhone())
                .build();
        companyRepo.save(company);
    }
}
