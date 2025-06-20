package com.ohgiraffers.no_injung.auth.service;


import com.ohgiraffers.no_injung.auth.Request.LoginRequest;
import com.ohgiraffers.no_injung.auth.Request.SignUpRequest;
import com.ohgiraffers.no_injung.auth.Response.AuthResponse;
import com.ohgiraffers.no_injung.auth.jwt.JwtTokenProvider;
import com.ohgiraffers.no_injung.user.entity.User;
import com.ohgiraffers.no_injung.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder; //비밀번호를 암호화 후 저장
    @Autowired private JwtTokenProvider jwtTokenProvider;

    public void signup(SignUpRequest request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exist");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNickname(request.getNickname());
        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtTokenProvider.createToken(user.getEmail());
        return new AuthResponse(token, user.getEmail(), user.getNickname());
    }
}
