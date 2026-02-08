
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

```
---

# Project Day 8 — MarketSnapshot Domain (시장 스냅샷)

> 목표: **의사결정(Decision) 시점의 시장 상태(가격·지표)를 정량적으로 기록**한다.  
> 감정/기준(EmotionTag, CriteriaTag)처럼 주관적 요소뿐 아니라, **팩트 데이터(가격/지표)**를 함께 남겨 이후 분석(성과/패턴/오판 원인)을 가능하게 한다.

---

## ✅ 오늘 구현한 것 (Checklist)

- [x] `Symbol` ↔ `MarketSnapshot` 연관관계 설계 (**1 Symbol : N Snapshots**)
- [x] `MarketSnapshot` 엔티티 생성 (가격/지표 + capturedAt)
- [x] `MarketSnapshotRepository` 생성
  - [x] 특정 종목의 **최신 스냅샷 1개 조회**
- [x] `MarketSnapshotService` 생성
  - [x] 스냅샷 저장
  - [x] 최신 스냅샷 조회
- [x] `MarketSnapshotController` 생성
  - [x] `POST /api/snapshots/{symbolId}` 저장 API
  - [x] `GET /api/snapshots/{symbolId}/latest` 최신 조회 API
- [x] 기본 예외 처리 구성 (`ApiException`, `ErrorCode`, `GlobalExceptionHandler`)

---

## 🧩 도메인 설계

### 1) Symbol ↔ MarketSnapshot 관계

- `Symbol` : **분석 대상(종목)**
- `MarketSnapshot` : **특정 시점(capturedAt)의 시장 상태 기록**

관계는 아래처럼 정의한다:

- 하나의 `Symbol`은 **여러 개의 `MarketSnapshot`**을 가진다.
- 하나의 `MarketSnapshot`은 **반드시 하나의 `Symbol`**에 속한다.

```java
@ManyToOne(fetch = FetchType.LAZY, optional = false)
@JoinColumn(name = "symbol_id", nullable = false)
private Symbol symbol;

```
---

# 📅 Project Day 9
## Waiting Simulation (기다림 시뮬레이션)

---

## 🎯 목표

투자 판단 시점에 바로 행동하지 않고  
**N일을 더 기다렸다면 수익률이 어떻게 달라졌을지**를 계산한다.

이 기능은 단순한 투자 결과 기록이 아니라,  
**투자 판단의 대안 시나리오(if)** 를 수치로 분석하는 데 목적이 있다.

---

##  왜 필요한가?

일반적인 투자 기록 앱은 다음 정보만 남긴다.

- 언제 샀는가
- 얼마에 샀는가
- 결과가 어땠는가

하지만 이 프로젝트는 다음 질문에 답한다.

> **“그때 바로 행동하지 않고 기다렸다면 더 나았을까?”**

이를 위해서는  
**과거 시점의 시장 상태를 정확히 복원**할 수 있어야 하며,  
이 역할을 `MarketSnapshot`이 담당한다.

---

##  핵심 도메인 구조

## 📌 MarketSnapshot 도메인

**특정 시점의 시장 상태를 그대로 저장하는 불변 데이터**

### 역할
- 과거 가격 및 지표 복원
- 판단 시점과 결과 시점을 시간 기준으로 비교
- 이후 모든 분석(Event Impact, Emotion 분석)의 기준 데이터

### 주요 필드
```java
Symbol symbol;              // 종목
BigDecimal price;           // 가격
BigDecimal changeRate;      // 등락률
BigDecimal per;             // PER
BigDecimal pbr;             // PBR
BigDecimal roe;             // ROE
BigDecimal rsi;             // RSI
LocalDateTime capturedAt;   // 스냅샷 시점

