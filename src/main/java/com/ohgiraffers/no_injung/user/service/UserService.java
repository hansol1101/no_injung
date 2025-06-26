package com.ohgiraffers.no_injung.user.service;

import com.ohgiraffers.no_injung.auth.Response.UserInfoResponse;
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

        User user = userRepository.findByEmailAndIsDeletedFalse(userDetails.getUsername())
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
        List<User> users = userRepository.findAllByIsDeletedFalse();
        return users.stream()
                .map(UserResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 사용자 정보 수정
     */
    @Transactional
    public UserResponseDTO updateUser(Long userId, UserRequestDTO userRequestDTO) {
        User user = userRepository.findByUserIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. ID: " + userId));

        // 이메일 중복 체크 (자신 제외)
        if (userRequestDTO.getEmail() != null && 
            !userRequestDTO.getEmail().equals(user.getEmail()) &&
            userRepository.existsByEmailAndUserIdNotAndIsDeletedFalse(userRequestDTO.getEmail(), userId)) {
            throw new RuntimeException("이미 사용 중인 이메일입니다: " + userRequestDTO.getEmail());
        }

        // 닉네임 중복 체크 (자신 제외)
        if (userRequestDTO.getNickname() != null && 
            !userRequestDTO.getNickname().equals(user.getNickname()) &&
            userRepository.existsByNicknameAndUserIdNotAndIsDeletedFalse(userRequestDTO.getNickname(), userId)) {
            throw new RuntimeException("이미 사용 중인 닉네임입니다: " + userRequestDTO.getNickname());
        }

        // 필드 업데이트
        if (userRequestDTO.getEmail() != null) {
            user.setEmail(userRequestDTO.getEmail());
        }
        if (userRequestDTO.getNickname() != null) {
            user.setNickname(userRequestDTO.getNickname());
        }
        if (userRequestDTO.getPassword() != null && !userRequestDTO.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        }

        User updatedUser = userRepository.save(user);
        return UserResponseDTO.fromEntity(updatedUser);
    }

    /**
     * 내 정보 수정 (현재 로그인한 사용자) - 더 효율적인 방법
     */
    @Transactional
    public UserResponseDTO updateMyInfo(UserDetails userDetails, UserRequestDTO userRequestDTO) {
        if(userDetails == null){
            throw new RuntimeException("로그인이 필요합니다.");
        }

        User user = userRepository.findByEmailAndIsDeletedFalse(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 이메일 중복 체크 (자신 제외)
        if (userRequestDTO.getEmail() != null && 
            !userRequestDTO.getEmail().equals(user.getEmail()) &&
            userRepository.existsByEmailAndUserIdNotAndIsDeletedFalse(userRequestDTO.getEmail(), user.getUserId())) {
            throw new RuntimeException("이미 사용 중인 이메일입니다: " + userRequestDTO.getEmail());
        }

        // 닉네임 중복 체크 (자신 제외)
        if (userRequestDTO.getNickname() != null && 
            !userRequestDTO.getNickname().equals(user.getNickname()) &&
            userRepository.existsByNicknameAndUserIdNotAndIsDeletedFalse(userRequestDTO.getNickname(), user.getUserId())) {
            throw new RuntimeException("이미 사용 중인 닉네임입니다: " + userRequestDTO.getNickname());
        }

        // 필드 업데이트
        if (userRequestDTO.getEmail() != null) {
            user.setEmail(userRequestDTO.getEmail());
        }
        if (userRequestDTO.getNickname() != null) {
            user.setNickname(userRequestDTO.getNickname());
        }
        if (userRequestDTO.getPassword() != null && !userRequestDTO.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        }

        User updatedUser = userRepository.save(user);
        return UserResponseDTO.fromEntity(updatedUser);
    }

    /**
     * 논리적 삭제
     */
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findByUserIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. ID: " + userId));

        user.softDelete(); // Entity의 softDelete 메서드 사용
        userRepository.save(user);
    }

    /**
     * 논리적 삭제 - 현재 로그인한 사용자 (자기 자신)
     */
    @Transactional
    public void deleteMyAccount(UserDetails userDetails) {
        if(userDetails == null){
            throw new RuntimeException("로그인이 필요합니다.");
        }

        User user = userRepository.findByEmailAndIsDeletedFalse(userDetails.getUsername())
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
