package com.monarchs.SkillBridge.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityDto {

    private String title;
    private String description;
    private String activityType;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private String location;
    private String registrationUrl;
}
