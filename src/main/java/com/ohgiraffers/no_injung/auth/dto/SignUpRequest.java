package com.ohgiraffers.no_injung.auth.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

/**
 * 회원가입 요청 DTO - 닉네임을 로그인용 아이디로 사용
 * 보안상 Role 필드 없음 (일반 사용자는 회원가입 시 권한 선택 불가)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpRequest {

    @NotBlank(message = "닉네임은 필수 항목입니다.")
    @Size(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이하여야 합니다.")
    private String nickname; // 로그인용 아이디

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    @Size(min = 6, max = 255, message = "비밀번호는 6자 이상 255자 이하여야 합니다.")
    private String password;

    @NotNull(message = "생년월일은 필수 항목입니다.")
    @Past(message = "생년월일은 현재 날짜보다 이전이어야 합니다.")
    private LocalDate birthdate; //yyyy-MM-dd 형식으로 받음

    @Override
    public String toString() {
        return "SignUpRequest{" +
                "nickname='" + nickname + '\'' +
                ", password='[PROTECTED]'" + // 보안상 비밀번호는 로그에 출력하지 않음
                ", birthdate=" + birthdate +
                '}';
    }
}
