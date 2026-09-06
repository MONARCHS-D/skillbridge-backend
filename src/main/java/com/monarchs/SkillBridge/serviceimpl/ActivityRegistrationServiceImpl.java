package com.monarchs.SkillBridge.serviceimpl;

import com.monarchs.SkillBridge.entities.Activity;
import com.monarchs.SkillBridge.entities.ActivityRegistration;
import com.monarchs.SkillBridge.entities.User;
import com.monarchs.SkillBridge.repository.ActivityRegistrationRepository;
import com.monarchs.SkillBridge.repository.ActivityRepository;
import com.monarchs.SkillBridge.repository.UserRepository;
import com.monarchs.SkillBridge.response.ActivityRegistrationResponse;
import com.monarchs.SkillBridge.service.ActivityRegistrationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ActivityRegistrationServiceImpl implements ActivityRegistrationService {

    private final ActivityRegistrationRepository activityRegistrationRepo;

    private final UserRepository userRepo;

    private final ActivityRepository activityRepo;

    public ActivityRegistrationServiceImpl(ActivityRegistrationRepository activityRegistrationRepo, UserRepository userRepo, ActivityRepository activityRepo) {
        this.activityRegistrationRepo = activityRegistrationRepo;
        this.userRepo = userRepo;
        this.activityRepo = activityRepo;
    }

    @Override
    public ActivityRegistrationResponse registerToActivity(Long activityId, Long actorUserId) {
        Activity activity=activityRepo.findById(activityId)
                .orElseThrow(()->new EntityNotFoundException("Activity not found"));
        User user=userRepo.findById(actorUserId)
                .orElseThrow(()->new EntityNotFoundException("User not found"));

        if(activityRegistrationRepo.existsByActivityIdAndUserId(activityId,actorUserId)){
            throw new IllegalStateException("User already registered for this activity");
        }
        ActivityRegistration reg = ActivityRegistration.builder()
                .activity(activity)
                .user(user)
                .build();
        ActivityRegistration saved=activityRegistrationRepo.save(reg);
        return toRegDto(saved);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ActivityRegistrationResponse> listRegistrationsForActivity(Long activityId, Pageable pageable) {
        return activityRegistrationRepo.findByActivityId(activityId,pageable).map(this::toRegDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ActivityRegistrationResponse> listRegistrationsForUser(Long userId, Pageable pageable) {
        return activityRegistrationRepo.findByUserId(userId,pageable).map(this::toRegDto);
    }

    @Override
    public ActivityRegistrationResponse updateRegistrationStatus(Long registrationId, String newStatus, Long actorUserId) {
        ActivityRegistration reg = activityRegistrationRepo.findById(registrationId)
                .orElseThrow(() -> new EntityNotFoundException("Registration not found: " + registrationId));

        reg.setStatus(newStatus);
        ActivityRegistration saved = activityRegistrationRepo.save(reg);
        return toRegDto(saved);
    }

    private ActivityRegistrationResponse toRegDto(ActivityRegistration r) {
        return ActivityRegistrationResponse.builder()
                .id(r.getId())
                .activityId(r.getActivity() != null ? r.getActivity().getId() : null)
                .userId(r.getUser() != null ? r.getUser().getId() : null)
                .status(r.getStatus())
                .registeredAt(r.getRegisteredAt())
                .build();
    }
}
