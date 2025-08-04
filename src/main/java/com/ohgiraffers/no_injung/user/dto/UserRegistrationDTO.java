package com.ohgiraffers.no_injung.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * 회원가입 전용 DTO - Role 필드 없음 (보안상 이유)
 * 일반 사용자는 회원가입 시 권한을 선택할 수 없음
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegistrationDTO {

    @NotBlank(message = "이메일은 필수 항목입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @Size(max = 100, message = "이메일은 100자를 초과할 수 없습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    @Size(min = 6, max = 255, message = "비밀번호는 6자 이상 255자 이하여야 합니다.")
    private String password;

    @NotBlank(message = "닉네임은 필수 항목입니다.")
    @Size(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이하여야 합니다.")
    private String id;

    @Override
    public String toString() {
        return "UserRegistrationDTO{" +
                "email='" + email + '\'' +
                ", password='[PROTECTED]'" + // 보안상 비밀번호는 로그에 출력하지 않음
                ", id='" + id + '\'' +
                '}';
    }
}
