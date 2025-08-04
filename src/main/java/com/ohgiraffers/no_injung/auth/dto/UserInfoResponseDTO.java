package com.ohgiraffers.no_injung.auth.dto;

import com.ohgiraffers.no_injung.user.entity.User;
import lombok.*;

/**
 * 사용자 정보 응답 DTO (간단한 버전)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoResponseDTO {
    private String id;

    public UserInfoResponseDTO(User user) {
        this.id = user.getNickname();
    }
}