```

---

## Project Day 11- Event Impact 분석

### 목표
기업 이벤트가 실제 가격에 어떤 영향을 미쳤는지를
정량적으로 분석하여 기록한다.

### 구현 내용
- EventImpact 엔티티 설계
- 이벤트 전/후 가격 기반 변화율 계산
- 분석 기간(windowDays)별 Impact 저장
- EventImpact API 구현

### 핵심 개념
- Event = 원인
- MarketSnapshot = 시계열 가격 데이터
- EventImpact = 결과(영향 분석)

### 기대 효과
- 이벤트의 의미를 숫자로 검증
- 투자 판단의 사후 평가 데이터 확보

---
## Project Day10 – Emotion Analytics

### Goal
Analyze how emotional states at decision time affect investment performance.

### Key Features
- Emotion-based win/loss statistics
- Average return rate per emotion
- Win rate calculation
- User-specific analytics

### API
GET /api/analytics/emotions

### Insights Example
- FEAR → 낮은 승률, 음수 평균 수익률
- CONFIDENCE → 높은 평균 수익률
- IMPULSIVE → WaitingSimulation 대비 성과 열위

### Meaning
This system transforms raw investment logs into behavioral insights.

---

## Project Day11 – Emotion Analytics

### Goal
Analyze how emotional decisions affect investment outcomes.

### Implemented
- Emotion-based aggregation analytics
- Total / Win / Loss count
- Average return rate
- Win rate calculation

### Endpoint
GET /api/analytics/emotions

### Tech Highlights
- Spring Data JPA Projection
- JPQL aggregation
- Service-layer business calculation

---

## Project Day12 – Analytics Execution Pipeline

> 투자 판단(Decision) 데이터를 기반으로  
> **분석 결과를 계산 → JSON 스냅샷으로 저장 → API로 실행/조회**할 수 있는  
> Analytics 실행 파이프라인을 구축한다.

---

### 내용

- 판단 데이터(Decision)를 기반으로 분석 로직 실행
- 계산 결과를 **JSON 형태로 직렬화하여 DB에 저장**
- 분석 실행을 하나의 파이프라인으로 묶어 API로 제공
- 향후 분석 확장을 고려한 구조 설계

---
# Project Day13 – Core Tests (Decision & Auth/JWT)

 
Decision 도메인의 핵심 비즈니스 로직과  
Auth + JWT 인증 흐름을 **실제 서비스와 동일한 방식으로 검증**한다.

---

##  테스트 목표

### 1. Decision Domain
- 투자 판단(Decision) 생성 로직 검증
- CriteriaTag 연결 및 중복 제거 검증
- Emotion 값 null 처리 검증
- Criteria 교체(replace) 시
  - 기존 연관관계 삭제(orphanRemoval)
  - 신규 연관관계 재연결
  - DB 기준으로 정확히 반영되는지 검증

### 2. Auth & JWT
- 회원가입(signup) 정상 동작 검증
- 로그인(login) 시 JWT 토큰 발급 검증
- JWT를 통한 보호 API 접근 가능 여부 검증
- 토큰 미전달 시 401 Unauthorized 반환 검증

---


# Project Day14 — Docker 기반 로컬 운영 환경 구축

## 내용
Spring Boot 백엔드 서비스를 **Docker + Docker Compose** 환경에서 실행하고,  
MySQL 컨테이너와 연동하여 **로컬에서도 운영 환경과 동일한 구조**를 완성한다.

---

# Day 15 — AWS Deployment (Production Standard)

## Architecture
- Build & Image: GitHub Actions (CI)
- Registry: Docker Hub
- Runtime: AWS EC2 (run-only)
- DB: AWS RDS MySQL

## Deployment Flow
1. `git push main`
2. GitHub Actions runs:
   - `./gradlew clean build`
   - Docker image build & push
   - SSH to EC2
   - `docker pull` + `docker compose up -d`
3. Verify:
   - `/actuator/health` = UP

## Notes
- EC2 does not build source (prevents OOM on small instances)
- Secrets are managed via GitHub Secrets and/or EC2 `.env`
