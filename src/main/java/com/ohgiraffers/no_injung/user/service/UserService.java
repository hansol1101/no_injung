package com.ohgiraffers.no_injung.user.service;

import com.ohgiraffers.no_injung.auth.dto.UserInfoResponse;
import com.ohgiraffers.no_injung.user.dto.UserRequestDTO;
import com.ohgiraffers.no_injung.user.dto.UserResponseDTO;
import com.ohgiraffers.no_injung.user.entity.User;
import com.ohgiraffers.no_injung.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화를 위해 추가

    /**
     * 기존 메서드 - 내 정보 조회
     */
    public UserInfoResponse getMyInfo(UserDetails userDetails) {
        if(userDetails == null){
            throw new RuntimeException("로그인이 필요합니다.");
        }

        User user = userRepository.findByIdAndIsDeletedFalse(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        return new UserInfoResponse(user);
    }

    /**
     * 단일 사용자 조회 (ID로 조회)
     */
    public UserResponseDTO getUserById(Long userId) {
        User user = userRepository.findByUserIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. ID: " + userId));
        
        return UserResponseDTO.fromEntity(user);
    }

    /**
     * 전체 사용자 조회 (논리적 삭제 제외)
     */
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAllByIsDeletedFalse()
                .stream()
                .map(UserResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 사용자 정보 수정
     */
    @Transactional
    public UserResponseDTO updateUser(Long userId, UserRequestDTO requestDTO) {
        User user = userRepository.findByUserIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. ID: " + userId));

        // 아이디 중복 체크 (자신 제외)
        if (!user.getId().equals(requestDTO.getId()) &&
            userRepository.existsByIdAndUserIdNotAndIsDeletedFalse(requestDTO.getId(), userId)) {
            throw new RuntimeException("이미 사용 중인 아이디입니다: " + requestDTO.getId());
        }

        // 비밀번호 변경 시 암호화
        if (requestDTO.getPassword() != null && !requestDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        }

        user.setId(requestDTO.getId());
        if (requestDTO.getBirthDate() != null) {
            user.setBirthDate(requestDTO.getBirthDate());
        }

        User savedUser = userRepository.save(user);
        return UserResponseDTO.fromEntity(savedUser);
    }

    /**
     * 내 정보 수정 (현재 로그인한 사용자)
     */
    @Transactional
    public UserResponseDTO updateMyInfo(UserDetails userDetails, UserRequestDTO requestDTO) {
        if(userDetails == null){
            throw new RuntimeException("로그인이 필요합니다.");
        }

        User user = userRepository.findByIdAndIsDeletedFalse(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 아이디 중복 체크 (자신 제외)
        if (!user.getId().equals(requestDTO.getId()) &&
            userRepository.existsByIdAndUserIdNotAndIsDeletedFalse(requestDTO.getId(), user.getUserId())) {
            throw new RuntimeException("이미 사용 중인 아이디입니다: " + requestDTO.getId());
        }

        // 비밀번호 변경 시 암호화
        if (requestDTO.getPassword() != null && !requestDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        }

        user.setId(requestDTO.getId());
        if (requestDTO.getBirthDate() != null) {
            user.setBirthDate(requestDTO.getBirthDate());
        }

        User savedUser = userRepository.save(user);
        return UserResponseDTO.fromEntity(savedUser);
    }

    /**
     * 사용자 삭제 (논리적 삭제)
     */
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findByUserIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. ID: " + userId));
        
        user.softDelete();
        userRepository.save(user);
    }

    /**
     * 내 계정 삭제 (논리적 삭제) - 현재 로그인한 사용자
     */
    @Transactional
    public void deleteMyAccount(UserDetails userDetails) {
        if(userDetails == null){
            throw new RuntimeException("로그인이 필요합니다.");
        }

        User user = userRepository.findByIdAndIsDeletedFalse(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        user.softDelete();
        userRepository.save(user);
    }

    /**
     * 사용자 존재 여부 확인 (논리적 삭제 제외)
     */
    public boolean existsById(Long userId) {
        return userRepository.findByUserIdAndIsDeletedFalse(userId).isPresent();
    }
}
