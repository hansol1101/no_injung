package com.ohgiraffers.no_injung.user.dto;

import com.ohgiraffers.no_injung.user.entity.Role;
import lombok.*;

import java.time.LocalDate;

/**
 * 사용자 요청 DTO - 클라이언트로부터 받는 사용자 정보
 * 생성, 수정 요청 시 사용
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDTO {

    private Long userId;
    private String password;
    private String nickname;
    private LocalDate birthDate;
    private Role role; // 관리자만 수정 가능

    // 수정용 생성자 (비밀번호 제외)
    public UserRequestDTO(String nickname, LocalDate birthDate) {
        this.nickname = nickname;
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return "UserRequestDTO{" +
                "userId=" + userId +
                ", password='[PROTECTED]'" + // 보안상 비밀번호는 로그에 출력하지 않음
                ", nickname='" + nickname + '\'' +
                ", birthDate=" + birthDate +
                ", role=" + role +
                '}';
    }
}
