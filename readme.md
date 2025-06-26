각 게임 플로우 

- 지남력 퀴즈 훈련

    ## 1. 지남력 퀴즈 훈련 (Orientation Quiz Training)

    ### 🎯 목적

    사용자의 **시간, 장소, 사람에 대한 인지 능력(지남력)**을 점진적으로 훈련

    ### 🔁 게임 흐름 (API 기반)

    ```
    text
    복사편집
    [1] 클라이언트 → 서버: 훈련 시작 요청 (/start)
    [2] 서버 → 클라이언트: 문제 리스트 응답 (/questions)
    [3] 클라이언트 → 서버: 사용자 답변 제출 (/answer)
    [4] 서버: 정답 체크 및 점수 저장
    [5] 클라이언트 → 서버: 결과 요청 (/result)

    ```

    ### 🧩 문제 예시 및 난이도

    | 단계 | 난이도 | 예시 |
    | --- | --- | --- |
    | 1단계 | 매우 쉬움 | "오늘은 몇 월인가요?" |
    | 2단계 | 쉬움 | "오늘은 무슨 요일인가요?" |
    | 3단계 | 보통 | "다음 중 오늘 날짜는?" |
    | 4단계 | 어려움 | "지금은 몇 시 몇 분인가요?" |
    | 5단계 | 매우 어려움 | "지난 주 금요일 날짜는?" |

    ### 📌 특징

    - 퀴즈 형식 (객관식 가능)
    - 반복 시 자동 난이도 상승
    - 결과 피드백 및 히스토리 저장
- 순서 기억 인지 게임

    ### 🎯 목적

    **단기 기억력**과 **순서 기억 능력**을 훈련

    ### 🔁 게임 흐름

    1. **제시**: 그림(동물, 사물 등)을 순서대로 노출 (짧은 시간)
    2. **기억**: 사용자 시각적 순서 기억
    3. **선택**: 보기 중 올바른 순서를 선택
    4. **정답 확인**: 점수 부여 및 반복 → 난이도 점진 상승

    ### 🎚️ 난이도 조절 방식

    - 노출 시간 단축
    - 항목 수 증가 (3개 → 4개 → 5개 …)
    - 보기 수 증가 (4지선다 → 5지선다 …)

    ### 📌 특징

    - 순서 기억 훈련
    - 반복 시 난이도 자동 조정
    - 실시간 점수 제공 및 기록 가능
- 낱말 퀴즈 게임

    ### 🎯 목적

    **단어 인식**, **시각 탐색 능력**, **주의 집중력** 향상

    ### 🔁 게임 흐름

    1. **격자 제공**: 정사각형 또는 직사각형 문자 그리드 생성
    2. **단어 제시** (또는 주제만): 사용자는 해당 단어들을 격자에서 탐색
    3. **찾기 완료 시 서버에 전송** (/answer)
    4. **정답 검증 및 점수 반영**

    ### 🧭 방향

    - 가로, 세로, 대각선 (8방향 지원)

    ### 🎚️ 난이도 조절

    - 격자 크기 (5x5, 8x8 등)
    - 단어 길이
    - 숨겨진 방향 수 제한 (가로/세로만 → 모든 방향)
    - 단어 미리보기 유무
- 카드 짝 맞추기 게임

    ### 목적

    **기억력**과 **주의력** 향상

    ### 🔁 게임 흐름

    1. **초기 카드 노출**: 전체 카드 앞면 3~5초 노출
    2. **기억 단계**: 플레이어가 각 카드 위치 기억
    3. **뒤집기 시작**: 카드 뒷면 상태에서 2장씩 선택하여 짝 맞추기
    4. **정답 처리**: 짝 맞추면 카드 유지, 틀리면 다시 뒤집기
    5. **모든 카드 맞추면 종료 및 점수 저장**

    ### 🎚️ 난이도 조절

    - 카드 개수 증가 (4장 → 8장 → 16장)
    - 공개 시간 단축
    - 그림 유사도 증가 (혼동 유도)

    ---

    ## 🔄 공통 기능 제안 (서버 관점)

    | 기능 | 설명 |
    | --- | --- |
    | `/start` | 게임 시작 요청 (게임 종류 및 사용자 ID 포함) |
    | `/questions` | 문제 데이터 제공 (지남력/순서기억/낱말/카드게임) |
    | `/answer` | 사용자 답변 제출 |
    | `/result` | 점수 및 피드백 제공 |
    | `/history` | 과거 점수 및 게임 기록 제공 (선택적) |
    



