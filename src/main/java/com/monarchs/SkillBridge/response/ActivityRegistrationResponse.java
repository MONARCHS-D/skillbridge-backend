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
public class ActivityRegistrationResponse {
    private Long id;
    private Long activityId;
    private Long userId;
    private String status;
    private OffsetDateTime registeredAt;
}
