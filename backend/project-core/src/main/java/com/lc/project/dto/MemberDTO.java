package com.lc.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class MemberDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InviteRequest {
        private Long projectId;
        private Long userId;
        private String role;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRoleRequest {
        private String role;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberResponse {
        private Long id;
        private Long projectId;
        private Long userId;
        private String username;
        private String realName;
        private String email;
        private String role;
        private Integer status;
        private LocalDateTime joinedTime;
    }
}