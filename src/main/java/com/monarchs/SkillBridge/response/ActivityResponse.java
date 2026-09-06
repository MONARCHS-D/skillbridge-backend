package com.monarchs.SkillBridge.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityResponse {
    private Long id;
    private Long hostCompanyId;
    private Long hostInstitutionId;
    private String title;
    private String description;
    private String activityType;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private String location;
    private String registrationUrl;
    private OffsetDateTime createdAt;
}
