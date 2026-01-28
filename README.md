
# Investment Analysis Backend

개인 투자자의 판단을 기록하고,
기업 이벤트 · 심리 · 판단 기준 · 기다림 전략을 함께 분석하는
투자 의사결정 분석 백엔드 프로젝트입니다.

## Project Day1
- 프로젝트 초기 세팅
- 레이어 및 도메인 구조 설계
- Spring Boot + JPA 기반 실행 환경 구축

## Project Day2
#Event Domain & CRUD API

> 목표: “종목별 기업 이벤트 타임라인(Event)” 도메인을 만들고, 기본 CRUD API(등록/조회/수정/삭제)를 완성한다.  
> Day3~Day8에서 투자 기록/심리/판단 기준 분석과 연결되는 **시간축(Time Axis)** 이 된다.

---

 오늘 한 일 요약

- [x] `Event` 엔티티 설계 (기업 이벤트)
- [x] `EventType` enum 정의
- [x] `EventRepository` 생성 (Spring Data JPA)
- [x] `EventService` 생성 (등록/조회/수정/삭제)
- [x] `EventController` 생성 (REST API)
- [x] DTO 작성 (Request/Response)

---

## Project Day3
# Investment Log Domain & CRUD API

> 목표: “투자 판단 기록(Investment Log)” 도메인을 만들고,  
> 판단 시점의 정보를 저장·조회할 수 있는 기본 CRUD API를 구현한다.  
> Day4~Day8에서 기업 이벤트, 심리 태그, 판단 기준 분석의 **중심 축(Core Domain)** 이 된다.

---

### 오늘 한 일 요약

- [x] `InvestmentLog` 엔티티 설계 (투자 판단 기록)
- [x] `DecisionType` enum 정의 (BUY / SELL / HOLD)
- [x] `InvestmentLogRepository` 생성 (Spring Data JPA)
- [x] `InvestmentLogService` 생성 (등록 / 단건 조회)
- [x] `InvestmentLogController` 생성 (REST API)
- [x] Request / Response DTO 작성
- [x] RESTful API 경로 설계 (`/api/investments`)
- [x] 엔티티 생명주기(`@PrePersist`)를 활용한 생성 시간 관리

---

### 핵심 개념 정리

- Investment Log는 **투자 결과가 아닌, 판단 당시의 정보**를 기록한다.
- 하나의 투자 판단을 “왜, 언제, 어떤 근거로” 내렸는지를 구조화한다.
- 이후 Day4~Day8에서 이벤트(Event), 심리(Psychology), 판단 기준(Criteria) 분석의 기준점이 된다.

---

### 구현 범위

- 투자 기록 생성 (Create)
- 투자 기록 단건 조회 (Read)
- 수정/삭제는 도메인 확장 이후 단계에서 진행

---

### API 요약

| Method | Endpoint | Description |
|------|---------|------------|
| POST | `/api/investments` | 투자 판단 기록 생성 |
| GET | `/api/investments/{id}` | 투자 판단 기록 단건 조회 |

---

### 설계 포인트

- 리소스 중심 API 설계 (명사 + 복수형)
- 판단 시점 데이터만 저장하여 책임 분리
- 비즈니스 로직은 Service 계층에 집중
- 확장 가능한 도메인 구조 유지


---
## Project Day4
# Symbol & Watchlist Domain & CRUD API

> 목표: “투자 분석의 대상이 되는 종목(Symbol)”을 정의하고,  
> JWT 인증 기반 관심종목(Watchlist)을 통해 **사용자별 분석 범위**를 확정한다.  
> 이 도메인은 이후 투자 판단(Decision), 기업 이벤트(Event), 가격 분석의 **기준 축(Core Domain)** 이 된다.

---

### 오늘 한 일 요약

- [x] JWT 인증 기반 사용자 식별 구조 확정 (`@AuthenticationPrincipal`)
- [x] `Symbol` 엔티티 설계 (공용 종목 마스터)
- [x] `Watchlist` 엔티티 설계 (User × Symbol 관계 엔티티)
- [x] `(user_id, symbol_id)` 복합 UNIQUE 제약 적용
- [x] `WatchlistRepository` 생성 (사용자 기준 조회)
- [x] `WatchlistService` 생성 (추가 / 조회 / 삭제)
- [x] `WatchlistController` 생성 (REST API)
- [x] `SymbolResponse` DTO 작성 (Entity 직접 노출 방지)
- [x] 인증 기반 API 접근 제어 (토큰 없을 시 401)

