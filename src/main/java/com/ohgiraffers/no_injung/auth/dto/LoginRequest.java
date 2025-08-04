package com.ohgiraffers.no_injung.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * 로그인 요청 DTO - 닉네임을 로그인용 아이디로 사용
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {
    
    @NotBlank(message = "닉네임은 필수 항목입니다.")
    private String nickname; // 로그인용 아이디
    
    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    private String password;

    @Override
    public String toString() {
        return "LoginRequest{" +
                "nickname='" + nickname + '\'' +
                ", password='[PROTECTED]'" + // 보안상 비밀번호는 로그에 출력하지 않음
                '}';
    }
}
