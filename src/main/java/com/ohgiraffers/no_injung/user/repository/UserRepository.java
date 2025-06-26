package com.ohgiraffers.no_injung.user.repository;

import com.ohgiraffers.no_injung.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    
    // 기존 메서드들
    Optional<User> findByEmailAndIsDeletedFalse(String email);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    Optional<User> findByEmail(String email);
    
    // 논리적 삭제를 고려한 단일 조회
    Optional<User> findByUserIdAndIsDeletedFalse(Long userId);
    
    // 논리적 삭제를 고려한 전체 조회
    List<User> findAllByIsDeletedFalse();
    
    // 이메일 중복 체크 (논리적 삭제 제외)
    boolean existsByEmailAndIsDeletedFalse(String email);
    
    // 닉네임 중복 체크 (논리적 삭제 제외)
    boolean existsByNicknameAndIsDeletedFalse(String nickname);
    
    // 수정 시 중복 체크 (자신 제외)
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email AND u.userId != :userId AND u.isDeleted = false")
    boolean existsByEmailAndUserIdNotAndIsDeletedFalse(@Param("email") String email, @Param("userId") Long userId);
    
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.nickname = :nickname AND u.userId != :userId AND u.isDeleted = false")
    boolean existsByNicknameAndUserIdNotAndIsDeletedFalse(@Param("nickname") String nickname, @Param("userId") Long userId);
}
