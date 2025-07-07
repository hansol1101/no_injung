package com.ohgiraffers.no_injung.games.common.controller;

import com.ohgiraffers.no_injung.games.common.service.GameService;
import com.ohgiraffers.no_injung.games.common.entity.GameSession;
import com.ohgiraffers.no_injung.games.common.entity.GameResult;
import com.ohgiraffers.no_injung.games.common.entity.GameDetail;
import com.ohgiraffers.no_injung.games.common.enums.GameType;
import com.ohgiraffers.no_injung.games.common.enums.DifficultyLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 통합 게임 API 컨트롤러
 * 4가지 게임의 공통 API 제공
 */
// @RestController
// @RequestMapping("/api/games")
@RequiredArgsConstructor
// @Profile("!swagger-test") // 임시 비활성화
public class GameController {

    private final GameService gameService;

    /**
     * 게임 시작
     * POST /api/games/start
     */
    @PostMapping("/start")
    public ResponseEntity<GameSession> startGame(@RequestBody Map<String, Object> request) {
        Long userId = Long.valueOf(request.get("userId").toString());
        GameType gameType = GameType.valueOf(request.get("gameType").toString());
        DifficultyLevel difficultyLevel = DifficultyLevel.valueOf(
            request.getOrDefault("difficultyLevel", "EASY").toString()
        );
        
        GameSession session = gameService.startGame(userId, gameType, difficultyLevel);
        return ResponseEntity.ok(session);
    }

    /**
     * 게임 결과 제출
     * POST /api/games/results
     */
    @PostMapping("/results")
    public ResponseEntity<Map<String, Object>> submitGameResult(@RequestBody Map<String, Object> request) {
        // 1. 기본 게임 결과 저장
        Long sessionId = Long.valueOf(request.get("sessionId").toString());
        Integer roundNumber = Integer.valueOf(request.get("roundNumber").toString());
        Boolean isCorrect = Boolean.valueOf(request.get("isCorrect").toString());
        Integer score = Integer.valueOf(request.getOrDefault("score", 0).toString());
        Integer responseTimeMs = Integer.valueOf(request.get("responseTimeMs").toString());
        
        GameResult result = gameService.saveGameResult(sessionId, roundNumber, isCorrect, score, responseTimeMs);
        
        // 2. 게임별 상세 정보 저장 (있는 경우)
        if (request.containsKey("gameDetails")) {
            Map<String, Object> gameDetails = (Map<String, Object>) request.get("gameDetails");
            GameType gameType = GameType.valueOf(gameDetails.get("gameType").toString());
            
            Integer param1 = gameDetails.containsKey("param1") ? 
                Integer.valueOf(gameDetails.get("param1").toString()) : null;
            Integer param2 = gameDetails.containsKey("param2") ? 
                Integer.valueOf(gameDetails.get("param2").toString()) : null;
            Integer param3 = gameDetails.containsKey("param3") ? 
                Integer.valueOf(gameDetails.get("param3").toString()) : null;
            String textData1 = gameDetails.containsKey("textData1") ? 
                gameDetails.get("textData1").toString() : null;
            String textData2 = gameDetails.containsKey("textData2") ? 
                gameDetails.get("textData2").toString() : null;
            String textData3 = gameDetails.containsKey("textData3") ? 
                gameDetails.get("textData3").toString() : null;
            
            gameService.saveGameDetail(result.getResultId(), gameType, 
                param1, param2, param3, textData1, textData2, textData3);
        }
        
        return ResponseEntity.ok(Map.of(
            "resultId", result.getResultId(),
            "isCorrect", result.getCorrect(),
            "score", result.getScore()
        ));
    }

    /**
     * 게임 세션 완료
     * POST /api/games/{sessionId}/complete
     */
    @PostMapping("/{sessionId}/complete")
    public ResponseEntity<String> completeGame(@PathVariable Long sessionId, 
                                             @RequestBody Map<String, Object> request) {
        Integer totalScore = Integer.valueOf(request.get("totalScore").toString());
        Integer totalRounds = Integer.valueOf(request.get("totalRounds").toString());
        Integer correctRounds = Integer.valueOf(request.get("correctRounds").toString());
        Integer playTimeMs = Integer.valueOf(request.get("playTimeMs").toString());
        
        gameService.completeGameSession(sessionId, totalScore, totalRounds, correctRounds, playTimeMs);
        
        return ResponseEntity.ok("Game completed successfully");
    }

    /**
     * 사용자별 게임 세션 조회
     * GET /api/games/sessions/{userId}
     */
    @GetMapping("/sessions/{userId}")
    public ResponseEntity<List<GameSession>> getUserSessions(@PathVariable Long userId) {
        List<GameSession> sessions = gameService.getUserGameSessions(userId);
        return ResponseEntity.ok(sessions);
    }

    /**
     * 세션별 게임 결과 조회
     * GET /api/games/{sessionId}/results
     */
    @GetMapping("/{sessionId}/results")
    public ResponseEntity<List<GameResult>> getSessionResults(@PathVariable Long sessionId) {
        List<GameResult> results = gameService.getSessionResults(sessionId);
        return ResponseEntity.ok(results);
    }

    /**
     * 게임 결과 상세 정보 조회
     * GET /api/games/results/{resultId}/details
     */
    @GetMapping("/results/{resultId}/details")
    public ResponseEntity<GameDetail> getGameDetail(@PathVariable Long resultId) {
        GameDetail detail = gameService.getGameDetail(resultId);
        if (detail != null) {
            return ResponseEntity.ok(detail);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}