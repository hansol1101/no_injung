package com.ohgiraffers.no_injung.games.common.entity;

import com.ohgiraffers.no_injung.games.common.enums.GameType;
import jakarta.persistence.*;

/**
 * 게임별 상세 정보 - 시퀀스 타입 추가 버전
 * 
 * 설계 원칙: 4개 게임에서 필요한 핵심 데이터 + 카테고리 구분
 * 목표: 최소화 + 필수 분석 데이터 확보
 */
@Entity
@Table(name = "game_details", indexes = {
    @Index(name = "idx_result_id", columnList = "result_id"),
    @Index(name = "idx_game_type", columnList = "game_type"),
    @Index(name = "idx_param1", columnList = "param1"),
    @Index(name = "idx_text_data3", columnList = "text_data3")  // 카테고리 검색용
})
public class GameDetail {

    /**
     * 게임 결과 ID (PK & FK)
     * 존재 이유: GameResult와 1:1 연결점
     */
    @Id
    @Column(name = "result_id")
    private Long resultId;

    /**
     * 게임 타입 (REQUIRED)
     * 존재 이유: param 해석의 기준점
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "game_type", nullable = false, length = 20)
    private GameType gameType;

    /**
     * 숫자 파라미터 1 (핵심 설정값)
     * 게임별 의미:
     * - SEQUENCE: 시퀀스 길이 (3~8)
     * - ORIENTATION: 질문 타입 ID (1:시간, 2:날짜, 3:장소, 4:사람)  
     * - WORD_SEARCH: 격자 크기 (5~15, 정사각형으로 단순화)
     * - CARD_MATCH: 총 카드 수 (4~20)
     */
    @Column(name = "param1")
    private Integer param1;

    /**
     * 숫자 파라미터 2 (보조 설정값)
     * 게임별 의미:
     * - SEQUENCE: 노출 시간(ms) (1000~5000)
     * - ORIENTATION: 사용 안함 (NULL)
     * - WORD_SEARCH: 전체 단어 수 (3~10)
     * - CARD_MATCH: 뒤집기 시도 횟수 (언리얼에서 카운트 후 전송)
     */
    @Column(name = "param2")
    private Integer param2;

    /**
     * 숫자 파라미터 3 (성과 측정값)  
     * 게임별 의미:
     * - SEQUENCE: 사용 안함 (NULL)
     * - ORIENTATION: 사용 안함 (NULL)
     * - WORD_SEARCH: 찾은 단어 수 (0~전체단어수)
     * - CARD_MATCH: 맞춘 페어 수 (언리얼에서 카운트 후 전송)
     */
    @Column(name = "param3")
    private Integer param3;

    /**
     * 텍스트 데이터 1 (문제/정답 데이터)
     * 게임별 의미:
     * - SEQUENCE: 정답 순서 ("cat,dog,bird,fish")
     * - ORIENTATION: 질문 내용 ("오늘은 무슨 요일인가요?")
     * - WORD_SEARCH: 찾을 단어 목록 ("apple,banana,cat")
     * - CARD_MATCH: 사용 안함 (언리얼에서 자체 처리) - NULL
     */
    @Column(name = "text_data1", length = 500)
    private String textData1;

    /**
     * 텍스트 데이터 2 (사용자 답안)
     * 게임별 의미:
     * - SEQUENCE: 사용자 답안 순서 ("cat,bird,dog,fish")
     * - ORIENTATION: 사용자 답안 ("화요일") 
     * - WORD_SEARCH: 찾은 단어 목록 ("apple,cat")
     * - CARD_MATCH: 사용 안함 (언리얼에서 자체 처리) - NULL
     */
    @Column(name = "text_data2", length = 500)
    private String textData2;

    /**
     * 텍스트 데이터 3 (카테고리/타입 구분) ← 새로 추가!
     * 게임별 의미:
     * - SEQUENCE: 시퀀스 타입 ("ANIMAL", "SHAPE", "COLOR", "NUMBER") ← 핵심!
     * - ORIENTATION: 질문 카테고리 ("TIME", "DATE", "PLACE", "PERSON")
     * - WORD_SEARCH: 단어 테마 ("ANIMAL", "FOOD", "OBJECT") (선택적)
     * - CARD_MATCH: 카드 테마 ("ANIMAL", "FRUIT", "SHAPE") (선택적)
     * 
     * 존재 이유:
     * - 시퀀스 타입별 성과 분석 (동물 vs 도형 vs 색깔)
     * - 사용자 선호도 파악
     * - 난이도 조절 기준
     * - 게임별 세부 카테고리 관리
     */
    @Column(name = "text_data3", length = 50)
    private String textData3;

