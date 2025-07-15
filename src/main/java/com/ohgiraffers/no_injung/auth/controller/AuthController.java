package com.ohgiraffers.no_injung.auth.controller;

import com.ohgiraffers.no_injung.auth.dto.LoginRequest;
import com.ohgiraffers.no_injung.auth.dto.SignUpRequest;
import com.ohgiraffers.no_injung.auth.dto.AuthResponse;
import com.ohgiraffers.no_injung.auth.jwt.JwtTokenProvider;
import com.ohgiraffers.no_injung.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 인증 컨트롤러 - 강화된 기능
 * 회원가입, 로그인, 로그아웃, 실시간 중복 검증 등을 제공
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "인증", description = "회원가입, 로그인, 로그아웃 및 실시간 검증 API")
public class AuthController {

    private final AuthService authService;
    private final RedisTemplate<String, String> redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 회원가입
     * 강화된 검증과 에러 처리 포함
     */
    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다. 이메일, 닉네임, 생년월일의 중복 검증을 포함합니다.")
    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signup(@Valid @RequestBody SignUpRequest request) {
        log.info("회원가입 요청: {}", request.toString());
        
        try {
            authService.signup(request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "회원가입이 완료되었습니다.");
            response.put("email", request.getEmail());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (RuntimeException e) {
            log.error("회원가입 실패: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 로그인
     * 보안 강화된 로그인 처리
     */
    @Operation(summary = "로그인", description = "사용자 로그인을 처리하고 JWT 토큰을 반환합니다.")
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
        log.info("로그인 요청: {}", request.getEmail());
        
        try {
            AuthResponse authResponse = authService.login(request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "로그인 성공");
            response.put("token", authResponse.getToken());
            response.put("email", authResponse.getEmail());
            response.put("nickname", authResponse.getNickname());
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            log.error("로그인 실패: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    /**
     * 로그아웃
     * Redis를 활용한 토큰 블랙리스트 처리
     */
    @Operation(summary = "로그아웃", description = "사용자 로그아웃을 처리하고 토큰을 무효화합니다.")
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        
        Map<String, Object> response = new HashMap<>();
        
        if (token != null && jwtTokenProvider.validateToken(token)) {
            try {
                long expiration = jwtTokenProvider.getExpiration(token);
                redisTemplate.opsForValue().set(token, "logout", expiration, TimeUnit.MILLISECONDS);
                
                String email = jwtTokenProvider.getEmail(token);
                log.info("로그아웃 성공: {}", email);
                
                response.put("status", "success");
                response.put("message", "로그아웃이 완료되었습니다.");
                
                return ResponseEntity.ok(response);
                
            } catch (Exception e) {
                log.error("로그아웃 처리 중 오류: {}", e.getMessage());
                
                response.put("status", "error");
                response.put("message", "로그아웃 처리 중 오류가 발생했습니다.");
                
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } else {
            response.put("status", "error");
            response.put("message", "유효하지 않은 토큰입니다.");
            
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 이메일 중복 확인
     * 실시간 검증용 API
     */
    @Operation(summary = "이메일 중복 확인", description = "회원가입 시 이메일 중복 여부를 실시간으로 확인합니다.")
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Object>> checkEmailAvailability(
            @Parameter(description = "확인할 이메일 주소", example = "user@example.com")
            @RequestParam String email) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean isAvailable = authService.isEmailAvailable(email);
            
            response.put("email", email);
            response.put("available", isAvailable);
            response.put("message", isAvailable ? "사용 가능한 이메일입니다." : "이미 사용 중인 이메일입니다.");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("이메일 중복 확인 중 오류: {}", e.getMessage());
            
            response.put("status", "error");
            response.put("message", "이메일 확인 중 오류가 발생했습니다.");
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 닉네임 중복 확인
     * 실시간 검증용 API
     */
    @Operation(summary = "닉네임 중복 확인", description = "회원가입 시 닉네임 중복 여부를 실시간으로 확인합니다.")
    @GetMapping("/check-nickname")
    public ResponseEntity<Map<String, Object>> checkNicknameAvailability(
            @Parameter(description = "확인할 닉네임", example = "홍길동")
            @RequestParam String nickname) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean isAvailable = authService.isNicknameAvailable(nickname);
            
            response.put("nickname", nickname);
            response.put("available", isAvailable);
            response.put("message", isAvailable ? "사용 가능한 닉네임입니다." : "이미 사용 중인 닉네임입니다.");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("닉네임 중복 확인 중 오류: {}", e.getMessage());
            
            response.put("status", "error");
            response.put("message", "닉네임 확인 중 오류가 발생했습니다.");
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 토큰 유효성 확인
     * 클라이언트에서 토큰 상태 확인용
     */
    @Operation(summary = "토큰 유효성 확인", description = "현재 JWT 토큰의 유효성을 확인합니다.")
    @GetMapping("/validate-token")
    public ResponseEntity<Map<String, Object>> validateToken(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        
        Map<String, Object> response = new HashMap<>();
        
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 블랙리스트 확인
            String isLogout = redisTemplate.opsForValue().get(token);
            if (isLogout != null) {
                response.put("valid", false);
                response.put("message", "로그아웃된 토큰입니다.");
                
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            
            String email = jwtTokenProvider.getEmail(token);
            String role = jwtTokenProvider.getRole(token);
            
            response.put("valid", true);
            response.put("email", email);
            response.put("role", role);
            response.put("message", "유효한 토큰입니다.");
            
            return ResponseEntity.ok(response);
        } else {
            response.put("valid", false);
            response.put("message", "유효하지 않은 토큰입니다.");
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
