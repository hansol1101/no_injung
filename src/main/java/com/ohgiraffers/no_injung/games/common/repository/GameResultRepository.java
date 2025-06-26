package com.ohgiraffers.no_injung.games.common.repository;

import com.ohgiraffers.no_injung.games.common.entity.GameResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameResultRepository extends JpaRepository<GameResult, Long> {
    
    /**
     * 세션별 게임 결과 조회
     */
    List<GameResult> findBySessionId(Long sessionId);
    
    /**
     * 세션별 게임 결과를 라운드 순서로 조회
     */
    List<GameResult> findBySessionIdOrderByRoundNumber(Long sessionId);
    
    /**
     * 특정 세션의 특정 라운드 결과 조회
     */
    GameResult findBySessionIdAndRoundNumber(Long sessionId, Integer roundNumber);
    
    /**
     * 세션별 정답 개수 조회
     */
    @Query("SELECT COUNT(*) FROM GameResult gr WHERE gr.sessionId = :sessionId AND gr.isCorrect = true")
    Long countCorrectAnswersBySession(@Param("sessionId") Long sessionId);
    
    /**
     * 세션별 평균 응답 시간 조회
     */
    @Query("SELECT AVG(gr.responseTimeMs) FROM GameResult gr WHERE gr.sessionId = :sessionId")
    Double getAverageResponseTimeBySession(@Param("sessionId") Long sessionId);
    
    /**
     * 세션별 총 점수 조회
     */
    @Query("SELECT SUM(gr.score) FROM GameResult gr WHERE gr.sessionId = :sessionId")
    Integer getTotalScoreBySession(@Param("sessionId") Long sessionId);
    
    /**
     * 응답 시간이 특정 시간 이하인 결과 조회 (빠른 응답 분석용)
     */
    List<GameResult> findByResponseTimeMsLessThanEqual(Integer maxResponseTime);
    
    /**
     * 정답률이 낮은 라운드 분석 (어려운 문제 파악용)
     */
    @Query("SELECT gr.roundNumber, " +
           "COUNT(*) as totalAttempts, " +
           "SUM(CASE WHEN gr.isCorrect THEN 1 ELSE 0 END) as correctCount, " +
           "AVG(gr.responseTimeMs) as avgResponseTime " +
           "FROM GameResult gr " +
           "WHERE gr.sessionId IN (SELECT s.sessionId FROM GameSession s WHERE s.gameType = :gameType) " +
           "GROUP BY gr.roundNumber " +
           "ORDER BY (SUM(CASE WHEN gr.isCorrect THEN 1 ELSE 0 END) * 1.0 / COUNT(*)) ASC")
    List<Object[]> getDifficultRoundsAnalysis(@Param("gameType") String gameType);
}