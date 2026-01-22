
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

