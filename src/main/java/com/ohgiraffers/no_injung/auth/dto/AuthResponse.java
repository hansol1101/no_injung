package com.ohgiraffers.no_injung.auth.dto;

import lombok.*;

/**
 * 인증 응답 DTO - 로그인 성공 시 반환되는 정보
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String token;
    private String nickname; // 로그인용 아이디
    private String role;
}