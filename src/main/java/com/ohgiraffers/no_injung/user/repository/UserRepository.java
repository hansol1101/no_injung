package com.ohgiraffers.no_injung.user.repository;

import com.ohgiraffers.no_injung.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    
    // 아이디를 로그인용 아이디로 사용하는 메서드들
    Optional<User> findByIdAndIsDeletedFalse(String id);
    boolean existsByIdAndIsDeletedFalse(String id);
    
    // 논리적 삭제를 고려한 단일 조회
    Optional<User> findByUserIdAndIsDeletedFalse(Long userId);
    
    // 논리적 삭제를 고려한 전체 조회
    List<User> findAllByIsDeletedFalse();
    
    // 아이디 중복 체크 (논리적 삭제 제외) - 위에 이미 정의됨
    
    // 수정 시 중복 체크 (자신 제외)
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.id = :id AND u.userId != :userId AND u.isDeleted = false")
    boolean existsByIdAndUserIdNotAndIsDeletedFalse(@Param("id") String id, @Param("userId") Long userId);
}
