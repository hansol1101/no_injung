package com.ohgiraffers.no_injung.auth.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

/**
 * 회원가입 요청 DTO - 강화된 Validation 포함
 * 보안상 Role 필드 없음 (일반 사용자는 회원가입 시 권한 선택 불가)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpRequest {

    @NotBlank(message = "이메일은 필수 항목입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @Size(max = 100, message = "이메일은 100자를 초과할 수 없습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    @Size(min = 6, max = 255, message = "비밀번호는 6자 이상 255자 이하여야 합니다.")
    private String password;

    @NotBlank(message = "닉네임은 필수 항목입니다.")
    @Size(min = 2, max = 50, message = "닉네임은 2자 이상 50자 이하여야 합니다.")
    private String nickname;

    @NotNull(message = "생년월일은 필수 항목입니다.")
    @Past(message = "생년월일은 현재 날짜보다 이전이어야 합니다.")
    private LocalDate birthdate; //yyyy-MM-dd 형식으로 받음

    @Override
    public String toString() {
        return "SignUpRequest{" +
                "email='" + email + '\'' +
                ", password='[PROTECTED]'" + // 보안상 비밀번호는 로그에 출력하지 않음
                ", nickname='" + nickname + '\'' +
                ", birthDate=" + birthdate + // toString()에도 추가
                '}';
    }
}
