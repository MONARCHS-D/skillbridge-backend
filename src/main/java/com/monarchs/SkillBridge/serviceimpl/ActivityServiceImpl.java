package com.monarchs.SkillBridge.serviceimpl;

import com.monarchs.SkillBridge.dto.ActivityDto;
import com.monarchs.SkillBridge.entities.Activity;
import com.monarchs.SkillBridge.entities.Company;
import com.monarchs.SkillBridge.entities.Institution;
import com.monarchs.SkillBridge.entities.User;
import com.monarchs.SkillBridge.enums.UserRole;
import com.monarchs.SkillBridge.repository.ActivityRepository;
import com.monarchs.SkillBridge.repository.CompanyRepository;
import com.monarchs.SkillBridge.repository.InstitutionRepository;
import com.monarchs.SkillBridge.repository.UserRepository;
import com.monarchs.SkillBridge.response.ActivityResponse;
import com.monarchs.SkillBridge.service.ActivityService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepo;

    private final UserRepository userRepo;

    private final CompanyRepository companyRepo;

    private final InstitutionRepository institutionRepo;

    public ActivityServiceImpl(ActivityRepository activityRepo, UserRepository userRepo, CompanyRepository companyRepo, InstitutionRepository institutionRepo) {
        this.activityRepo = activityRepo;
        this.userRepo = userRepo;
        this.companyRepo = companyRepo;
        this.institutionRepo = institutionRepo;
    }

    @Transactional
    @Override
    public ActivityResponse createActivity(Long actorUserId, ActivityDto dto) {
        User user=userRepo.findById(actorUserId)
                .orElseThrow(()->new EntityNotFoundException("User not found"));

        boolean isCompany = user.getRole().name().contains(UserRole.COMPANY.name());
        boolean isInstitution = user.getRole().name().contains(UserRole.INSTITUTION.name());
        boolean isAdmin = user.getRole().name().contains(UserRole.ADMIN.name());

        Activity activity=Activity.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .activityType(dto.getActivityType())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .location(dto.getLocation())
                .registrationUrl(dto.getRegistrationUrl())
                .build();

        if(isCompany){
            Company company=companyRepo.findByUserId(actorUserId)
                    .orElseThrow(()->new IllegalStateException("No company found"));
            activity.setCompany(company);
        }

        if(isInstitution){
            Institution institution=institutionRepo.findByUserId(actorUserId)
                    .orElseThrow(()->new IllegalStateException("No Institution found"));
            activity.setHostInstitution(institution);
        }
        Activity saved=activityRepo.save(activity);
        return toDto(saved);
    }

    @Transactional
    @Override
    public ActivityResponse updateActivity(Long activityId, ActivityDto dto) {
         Activity activity=activityRepo.findById(activityId)
                 .orElseThrow(()->new EntityNotFoundException("Activity not found"));
         activity.setTitle(dto.getTitle());
         activity.setDescription(dto.getDescription());
         activity.setActivityType(dto.getActivityType());
         activity.setStartDate(dto.getStartDate());
         activity.setEndDate(dto.getEndDate());
         activity.setLocation(dto.getLocation());
         activity.setRegistrationUrl(dto.getRegistrationUrl());

         Activity saved=activityRepo.save(activity);
         return toDto(saved);
    }

    @Transactional
    @Override
    public String deleteActivity(Long activityId) {
        Activity activity=activityRepo.findById(activityId)
                .orElseThrow(()->new EntityNotFoundException("Activity not found"));
        activityRepo.delete(activity);
        return "Activity deleted successfully";
    }

    @Transactional(readOnly = true)
    @Override
    public ActivityResponse getActivity(Long id) {
        Activity activity=activityRepo.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Activity not found"));
        return toDto(activity);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ActivityResponse> listActivities(Pageable pageable) {
        return activityRepo.findAll(pageable).map(this::toDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ActivityResponse> listActivitiesForCompany(Long companyId, Pageable pageable) {
        return activityRepo.findByCompanyId(companyId,pageable).map(this::toDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ActivityResponse> listActivitiesForInstitution(Long institutionId, Pageable pageable) {
        return activityRepo.findByHostInstitutionId(institutionId,pageable).map(this::toDto);
    }


    private ActivityResponse toDto(Activity a) {
        return ActivityResponse.builder()
                .id(a.getId())
                .hostCompanyId(a.getCompany() != null ? a.getCompany().getId() : null)
                .hostInstitutionId(a.getHostInstitution() != null ? a.getHostInstitution().getId() : null)
                .title(a.getTitle())
                .description(a.getDescription())
                .activityType(a.getActivityType())
                .startDate(a.getStartDate())
                .endDate(a.getEndDate())
                .location(a.getLocation())
                .registrationUrl(a.getRegistrationUrl())
                .createdAt(a.getCreatedAt())
                .build();
    }
}
