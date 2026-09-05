package com.monarchs.SkillBridge.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse<T> {
    private String serviceName;
    private boolean success;
    private String type;
    private int statusCode;
    private T payload;
    private LocalDateTime timeStamp;

    public static <T> ApiResponse<T> success(HttpStatus httpStatus, T payload){
        return ApiResponse.<T>builder()
                .serviceName("AUTH-SERVICE")
                .success(true)
                .type(payload!=null?payload.getClass().getSimpleName():"void")
                .statusCode(httpStatus.value())
                .payload(payload)
                .timeStamp(LocalDateTime.now())
                .build();
    }
}
