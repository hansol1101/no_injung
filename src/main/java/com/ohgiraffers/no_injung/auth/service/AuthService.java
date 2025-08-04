package com.ohgiraffers.no_injung.auth.service;

import com.ohgiraffers.no_injung.auth.dto.LoginRequest;
import com.ohgiraffers.no_injung.auth.dto.SignUpRequest;
import com.ohgiraffers.no_injung.auth.dto.AuthResponse;
import com.ohgiraffers.no_injung.auth.jwt.JwtTokenProvider;
import com.ohgiraffers.no_injung.user.entity.Role;
import com.ohgiraffers.no_injung.user.entity.User;
import com.ohgiraffers.no_injung.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 인증 서비스 - 닉네임을 로그인용 아이디로 사용
 * 회원가입, 로그인, 보안 검증 등을 담당
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 회원가입 - 닉네임을 로그인용 아이디로 사용
     * 1. 닉네임 중복 검증
     * 2. 비밀번호 암호화
     * 3. 기본 권한 설정 (USER)
     * 4. 로그 기록
     */
    @Transactional
    public void signup(SignUpRequest request) {
        log.info("=== 회원가입 시작: {} ===", request.getNickname());

        // 1. 닉네임 중복 검증 (논리삭제된 사용자 제외)
        if (userRepository.existsByNicknameAndIsDeletedFalse(request.getNickname())) {
            log.warn("회원가입 실패 - 닉네임 중복: {}", request.getNickname());
            throw new RuntimeException("이미 사용 중인 닉네임입니다: " + request.getNickname());
        }

        // 2. 비밀번호 강도 검증 (추가 보안)
        validatePasswordStrength(request.getPassword());

        // 3. 사용자 엔티티 생성
        User user = User.builder()
                .nickname(request.getNickname())
                .password(passwordEncoder.encode(request.getPassword()))
                .birthDate(request.getBirthdate())
                .role(Role.USER) // 기본 권한은 USER
                .build();

        try {
            User savedUser = userRepository.save(user);
            log.info("회원가입 성공: ID={}, 닉네임={}",
                    savedUser.getUserId(), savedUser.getNickname());
        } catch (Exception e) {
            log.error("회원가입 실패 - DB 저장 오류: {}", e.getMessage(), e);
            throw new RuntimeException("회원가입 처리 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
        }
    }

    /**
     * 로그인 - 닉네임을 로그인용 아이디로 사용
     * 1. 사용자 존재 여부 확인 (논리삭제 제외)
     * 2. 비밀번호 검증
     * 3. JWT 토큰 생성 (Role 정보 포함)
     * 4. 로그 기록
     */
    @Transactional
    public AuthResponse login(LoginRequest request) {
        log.info("=== 로그인 시도: {} ===", request.getNickname());

        // 1. 사용자 조회 (논리삭제되지 않은 사용자만)
        User user = userRepository.findByNicknameAndIsDeletedFalse(request.getNickname())
                .orElseThrow(() -> {
                    log.warn("로그인 실패 - 사용자 없음: {}", request.getNickname());
                    return new RuntimeException("닉네임 또는 비밀번호가 올바르지 않습니다.");
                });

        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("로그인 실패 - 비밀번호 불일치: {}", request.getNickname());
            throw new RuntimeException("닉네임 또는 비밀번호가 올바르지 않습니다.");
        }

        // 3. JWT 토큰 생성 (Role 정보 포함)
        String token = jwtTokenProvider.createToken(user.getNickname());

        log.info("로그인 성공: ID={}, 닉네임={}, 권한={}",
                user.getUserId(), user.getNickname(), user.getRole());

        return new AuthResponse(token, user.getNickname(), user.getRole().name());
    }

    /**
     * 비밀번호 강도 검증
     * 최소 6자 이상, 영문 + 숫자 조합 권장
     */
    private void validatePasswordStrength(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new RuntimeException("비밀번호는 필수 항목입니다.");
        }

        if (password.length() < 6) {
            throw new RuntimeException("비밀번호는 최소 6자 이상이어야 합니다.");
        }

        if (password.length() > 255) {
            throw new RuntimeException("비밀번호는 255자를 초과할 수 없습니다.");
        }

        // 추가 보안: 공백만으로 구성된 비밀번호 방지
        if (password.trim().length() != password.length()) {
            throw new RuntimeException("비밀번호에는 앞뒤 공백을 포함할 수 없습니다.");
        }

        // 선택적: 영문+숫자 조합 권장 (주석 처리, 필요시 활성화)
        /*
        boolean hasLetter = password.matches(".*[a-zA-Z].*");
        boolean hasDigit = password.matches(".*[0-9].*");
        if (!hasLetter || !hasDigit) {
            throw new RuntimeException("비밀번호는 영문과 숫자를 모두 포함해야 합니다.");
        }
        */
    }

    /**
     * 닉네임 중복 확인 (공개 API용)
     * 회원가입 폼에서 실시간 검증에 활용 가능
     */
    public boolean isNicknameAvailable(String nickname) {
        if (nickname == null || nickname.trim().isEmpty()) {
            return false;
        }
        return !userRepository.existsByNicknameAndIsDeletedFalse(nickname.trim());
    }

    /**
     * 사용자 활성 상태 확인
     * JWT 필터에서 활용 가능
     */
    public boolean isUserActive(String nickname) {
        return userRepository.findByNicknameAndIsDeletedFalse(nickname).isPresent();
    }
}