프로그램 자체의 내용
해당 게임들을 구현을 하려고해 우리는 백엔드 스프링 부트이야 프론트는 언리얼쪽에서 해준다고 했고 멀티 플레이(p2p를 이용한)도 언리얼쪽에서 할것같아 클라이언트가 원하는 플로우는 간단하게 회원가입(구현되어 있음)
들어와서 게임선택 후 게임 진행, 멀티 플레이도 가능 게임 결과는 확인 가능하게 이런식의 플로우


├── GameTrainingApplication.java                    # 메인 애플리케이션
├── common/                                         # 공통 기능
│   ├── config/                                     # 설정 클래스
│   ├── exception/                                  # 예외 처리
│   ├── response/                                   # 공통 응답 형식
│   └── util/                                       # 유틸리티
├── user/                                           # 사용자 도메인
│   ├── entity/User.java
│   ├── repository/UserRepository.java
│   ├── service/UserService.java
│   ├── controller/UserController.java
│   └── dto/                                        # 사용자 DTO
└── games/                                          # 게임 관련 모든 것
├── common/                                     # 게임 공통 요소
│   ├── entity/                                 # 공통 엔티티
│   │   ├── GameSession.java                   # 게임 세션 엔티티
│   │   └── GameResult.java                    # 게임 결과 엔티티
│   ├── enums/                                  # 게임 관련 enum
│   │   ├── GameType.java                      # 게임 타입
│   │   └── DifficultyLevel.java               # 난이도
│   ├── repository/                             # 공통 리포지토리
│   │   ├── GameSessionRepository.java
│   │   └── GameResultRepository.java
│   ├── service/                                # 게임 공통 서비스
│   │   ├── AbstractGameService.java           # 게임 추상 서비스
│   │   ├── GameServiceFactory.java            # 게임 서비스 팩토리
│   │   ├── GameSessionService.java            # 세션 관리 서비스
│   │   └── GameStatisticsService.java         # 통계 서비스
│   ├── controller/                             # 게임 공통 컨트롤러
│   │   ├── GameController.java                # 통합 게임 API
│   │   └── GameStatisticsController.java      # 통계 API
│   ├── dto/                                    # 공통 DTO
│   │   ├── request/
│   │   │   ├── GameStartRequest.java
│   │   │   └── GameResultRequest.java
│   │   └── response/
│   │       ├── GameSessionResponse.java
│   │       └── GameStatisticsResponse.java
│   └── validator/                              # 게임 공통 검증
│       └── GameValidator.java
├── sequence/                                   # 순서 기억 게임
│   ├── service/
│   │   └── SequenceGameService.java
│   ├── dto/
│   │   ├── SequenceGameRequest.java
│   │   ├── SequenceGameResponse.java
│   │   └── SequenceGameConfig.java
│   ├── controller/
│   │   └── SequenceGameController.java        # 게임별 특화 API (선택사항)
│   └── util/
│       └── SequenceGenerator.java              # 시퀀스 생성 유틸
├── wordsearch/                                 # 낱말 퍼즐 게임
│   ├── service/
│   │   └── WordSearchGameService.java
│   ├── dto/
│   │   ├── WordSearchRequest.java
│   │   ├── WordSearchResponse.java
│   │   └── WordSearchConfig.java
│   ├── controller/
│   │   └── WordSearchController.java
│   └── util/
│       ├── WordGridGenerator.java              # 단어 격자 생성
│       └── WordDictionary.java                 # 단어 사전
├── orientation/                                # 지남력 훈련 게임
│   ├── service/
│   │   └── OrientationGameService.java
│   ├── dto/
│   │   ├── OrientationRequest.java
│   │   ├── OrientationResponse.java
│   │   └── OrientationQuestion.java
│   ├── controller/
│   │   └── OrientationController.java
│   └── util/
│       └── QuestionGenerator.java              # 질문 생성기
└── cardmatch/                                  # 카드 뒤집기 게임
├── service/
│   └── CardMatchGameService.java
├── dto/
│   ├── CardMatchRequest.java
│   ├── CardMatchResponse.java
│   └── CardMatchConfig.java
├── controller/
│   └── CardMatchController.java
└── util/
└── CardGenerator.java                  # 카드 생성 유틸 

