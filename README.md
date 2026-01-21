
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