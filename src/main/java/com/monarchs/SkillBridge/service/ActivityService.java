package com.monarchs.SkillBridge.service;

import com.monarchs.SkillBridge.dto.ActivityDto;
import com.monarchs.SkillBridge.response.ActivityResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ActivityService {

    ActivityResponse createActivity(Long actorUserId, ActivityDto dto);

    ActivityResponse updateActivity(Long activityId, ActivityDto dto);

    String deleteActivity(Long activityId);

    ActivityResponse getActivity(Long id);

    Page<ActivityResponse> listActivities(Pageable pageable);

    Page<ActivityResponse> listActivitiesForCompany(Long companyId, Pageable pageable);

    Page<ActivityResponse> listActivitiesForInstitution(Long institutionId, Pageable pageable);
}
