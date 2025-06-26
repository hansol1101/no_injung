package com.ohgiraffers.no_injung.auth.Response;

import com.ohgiraffers.no_injung.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoResponse {

    private Long userId;  // userId 필드 추가
    private String email;
    private String nickname;

    public UserInfoResponse(User user) {
        this.userId = user.getUserId();  // userId 설정 추가
        this.email = user.getEmail();
        this.nickname = user.getNickname();
    }
}
