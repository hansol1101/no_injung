package com.ohgiraffers.no_injung.games.common.entity;

import com.ohgiraffers.no_injung.games.common.enums.DifficultyLevel;
import com.ohgiraffers.no_injung.games.common.enums.GameType;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "game_sessions", indexes = {
    @Index(name = "idx_user_id", columnList = "user_id")        //사용자별 조회
})
public class GameSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Long sessionId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "game_type", nullable = false, length = 20)
    private GameType gameType;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty_level", nullable = false, length = 10)
    private DifficultyLevel difficultyLevel = DifficultyLevel.EASY;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime = LocalDateTime.now();

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted = false;

    @Column(name = "total_score", nullable = false)
    private Integer totalScore = 0;

    @Column(name = "total_rounds", nullable = false)
    private Integer totalRounds = 0;

    @Column(name = "correct_rounds", nullable = false)
    private Integer correctRounds = 0;

    @Column(name = "play_time_ms", nullable = false)
    private Integer playTimeMs = 0;

    // 기본 생성자
    public GameSession() {}

    // 생성자
    public GameSession(Long userId, GameType gameType, DifficultyLevel difficultyLevel) {
        this.userId = userId;
        this.gameType = gameType;
        this.difficultyLevel = difficultyLevel;
    }

    // Getters and Setters
    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Boolean getCompleted() {
        return isCompleted;
    }

    public void setCompleted(Boolean completed) {
        isCompleted = completed;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public Integer getTotalRounds() {
        return totalRounds;
    }

    public void setTotalRounds(Integer totalRounds) {
        this.totalRounds = totalRounds;
    }

    public Integer getCorrectRounds() {
        return correctRounds;
    }

    public void setCorrectRounds(Integer correctRounds) {
        this.correctRounds = correctRounds;
    }

    public Integer getPlayTimeMs() {
        return playTimeMs;
    }

    public void setPlayTimeMs(Integer playTimeMs) {
        this.playTimeMs = playTimeMs;
    }

    @Override
    public String toString() {
        return "GameSession{" +
                "sessionId=" + sessionId +
                ", userId=" + userId +
                ", gameType=" + gameType +
                ", difficultyLevel=" + difficultyLevel +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", isCompleted=" + isCompleted +
                ", totalScore=" + totalScore +
                ", totalRounds=" + totalRounds +
                ", correctRounds=" + correctRounds +
                ", playTimeMs=" + playTimeMs +
                '}';
    }
}