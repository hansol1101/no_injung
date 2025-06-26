package com.ohgiraffers.no_injung.games.common.repository;

import com.ohgiraffers.no_injung.games.common.entity.GameSession;
import com.ohgiraffers.no_injung.games.common.enums.GameType;
import com.ohgiraffers.no_injung.games.common.enums.DifficultyLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface GameSessionRepository extends JpaRepository<GameSession, Long> {
    
    /**
     * 사용자별 게임 세션 조회
     */
    List<GameSession> findByUserId(Long userId);
    
    /**
     * 사용자별 특정 게임 타입 세션 조회
     */
    List<GameSession> findByUserIdAndGameType(Long userId, GameType gameType);
    
    /**
     * 사용자별 완료된 게임 세션 조회
     */
    List<GameSession> findByUserIdAndIsCompleted(Long userId, Boolean isCompleted);
    
    /**
     * 사용자의 최근 게임 세션 조회
     */
    List<GameSession> findByUserIdOrderByStartTimeDesc(Long userId);
    
    /**
     * 사용자별 게임 통계 조회
     */
    @Query("SELECT gs.gameType, " +
           "COUNT(*) as totalSessions, " +
           "AVG(gs.totalScore) as avgScore, " +
           "AVG(CASE WHEN gs.correctRounds > 0 THEN gs.correctRounds * 1.0 / gs.totalRounds ELSE 0 END) as avgAccuracy " +
           "FROM GameSession gs " +
           "WHERE gs.userId = :userId AND gs.isCompleted = true " +
           "GROUP BY gs.gameType")
    List<Object[]> getUserGameStatistics(@Param("userId") Long userId);
    
    /**
     * 특정 기간 내 사용자 활동 조회
     */
    List<GameSession> findByUserIdAndStartTimeBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * 게임 타입별 전체 통계
     */
    @Query("SELECT gs.gameType, " +
           "COUNT(*) as totalSessions, " +
           "AVG(gs.totalScore) as avgScore, " +
           "AVG(gs.playTimeMs) as avgPlayTime " +
           "FROM GameSession gs " +
           "WHERE gs.isCompleted = true " +
           "GROUP BY gs.gameType")
    List<Object[]> getGameTypeStatistics();
}