    // 생성자
    public GameDetail() {}

    public GameDetail(Long resultId, GameType gameType) {
        this.resultId = resultId;
        this.gameType = gameType;
    }

    // 게임별 의미있는 접근 메서드
    
    // 순서 기억 게임
    public Integer getSequenceLength() {
        return gameType == GameType.SEQUENCE ? param1 : null;
    }
    
    public Integer getExposureTimeMs() {
        return gameType == GameType.SEQUENCE ? param2 : null;
    }
    
    public String getCorrectSequence() {
        return gameType == GameType.SEQUENCE ? textData1 : null;
    }
    
    public String getUserSequence() {
        return gameType == GameType.SEQUENCE ? textData2 : null;
    }
    
    /**
     * 시퀀스 타입 반환 (핵심 추가 기능!)
     * ANIMAL, SHAPE, COLOR, NUMBER 등
     */
    public String getSequenceType() {
        return gameType == GameType.SEQUENCE ? textData3 : null;
    }

    // 지남력 퀴즈
    public Integer getQuestionTypeId() {
        return gameType == GameType.ORIENTATION ? param1 : null;
    }
    
    public String getQuestionText() {
        return gameType == GameType.ORIENTATION ? textData1 : null;
    }
    
    public String getUserAnswer() {
        return gameType == GameType.ORIENTATION ? textData2 : null;
    }
    
    public String getQuestionCategory() {
        return gameType == GameType.ORIENTATION ? textData3 : null;
    }

    // 낱말 퍼즐
    public Integer getGridSize() {
        return gameType == GameType.WORD_SEARCH ? param1 : null;
    }
    
    public Integer getTotalWords() {
        return gameType == GameType.WORD_SEARCH ? param2 : null;
    }
    
    public Integer getFoundWords() {
        return gameType == GameType.WORD_SEARCH ? param3 : null;
    }
    
    public String getTargetWords() {
        return gameType == GameType.WORD_SEARCH ? textData1 : null;
    }
    
    public String getFoundWordsList() {
        return gameType == GameType.WORD_SEARCH ? textData2 : null;
    }
    
    public String getWordTheme() {
        return gameType == GameType.WORD_SEARCH ? textData3 : null;
    }

    // 카드 매칭
    public Integer getTotalCards() {
        return gameType == GameType.CARD_MATCH ? param1 : null;
    }
    
    public Integer getFlipCount() {
        return gameType == GameType.CARD_MATCH ? param2 : null;
    }
    
    public Integer getMatchedPairs() {
        return gameType == GameType.CARD_MATCH ? param3 : null;
    }
    
    public String getCardTheme() {
        return gameType == GameType.CARD_MATCH ? textData3 : null;
    }

    // 기본 Getters and Setters
    public Long getResultId() {
        return resultId;
    }

    public void setResultId(Long resultId) {
        this.resultId = resultId;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public Integer getParam1() {
        return param1;
    }

    public void setParam1(Integer param1) {
        this.param1 = param1;
    }

    public Integer getParam2() {
        return param2;
    }

    public void setParam2(Integer param2) {
        this.param2 = param2;
    }

    public Integer getParam3() {
        return param3;
    }

    public void setParam3(Integer param3) {
        this.param3 = param3;
    }

    public String getTextData1() {
        return textData1;
    }

    public void setTextData1(String textData1) {
        this.textData1 = textData1;
    }

    public String getTextData2() {
        return textData2;
    }

    public void setTextData2(String textData2) {
        this.textData2 = textData2;
    }

    public String getTextData3() {
        return textData3;
    }

    public void setTextData3(String textData3) {
        this.textData3 = textData3;
    }

    @Override
    public String toString() {
        return "GameDetail{" +
                "resultId=" + resultId +
                ", gameType=" + gameType +
                ", param1=" + param1 +
                ", param2=" + param2 +
                ", param3=" + param3 +
                ", textData1='" + textData1 + '\'' +
                ", textData2='" + textData2 + '\'' +
                ", textData3='" + textData3 + '\'' +
                '}';
    }
}