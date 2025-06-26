package com.ohgiraffers.no_injung.auth.controller;

import com.ohgiraffers.no_injung.auth.Request.LoginRequest;
import com.ohgiraffers.no_injung.auth.Request.SignUpRequest;
import com.ohgiraffers.no_injung.auth.Response.AuthResponse;
import com.ohgiraffers.no_injung.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody SignUpRequest request) {
        authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).build(); // 바디 없음
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