---

# 🎯 **최종 최적화된 구현 설계 (2025.06.26 업데이트)**

## 📊 **최적화된 DB 설계 (4개 테이블)**

### **주요 변경사항**
- **테이블 수 최소화**: 7개 → 4개 (43% 감소)
- **정규화 완료**: 중복 데이터 제거
- **언리얼 연동 최적화**: 카드 게임은 클라이언트 처리
- **시퀀스 타입 구분**: ANIMAL/SHAPE/COLOR 분석 가능

### **1. users 테이블 (최소화 완료)**
```sql
CREATE TABLE users (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(50) NOT NULL UNIQUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at DATETIME NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    
    INDEX idx_email (email),
    INDEX idx_nickname (nickname)
);
```

### **2. game_sessions 테이블 (집계 최적화)**
```sql
CREATE TABLE game_sessions (
    session_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    game_type VARCHAR(20) NOT NULL,  -- ORIENTATION/SEQUENCE/WORD_SEARCH/CARD_MATCH
    difficulty_level VARCHAR(10) NOT NULL DEFAULT 'EASY',  -- EASY/NORMAL/HARD
    start_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    end_time DATETIME NULL,
    is_completed BOOLEAN NOT NULL DEFAULT FALSE,
    total_score INTEGER NOT NULL DEFAULT 0,
    total_rounds INTEGER NOT NULL DEFAULT 0,
    correct_rounds INTEGER NOT NULL DEFAULT 0,
    play_time_ms INTEGER NOT NULL DEFAULT 0,
    
    INDEX idx_user_id (user_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
```

### **3. game_results 테이블 (공통 데이터만)**
```sql
CREATE TABLE game_results (
    result_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    session_id BIGINT NOT NULL,
    round_number INTEGER NOT NULL,
    is_correct BOOLEAN NOT NULL DEFAULT FALSE,
    score INTEGER NOT NULL DEFAULT 0,
    response_time_ms INTEGER NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_session_id (session_id),
    INDEX idx_session_round (session_id, round_number),
    FOREIGN KEY (session_id) REFERENCES game_sessions(session_id)
);
```

### **4. game_details 테이블 (게임별 상세 정보) ⭐ 핵심**
```sql
CREATE TABLE game_details (
    result_id BIGINT PRIMARY KEY,  -- game_results와 1:1 관계
    game_type VARCHAR(20) NOT NULL,
    param1 INTEGER NULL,            -- 핵심 설정값
    param2 INTEGER NULL,            -- 보조 설정값  
    param3 INTEGER NULL,            -- 성과 측정값
    text_data1 VARCHAR(500) NULL,   -- 문제/정답 데이터
    text_data2 VARCHAR(500) NULL,   -- 사용자 답안
    text_data3 VARCHAR(50) NULL,    -- 카테고리/타입 구분 (중요!)
    
    INDEX idx_result_id (result_id),
    INDEX idx_game_type (game_type),
    INDEX idx_param1 (param1),
    INDEX idx_text_data3 (text_data3),  -- 시퀀스 타입별 검색용
    FOREIGN KEY (result_id) REFERENCES game_results(result_id)
);
```

