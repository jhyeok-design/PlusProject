# 돌보미 
아무것도 요구하지 않는 애완 돌을 키워보세요!

## 프로그램 소개 
돌보미는 바쁜 현대인을 위한 애완용 돌 쇼핑 플랫폼입니다.
먹이를 주지 않아도, 산책을 시키지 않아도, 삐지지 않는 완벽한 반려 존재(애완 돌)를 만나보세요.

일상에 작은 즐거움과 위로를 더해주는 애완 돌을 검색하고,
구매하고, 자랑하고, 후기를 남길 수 있는 서비스를 제공합니다.

## 프로젝트 목적
- 반려돌 구매 서비스를 제공하는 웹 사이트의 백엔드 API 개발
- RESTful API 설계 및 구현 역량을 검증하기 위해 진행
- 단순 기능 구현을 넘어서 사용자 경험 향상을 위한 성능 개선 목표

## 개발 기간 
25.12.31 ~ 26.01.08

## 기술 스택 
- Java 17
- Spring Boot 3.5.8
- Postman
- JUnit5
- Docker
- MySQL 8.0
- JPA
- QueryDSL 5.0.0
- Redis 8.4.0

## 프로젝트 구조 
```
├─plusproject
    ├─common
    │  ├─config
    │  ├─entity
    │  ├─enums
    │  ├─exception
    │  ├─filter
    │  ├─model
    │  └─util
    └─domain
        ├─auth
        │  ├─controller
        │  ├─model
        │  │  ├─request
        │  │  └─response
        │  └─service
        ├─comment
        │  ├─controller
        │  ├─entity
        │  ├─model
        │  │  ├─request
        │  │  └─response
        │  ├─repository
        │  └─service
        ├─order
        │  ├─controller
        │  ├─entity
        │  ├─model
        │  │  ├─request
        │  │  └─response
        │  ├─repository
        │  └─service
        ├─post
        │  ├─controller
        │  ├─entity
        │  ├─model
        │  │  ├─request
        │  │  └─response
        │  ├─repository
        │  └─service
        ├─product
        │  ├─controller
        │  ├─entity
        │  ├─model
        │  │  ├─request
        │  │  └─response
        │  ├─repository
        │  └─service
        ├─review
        │  ├─controller
        │  ├─entity
        │  ├─model
        │  │  ├─request
        │  │  └─response
        │  ├─repository
        │  └─service
        ├─search
        │  ├─controller
        │  ├─entity
        │  ├─repository
        │  └─service
        └─user
            ├─controller
            ├─entity
            ├─model
            │  ├─request
            │  └─response
            ├─repository
            └─service
```

## 핵심 기능
### 애완 돌 쇼핑
- 애완 돌 목록 조회
- 애완 돌 상세 정보 확인
- 애완 돌 구매 기능

### 애완 돌 검색
- 키워드 기반 애완 돌 검색
- 인기 애완돌 조회
  
### 게시판 및 리뷰
- 애완돌 자랑 게시판
- 애완 돌 구매 후기 작성

### 회원 기능 
- 회원 가입
- 회원 기반 서비스 이용

### 상품 관리 
- 애완 돌 상품 등록/ 조회/ 수정/ 삭제

## ERD 명세
<img width="2096" height="1126" alt="image" src="https://github.com/user-attachments/assets/fe0e7d23-3dde-4dfa-8696-00619e2f98ce" />

## 와이어 프레임
<img width="1950" height="1172" alt="image" src="https://github.com/user-attachments/assets/4a83d5b1-b4c2-4749-a6c5-44b273ebfd82" />

## 개발자
| 개발자 | 역할 |
|---|---|
| 최정윤 | 주문 CRUD, 인기 검색어, 주문 검색 기능 |
| 김태호 | 게시판 CRUD, 게시판 검색 기능 (제목 및 작성자 검색), 댓글 CRUD |
| 원민영 | 인증/인가, 회원 CRUD, 회원 검색 기능 (회원 이름 및 이메일 검색) |
| 정지원 | 리뷰 CRUD, 인기 리뷰 TOP 10, 내리뷰 전체 조회, 상품별 리뷰 조회 |
| 최정혁 | 상품 CRUD, 동시성 제어, 상품명 검색 기능|

