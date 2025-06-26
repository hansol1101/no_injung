package com.ohgiraffers.no_injung.user.dto;

import lombok.*;

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
    private String email;
    private String password;
    private String nickname;

    // 수정용 생성자 (비밀번호 제외)
    public UserRequestDTO(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "UserRequestDTO{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", password='[PROTECTED]'" + // 보안상 비밀번호는 로그에 출력하지 않음
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
