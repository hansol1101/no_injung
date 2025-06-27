package com.ohgiraffers.no_injung.user.dto;

import com.ohgiraffers.no_injung.user.entity.Role;
import com.ohgiraffers.no_injung.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 사용자 응답 DTO - 클라이언트에게 반환할 사용자 정보
 * 보안상 비밀번호는 제외
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {
    
    private Long userId;
    private String email;
    private String nickname;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Entity에서 DTO로 변환하는 정적 팩토리 메서드
    public static UserResponseDTO fromEntity(User user) {
        return UserResponseDTO.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
