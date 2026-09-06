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
public class ApplicationResponse {
    private Long id;
    private Long postingId;
    private Long studentId;
    private String status;
    private String resumeUrl;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
