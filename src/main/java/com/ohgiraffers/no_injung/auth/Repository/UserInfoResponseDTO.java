package com.ohgiraffers.no_injung.auth.Repository;


import com.ohgiraffers.no_injung.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoResponseDTO {
    private String email;
    private String nickname;

    public UserInfoResponseDTO(User user) {
        this.email = user.getEmail();
        this.nickname = user.getNickname();
    }
}