## 🎮 **게임별 데이터 매핑**

| 게임 | param1 | param2 | param3 | text_data1 | text_data2 | text_data3 |
|------|--------|--------|--------|------------|------------|------------|
| **SEQUENCE** | 시퀀스길이 | 노출시간(ms) | - | 정답순서 | 사용자답안 | **시퀀스타입** |
| **ORIENTATION** | 질문타입ID | - | - | 질문내용 | 사용자답안 | 질문카테고리 |
| **WORD_SEARCH** | 격자크기 | 전체단어수 | 찾은단어수 | 찾을단어들 | 찾은단어들 | 단어테마 |
| **CARD_MATCH** | 총카드수 | 뒤집기횟수 | 맞춘페어수 | - | - | 카드테마 |

### **시퀀스 타입 구분 예시 (text_data3)**
- `ANIMAL`: 고양이, 강아지, 새, 물고기
- `SHAPE`: 원, 사각형, 삼각형, 별
- `COLOR`: 빨강, 파랑, 노랑, 초록
- `NUMBER`: 1, 2, 3, 4

## 🏗️ **실제 구현된 프로젝트 구조**

### **현재 구현 상태 (2025.06.26)**
```
src/main/java/com/ohgiraffers/no_injung/
├── NoInjungApplication.java                     # 메인 애플리케이션
├── user/                                        # 사용자 도메인 ✅
│   ├── entity/User.java                         # 최적화 완료
│   └── ...
└── games/                                       # 게임 도메인
    └── common/                                  # 통합 게임 시스템 ✅
        ├── entity/                              # 엔티티 (최적화 완료)
        │   ├── GameSession.java                # 11개 컬럼
        │   ├── GameResult.java                 # 7개 컬럼 (간소화)
        │   └── GameDetail.java                 # 8개 컬럼 (시퀀스 타입 포함)
        ├── enums/                               # Enum 클래스들 ✅
        │   ├── GameType.java                   # 4개 게임 타입
        │   └── DifficultyLevel.java            # 3단계 난이도
        ├── repository/                          # Repository 인터페이스들 ✅
        │   ├── GameSessionRepository.java      # 세션 관리 + 통계 쿼리
        │   ├── GameResultRepository.java       # 결과 관리 + 분석 쿼리
        │   └── GameDetailRepository.java       # 상세 정보 + 타입별 분석
        ├── service/                             # 비즈니스 로직 ✅
        │   └── GameService.java                # 통합 게임 서비스
        └── controller/                          # API 컨트롤러 ✅
            └── GameController.java             # 통합 게임 API
```

## 🔄 **API 엔드포인트 설계**

### **RESTful API 구조**
```http
# 게임 시작
POST /api/games/start
{
  "userId": 123,
  "gameType": "SEQUENCE",
  "difficultyLevel": "EASY"
}

# 게임 결과 제출
POST /api/games/results  
{
  "sessionId": 456,
  "roundNumber": 1,
  "isCorrect": true,
  "score": 100,
  "responseTimeMs": 2500,
  "gameDetails": {
    "gameType": "SEQUENCE",
    "param1": 4,              // 시퀀스 길이
    "param2": 3000,           // 노출 시간
    "textData1": "cat,dog,bird,fish",     // 정답 순서
    "textData2": "cat,bird,dog,fish",     // 사용자 답안
    "textData3": "ANIMAL"     // 시퀀스 타입 ⭐
  }
}

# 게임 세션 완료
POST /api/games/{sessionId}/complete
{
  "totalScore": 850,
  "totalRounds": 10,
  "correctRounds": 7,
  "playTimeMs": 180000
}

# 사용자별 게임 히스토리
GET /api/games/sessions/{userId}

# 세션별 상세 결과
GET /api/games/{sessionId}/results

# 게임 결과 상세 정보 (시퀀스 타입 등)
GET /api/games/results/{resultId}/details
```