---

### 핵심 개념 정리

- Symbol은 **시장에 존재하는 종목의 공용 마스터 데이터**이다.
- Watchlist는 **사용자와 종목 간의 관계(Entity)** 이다.
- 관심종목은 “분석 대상”을 의미하며, 개인화 분석의 출발점이 된다.
- 사용자 식별은 Request가 아닌 **SecurityContext(JWT)** 기준으로 처리한다.
- 한 사용자는 같은 종목을 **한 번만** 관심종목으로 등록할 수 있다.

---

### 구현 범위

- 종목(Symbol) 등록 및 조회
- 관심종목(Watchlist) 추가
- 사용자별 관심종목 목록 조회
- 관심종목 삭제
- 인증된 사용자만 API 접근 가능

---

### API 요약

| Method | Endpoint | Description |
|------|---------|------------|
| POST | `/api/watchlist/{symbolId}` | 관심종목 추가 |
| GET | `/api/watchlist` | 내 관심종목 목록 조회 |
| DELETE | `/api/watchlist/{symbolId}` | 관심종목 삭제 |

---

### 설계 포인트

- 공용 데이터(Symbol)와 개인화 데이터(Watchlist) 명확히 분리
- 인증 정보는 `@AuthenticationPrincipal`을 통해 주입
- Entity 직접 반환 금지 → Response DTO 사용
- 비즈니스 규칙 `(user_id, symbol_id)` UNIQUE 제약으로 DB 레벨에서 강제
- 확장 가능한 도메인 구조 유지 (Decision / Event / Analysis 연계)

---

##Project Day5

# Company Event Domain & Timeline CRUD API

> 목표: 사용자별 · 종목별 **기업 이벤트(Company Event) 타임라인**을 기록하고,  
> 이후 이벤트 영향 분석(Event Impact), 기다림 시뮬레이션의 **기초 데이터**를 구축한다.  
> 이 도메인은 투자 판단(Decision)과 시장 분석을 연결하는 **시간 축(Time Axis)** 이 된다.

---

## ✅ 오늘 한 일 요약

- [x] `CompanyEvent` 엔티티 설계 (사용자별 기업 이벤트 기록)
- [x] `EventType` enum 정의 (실적, 배당, M&A 등 이벤트 분류)
- [x] 사용자 기준 이벤트 소유권 모델링 (`userId` 참조)
- [x] 종목(Symbol)과의 연관관계 설정 (`ManyToOne`)
- [x] `(user_id, symbol_id, event_at)` 복합 인덱스 적용
- [x] `CompanyEventRepository` 생성 (타임라인 조회 / 소유권 검증)
- [x] `CompanyEventService` 생성 (CRUD 비즈니스 로직)
- [x] `CompanyEventController` 생성 (REST API)
- [x] `CompanyEventResponse` DTO 작성 (Entity 직접 노출 방지)
- [x] JWT 인증 기반 사용자 이벤트 접근 제어

---

## 🧠 핵심 개념 정리

- CompanyEvent는 **“사용자가 기록한 기업 단위 이벤트”** 이다.
- 이벤트는 항상 **사용자(User) + 종목(Symbol)** 기준으로 관리된다.
- 이벤트는 분석의 대상이므로 **수정은 허용하되, 최소화**하는 것을 원칙으로 한다.
- 이벤트는 가격 분석, 판단 분석보다 **선행되는 원천 데이터**다.
- 사용자 식별은 Request 파라미터가 아닌 **JWT 인증 정보**를 기준으로 처리한다.

---

## 🧱 구현 범위

- 기업 이벤트 생성
- 사용자 + 종목 기준 이벤트 타임라인 조회 (최신순)
- 기업 이벤트 수정
- 기업 이벤트 삭제
- 인증된 사용자만 이벤트 접근 가능

---

## 🌐 API 요약

| Method | Endpoint | Description |
|------|---------|------------|
| POST | `/api/events` | 기업 이벤트 생성 |
| GET | `/api/events/timeline?symbolId={symbolId}` | 종목별 이벤트 타임라인 조회 |
| PATCH | `/api/events/{eventId}` | 기업 이벤트 수정 |
| DELETE | `/api/events/{eventId}` | 기업 이벤트 삭제 |

