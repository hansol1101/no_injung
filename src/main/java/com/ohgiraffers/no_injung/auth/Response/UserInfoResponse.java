package com.ohgiraffers.no_injung.auth.Response;

import com.ohgiraffers.no_injung.user.entity.Role;
import com.ohgiraffers.no_injung.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoResponse {

    private Long userId;
    private String email;
    private String nickname;
    private Role role;

    public UserInfoResponse(User user) {
        this.userId = user.getUserId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.role = user.getRole();
    }
}
