package com.ohgiraffers.no_injung.auth.service;


import com.ohgiraffers.no_injung.auth.Request.LoginRequest;
import com.ohgiraffers.no_injung.auth.Response.AuthResponse;
import com.ohgiraffers.no_injung.auth.jwt.JwtTokenProvider;
import com.ohgiraffers.no_injung.user.entity.User;
import com.ohgiraffers.no_injung.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

public class LoginService {

    UserRepository userRepository;
    JwtTokenProvider jwtTokenProvider;
    PasswordEncoder passwordEncoder;

    public AuthResponse login(LoginRequest request){
        User user = userRepository.findByEmailAndIsDeletedFalse(request.getEmail())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtTokenProvider.createToken(user.getEmail());
        return new AuthResponse(token, user.getEmail(), user.getNickname());
    }
}