---

## 🧩 설계 포인트

- 사용자(User)는 엔티티 연관관계를 맺지 않고 `userId`로 참조  
  → 기록/로그 성격 도메인에 적합한 설계
- 종목(Symbol)은 핵심 도메인이므로 `ManyToOne` 연관관계 유지
- `(user_id, symbol_id, event_at)` 복합 인덱스로  
  타임라인 조회 성능 최적화
- 수정/삭제 시 `eventId + userId` 조건으로  
  **소유권을 DB 조회 단계에서 검증**
- Entity 직접 반환 금지 → Response DTO 사용

---

## 🎯 Day5 한 줄 요약

> 사용자별 기업 이벤트를 타임라인 형태로 기록하여,  
> 이후 이벤트 영향 분석과 투자 판단 분석을 위한 시간 축 데이터를 구축했다.


# 📘 Project Day6 – Decision(판단) 기록 & 심리 태그

## 🎯 Day6 목표
- 사용자의 **투자 판단(Decision)** 을 기록한다
- 판단 시점의 **심리 상태(Emotion Tag)** 를 함께 저장한다
- 인증된 사용자 기준으로 **본인 데이터만 조회/수정/삭제** 가능하게 한다
- Postman으로 전체 흐름을 직접 검증한다

---

## 🧠 핵심 컨셉

> “이 사용자가, 이 시점에, 이 종목에 대해  
> 어떤 판단을 했고, 어떤 감정 상태였는가?”

Decision은 이후
- 감정 분석
- 판단 일관성
- 기다림/후회 분석  
  의 **중심 데이터**가 된다.

---

## 🧩 Domain 구성

### Decision Entity
- 투자 판단의 핵심 엔티티

**주요 필드**
- `id`
- `user` (판단한 사용자)
- `symbol` (판단 대상 종목)
- `type` (BUY / SELL / HOLD)
- `emotions` (Set\<EmotionTag\>)
- `confidence` (확신 정도, Integer)
- `reason` (판단 이유)
- `decidedAt` (판단 시각)

---

### EmotionTag (Enum)
판단 당시의 심리 상태를 표현

예시:
- `FEARFUL`
- `ANXIOUS`
- `CALM`
- `CONFIDENT`

Decision ↔ EmotionTag 는 **다대다(M:N)** 관계로 관리된다.

---

# Project Day7 — Decision × CriteriaTag 설계

## 🎯 목표

투자 판단(Decision)에  
**“왜 그런 판단을 했는가?”** 를 구조적으로 남기기 위해  
판단 기준(CriteriaTag)을 도입하고,  
Decision ↔ CriteriaTag를 **연결 엔티티(DecisionCriteria)** 로 설계한다.

이 구조를 통해 이후 **기준별 성과 분석 / 기준 진화 분석**이 가능해진다.

---

## 🧩 도메인 구성

### 1️⃣ Decision (투자 판단)

- 사용자가 특정 종목에 대해 내린 **하나의 판단 기록**
- 주요 정보
  - BUY / SELL / HOLD
  - confidence (확신도)
  - reason (판단 이유)
  - emotions (감정 태그)
- 하나의 Decision은 **여러 개의 판단 기준**을 가질 수 있음

---

### 2️⃣ CriteriaTag (판단 기준 사전)

- 판단에 사용되는 **기준의 정의**
- 예시
  - 실적성장
  - 저평가
  - 기술적돌파
- 특징
  - 전역에서 공유되는 기준 사전
  - `name`으로 의미를 표현
  - `UNIQUE(name)` 제약으로 중복 방지

---

### 3️⃣ DecisionCriteria (연결 엔티티)

- Decision ↔ CriteriaTag 를 연결하는 **중간 엔티티**
- 의미
  - “이 판단에 이 기준이 실제로 사용되었다”
- 특징
  - `(decision_id, criteria_tag_id)` 복합 UNIQUE 제약
  - 중복 연결 방지
  - 향후 가중치, 점수, 메모 등 확장 가능

---

## 🔗 엔티티 관계

```text
Decision (1)
   |
   | 1:N
   |
DecisionCriteria
   |
   | N:1
   |
CriteriaTag
