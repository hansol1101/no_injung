package com.ohgiraffers.no_injung.auth.dto;

import com.ohgiraffers.no_injung.user.entity.Role;
import com.ohgiraffers.no_injung.user.entity.User;
import lombok.*;

/**
 * 사용자 정보 응답 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoResponse {
    private Long userId;
    private String nickname;
    private Role role;

    public UserInfoResponse(User user) {
        this.userId = user.getUserId();
        this.nickname = user.getNickname();
        this.role = user.getRole();
    }
}