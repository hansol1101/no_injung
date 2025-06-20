package com.ohgiraffers.no_injung.auth.service;


import com.ohgiraffers.no_injung.auth.Request.SignUpRequest;
import com.ohgiraffers.no_injung.user.entity.User;
import com.ohgiraffers.no_injung.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class SignUpService {

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    UserRepository userRepository;
    public void signup(SignUpRequest request) {
        if(userRepository.existsByEmail(request.getEmail())){
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }
        if(userRepository.existsByNickname(request.getNickname())){
            throw new IllegalArgumentException("이미 사용중인 닉네임입니다.");
        }
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .build();

        userRepository.save(user);
    }
}
