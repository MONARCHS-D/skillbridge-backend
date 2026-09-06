package com.monarchs.SkillBridge.service;

import com.monarchs.SkillBridge.response.ActivityRegistrationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ActivityRegistrationService {

    ActivityRegistrationResponse registerToActivity(Long activityId, Long actorUserId);

    Page<ActivityRegistrationResponse> listRegistrationsForActivity(Long activityId, Pageable pageable);

    Page<ActivityRegistrationResponse> listRegistrationsForUser(Long userId, Pageable pageable);

    ActivityRegistrationResponse updateRegistrationStatus(Long registrationId, String newStatus, Long actorUserId);
}
