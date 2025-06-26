package com.ohgiraffers.no_injung.games.common.repository;

import com.ohgiraffers.no_injung.games.common.entity.GameDetail;
import com.ohgiraffers.no_injung.games.common.enums.GameType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameDetailRepository extends JpaRepository<GameDetail, Long> {
    
    /**
     * 게임 결과 ID로 상세 정보 조회
     */
    Optional<GameDetail> findByResultId(Long resultId);
    
    /**
     * 게임 타입별 상세 정보 조회
     */
    List<GameDetail> findByGameType(GameType gameType);
    
    /**
     * 게임 타입과 카테고리별 조회 (시퀀스 타입, 질문 카테고리 등)
     */
    List<GameDetail> findByGameTypeAndTextData3(GameType gameType, String category);
    
    /**
     * 특정 파라미터 조건으로 조회 (예: 시퀀스 길이별, 격자 크기별)
     */
    List<GameDetail> findByGameTypeAndParam1(GameType gameType, Integer param1);
    
    /**
     * 게임 타입별 통계 조회
     */
    @Query("SELECT d.textData3 as category, " +
           "AVG(CASE WHEN r.isCorrect THEN 1.0 ELSE 0.0 END) as successRate, " +
           "COUNT(*) as totalPlays " +
           "FROM GameDetail d JOIN GameResult r ON d.resultId = r.resultId " +
           "WHERE d.gameType = :gameType " +
           "GROUP BY d.textData3")
    List<Object[]> getGameTypeStatistics(@Param("gameType") GameType gameType);
    
    /**
     * 사용자별 게임 타입 성과 조회
     */
    @Query("SELECT d.textData3 as category, " +
           "AVG(CASE WHEN r.isCorrect THEN 1.0 ELSE 0.0 END) as successRate, " +
           "COUNT(*) as playCount " +
           "FROM GameDetail d " +
           "JOIN GameResult r ON d.resultId = r.resultId " +
           "JOIN GameSession s ON r.sessionId = s.sessionId " +
           "WHERE d.gameType = :gameType AND s.userId = :userId " +
           "GROUP BY d.textData3")
    List<Object[]> getUserGameTypePerformance(@Param("gameType") GameType gameType, 
                                            @Param("userId") Long userId);
}