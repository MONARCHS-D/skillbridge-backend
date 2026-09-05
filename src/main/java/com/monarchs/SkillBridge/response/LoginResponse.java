package com.monarchs.SkillBridge.response;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private String token;
    private String username;
    private String email;
    private String role;
    private Long userId;
}
