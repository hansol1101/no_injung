package com.ohgiraffers.no_injung.games.common.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "game_results", indexes = {
    @Index(name = "idx_session_id", columnList = "session_id"),
    @Index(name = "idx_session_round", columnList = "session_id, round_number")
})
public class GameResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id")
    private Long resultId;

    @Column(name = "session_id", nullable = false)
    private Long sessionId;

    @Column(name = "round_number", nullable = false)
    private Integer roundNumber;

    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect = false;

    @Column(name = "score", nullable = false)
    private Integer score = 0;

    @Column(name = "response_time_ms", nullable = false)
    private Integer responseTimeMs = 0;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // 기본 생성자
    public GameResult() {}

    // 생성자
    public GameResult(Long sessionId, Integer roundNumber) {
        this.sessionId = sessionId;
        this.roundNumber = roundNumber;
    }

    // Getters and Setters
    public Long getResultId() {
        return resultId;
    }

    public void setResultId(Long resultId) {
        this.resultId = resultId;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(Integer roundNumber) {
        this.roundNumber = roundNumber;
    }

    public Boolean getCorrect() {
        return isCorrect;
    }

    public void setCorrect(Boolean correct) {
        isCorrect = correct;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getResponseTimeMs() {
        return responseTimeMs;
    }

    public void setResponseTimeMs(Integer responseTimeMs) {
        this.responseTimeMs = responseTimeMs;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "GameResult{" +
                "resultId=" + resultId +
                ", sessionId=" + sessionId +
                ", roundNumber=" + roundNumber +
                ", isCorrect=" + isCorrect +
                ", score=" + score +
                ", responseTimeMs=" + responseTimeMs +
                ", createdAt=" + createdAt +
                '}';
    }
}