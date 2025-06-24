package com.ohgiraffers.no_injung.user.service;

import com.ohgiraffers.no_injung.auth.Repository.UserInfoResponseDTO;
import com.ohgiraffers.no_injung.auth.Response.UserInfoResponse;
import com.ohgiraffers.no_injung.user.entity.User;
import com.ohgiraffers.no_injung.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserInfoResponse getMyInfo(UserDetails userDetails) {
        if(userDetails == null){
            throw new RuntimeException("로그인이 필요합니다.");
        }

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        return new UserInfoResponse(user);
    }

}
