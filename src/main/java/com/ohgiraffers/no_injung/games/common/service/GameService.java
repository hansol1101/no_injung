package com.ohgiraffers.no_injung.games.common.service;

import com.ohgiraffers.no_injung.games.common.entity.GameSession;
import com.ohgiraffers.no_injung.games.common.entity.GameResult;
import com.ohgiraffers.no_injung.games.common.entity.GameDetail;
import com.ohgiraffers.no_injung.games.common.repository.GameSessionRepository;
import com.ohgiraffers.no_injung.games.common.repository.GameResultRepository;
import com.ohgiraffers.no_injung.games.common.repository.GameDetailRepository;
import com.ohgiraffers.no_injung.games.common.enums.GameType;
import com.ohgiraffers.no_injung.games.common.enums.DifficultyLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 통합 게임 서비스
 * 4가지 게임 (ORIENTATION, SEQUENCE, WORD_SEARCH, CARD_MATCH)을 모두 처리
 */
@Service
@RequiredArgsConstructor
@Transactional
public class    GameService {

    private final GameSessionRepository gameSessionRepository;
    private final GameResultRepository gameResultRepository;
    private final GameDetailRepository gameDetailRepository;

    /**
     * 게임 시작
     */
    public GameSession startGame(Long userId, GameType gameType, DifficultyLevel difficultyLevel) {
        GameSession session = new GameSession(userId, gameType, difficultyLevel);
        return gameSessionRepository.save(session);
    }

    /**
     * 게임 라운드 결과 저장
     */
    public GameResult saveGameResult(Long sessionId, Integer roundNumber, Boolean isCorrect, 
                                   Integer score, Integer responseTimeMs) {
        GameResult result = new GameResult(sessionId, roundNumber);
        result.setCorrect(isCorrect);
        result.setScore(score);
        result.setResponseTimeMs(responseTimeMs);
        
        return gameResultRepository.save(result);
    }

    /**
     * 게임 상세 정보 저장 (게임별 특화 데이터)
     */
    public GameDetail saveGameDetail(Long resultId, GameType gameType, 
                                   Integer param1, Integer param2, Integer param3,
                                   String textData1, String textData2, String textData3) {
        GameDetail detail = new GameDetail(resultId, gameType);
        detail.setParam1(param1);
        detail.setParam2(param2);
        detail.setParam3(param3);
        detail.setTextData1(textData1);
        detail.setTextData2(textData2);
        detail.setTextData3(textData3);
        
        return gameDetailRepository.save(detail);
    }

    /**
     * 게임 세션 완료 처리
     */
    public void completeGameSession(Long sessionId, Integer totalScore, 
                                  Integer totalRounds, Integer correctRounds, Integer playTimeMs) {
        GameSession session = gameSessionRepository.findById(sessionId)
            .orElseThrow(() -> new RuntimeException("Session not found"));
        
        session.setEndTime(LocalDateTime.now());
        session.setCompleted(true);
        session.setTotalScore(totalScore);
        session.setTotalRounds(totalRounds);
        session.setCorrectRounds(correctRounds);
        session.setPlayTimeMs(playTimeMs);
        
        gameSessionRepository.save(session);
    }

    /**
     * 사용자별 게임 세션 조회
     */
    @Transactional(readOnly = true)
    public List<GameSession> getUserGameSessions(Long userId) {
        return gameSessionRepository.findByUserIdOrderByStartTimeDesc(userId);
    }

    /**
     * 세션별 게임 결과 조회
     */
    @Transactional(readOnly = true)
    public List<GameResult> getSessionResults(Long sessionId) {
        return gameResultRepository.findBySessionIdOrderByRoundNumber(sessionId);
    }

    /**
     * 게임 결과의 상세 정보 조회
     */
    @Transactional(readOnly = true)
    public GameDetail getGameDetail(Long resultId) {
        return gameDetailRepository.findByResultId(resultId)
            .orElse(null);
    }
}