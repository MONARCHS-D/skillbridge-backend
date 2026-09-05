package com.monarchs.SkillBridge.event;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailEvent {
    private String receiverEmail;
    private String subject;
    private String message;
}