## 🎮 **언리얼 ↔ 백엔드 역할 분담**

### **🎮 언리얼 Engine 담당**
| 기능 | 담당 범위 | 이유 |
|------|-----------|------|
| **카드 매칭 게임** | 카드 배치, 뒤집기 로직, 매칭 검사 | 실시간 인터랙션, 애니메이션 |
| **시각적 표현** | 시퀀스 애니메이션, UI/UX | 사용자 경험 최적화 |
| **멀티플레이** | P2P 연결, 실시간 동기화 | 네트워크 지연 최소화 |

### **🖥️ 백엔드 (Spring Boot) 담당**
| 기능 | 담당 범위 | 이유 |
|------|-----------|------|
| **문제 생성** | 시퀀스 생성, 질문 생성, 격자 생성 | 게임 밸런싱, 난이도 조절 |
| **데이터 저장** | 결과 저장, 통계 분석 | 데이터 무결성, 성과 분석 |
| **사용자 관리** | 회원가입, 로그인, 히스토리 | 보안, 개인정보 관리 |

## 📈 **핵심 분석 기능**

### **시퀀스 타입별 성과 분석**
```sql
-- 사용자별 시퀀스 타입 선호도 분석
SELECT 
    d.text_data3 as sequence_type,
    AVG(CASE WHEN r.is_correct THEN 1.0 ELSE 0.0 END) as success_rate,
    AVG(r.response_time_ms) as avg_response_time,
    COUNT(*) as play_count
FROM game_results r 
JOIN game_details d ON r.result_id = d.result_id
JOIN game_sessions s ON r.session_id = s.session_id
WHERE s.user_id = ? AND d.game_type = 'SEQUENCE'
GROUP BY d.text_data3;
```

### **난이도별 성과 추이**
```sql
-- 시퀀스 길이별 성공률 분석
SELECT 
    d.param1 as sequence_length,
    d.text_data3 as sequence_type,
    AVG(CASE WHEN r.is_correct THEN 1.0 ELSE 0.0 END) as success_rate
FROM game_results r
JOIN game_details d ON r.result_id = d.result_id  
WHERE d.game_type = 'SEQUENCE'
GROUP BY d.param1, d.text_data3
ORDER BY d.param1, success_rate DESC;
```

## 🚀 **개발 현황 및 다음 단계**

### **✅ 완료된 작업**
- [x] DB 설계 최적화 (4개 테이블)
- [x] 엔티티 클래스 구현 (User, GameSession, GameResult, GameDetail)
- [x] Repository 인터페이스 구현 (통계 쿼리 포함)
- [x] 통합 Service 구현 (GameService)
- [x] RESTful API Controller 구현 (GameController)
- [x] 시퀀스 타입 구분 기능 (ANIMAL/SHAPE/COLOR)

### **🔄 진행 중인 작업**
- [ ] 게임별 비즈니스 로직 구현
- [ ] 난이도 조절 알고리즘
- [ ] 언리얼 연동 테스트

### **📋 향후 계획**
- [ ] 통계 대시보드 API
- [ ] 사용자 성과 분석 시스템
- [ ] 개인화 추천 알고리즘

## 💡 **핵심 설계 철학**

1. **최소 복잡성**: 4개 테이블로 모든 기능 구현
2. **빠른 개발**: 통합 서비스로 개발 시간 단축  
3. **확장성**: JSON 없이도 새로운 게임 타입 추가 가능
4. **성능**: 필수 인덱스만으로 조회 최적화
5. **분석 가능**: 시퀀스 타입별 세밀한 성과 분석

**🎯 결과: MVP 완성까지 3시간, 전체 기능 완성까지 2주 예상**