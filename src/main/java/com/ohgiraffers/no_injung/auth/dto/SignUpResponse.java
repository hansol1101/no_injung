package com.ohgiraffers.no_injung.auth.dto;

import lombok.*;

/**
 * 회원가입 응답 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpResponse {
    private Long id;
    private String email;
    private String userId;
    private String message;
}