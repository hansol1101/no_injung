package com.ohgiraffers.no_injung.user.controller;

import com.ohgiraffers.no_injung.auth.Response.UserInfoResponse;
import com.ohgiraffers.no_injung.user.dto.UserRegistrationDTO;
import com.ohgiraffers.no_injung.user.dto.UserRequestDTO;
import com.ohgiraffers.no_injung.user.dto.UserResponseDTO;
import com.ohgiraffers.no_injung.user.repository.UserRepository;
import com.ohgiraffers.no_injung.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize; // 임시 비활성화
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    /**
     * 기존 메서드 - 내 정보 조회 (일반 사용자 가능)
     */
    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> getMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userService.getMyInfo(userDetails));
    }

    /**
     * 단일 사용자 조회 (ID로 조회) - 인증 제거 (개발용)
     */
    @GetMapping("/{userId}")
    // @PreAuthorize("hasRole('ADMIN')") // 임시 비활성화
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long userId) {
        try {
            UserResponseDTO user = userService.getUserById(userId);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 전체 사용자 조회 (논리적 삭제 제외) - 인증 제거 (개발용)
     */
    @GetMapping
    // @PreAuthorize("hasRole('ADMIN')") // 임시 비활성화
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * 사용자 정보 수정 - 인증 제거 (개발용)
     */
    @PutMapping("/{userId}")
    // @PreAuthorize("hasRole('ADMIN')") // 임시 비활성화
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long userId, 
            @RequestBody UserRequestDTO userRequestDTO) {
        try {
            UserResponseDTO updatedUser = userService.updateUser(userId, userRequestDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 내 정보 수정 (현재 로그인한 사용자) - 일반 사용자 가능
     */
    @PutMapping("/me")
    public ResponseEntity<UserResponseDTO> updateMyInfo(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UserRequestDTO userRequestDTO) {
        try {
            UserResponseDTO updatedUser = userService.updateMyInfo(userDetails, userRequestDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 사용자 논리적 삭제 - 인증 제거 (개발용)
     */
    @DeleteMapping("/{userId}")
    // @PreAuthorize("hasRole('ADMIN')") // 임시 비활성화
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 내 계정 삭제 (논리적 삭제) - 일반 사용자 가능
     */
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMyAccount(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            userService.deleteMyAccount(userDetails);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 사용자 존재 여부 확인 - 인증 제거 (개발용)
     */
    @GetMapping("/{userId}/exists")
    // @PreAuthorize("hasRole('ADMIN')") // 임시 비활성화
    public ResponseEntity<Boolean> checkUserExists(@PathVariable Long userId) {
        boolean exists = userService.existsById(userId);
        return ResponseEntity.ok(exists);
    }

    /**
     * 새 계정 생성 (회원가입) - 인증 불필요
     */
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
        log.info("=== 회원가입 요청 시작 ===");
        log.info("요청 데이터: {}", registrationDTO);
        log.info("이메일: {}", registrationDTO.getEmail());
        log.info("닉네임: {}", registrationDTO.getNickname());
        System.out.println("=== SYSTEM OUT: 회원가입 요청 받음 ===");
        System.out.println("이메일: " + registrationDTO.getEmail());
        System.out.println("닉네임: " + registrationDTO.getNickname());
        
        try {
            log.info("UserService.createUser 호출 시작");
            System.out.println("UserService.createUser 호출 시작");
            UserResponseDTO newUser = userService.createUser(registrationDTO);
            log.info("회원가입 성공: {}", newUser);
            System.out.println("회원가입 성공: " + newUser);
            return ResponseEntity.ok(newUser);
        } catch (RuntimeException e) {
            log.error("회원가입 실패 - RuntimeException: {}", e.getMessage(), e);
            System.out.println("회원가입 실패 - RuntimeException: " + e.getMessage());
            e.printStackTrace();
            // 더 구체적인 에러 메시지 반환
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "회원가입 실패");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now().toString());
            
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            log.error("회원가입 실패 - 예상치 못한 에러: {}", e.getMessage(), e);
            System.out.println("회원가입 실패 - 예상치 못한 에러: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "서버 내부 오류");
            errorResponse.put("message", "잠시 후 다시 시도해주세요");
            errorResponse.put("timestamp", LocalDateTime.now().toString());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}
