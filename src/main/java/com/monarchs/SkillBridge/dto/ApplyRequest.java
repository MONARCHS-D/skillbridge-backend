package com.monarchs.SkillBridge.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplyRequest {

    private Long postingId;

    private String resume;

}
