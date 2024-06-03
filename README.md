# 여행 공유 서비스 FootPrint

**배포 링크** : https://ke4f765103c24a.user-app.krampoline.com

## 프로젝트 소개
- 여행 일정이나 리뷰를 작성하고 공유할 수 있습니다.
- 다른 사람의 여행일정을 copy하여 자신의 여행 스타일대로 수정하여 계획할 수 있습니다.

## 개발 기간
2024.03.01 ~ 2024.04.21

## 팀원 구성
### 백엔드
- 강원빈 (팀장) - 크램폴린 세팅 및 서버 배포, prometheus 및 grafana 세팅, 백엔드 초기 설정
- 강화석 - 일정조회 및 정렬, 일정 검색 및 정렬, 일정 즐겨찾기 및 좋아요 기능, 사용자 일정 관리
- 손승기 - DB 설계, 일정 CRUD, 여행 일정 복사 기능, 스프링 시큐리티 설정, 회원가입 및 회원탈퇴, Jwt 기반 로그인 및 로그아웃, Kakao OAuth 로그인
- 신서연 - 리뷰 CRUD, 이미지 기능 구현, AWS S3 관리 및 연결

### 프론트엔드
- 김은솔 (PM) - 메인 & 일정생성 등 UI 구성, 임시 mirage API, 일정생성 기능구현
- 김주원 - 마이페이지, 리뷰 추가 페이지 UI 구성, 일정불러오기 UI, 리뷰 API 연결, 카카오맵 API 구현, 무한스크롤 구현

## 백엔드 개발 환경 및 기술 스택

- **Language** : Java 21
- **Build** : Gradle
- **Framework** : Spring Boot 3.2.3
- **Library** : Spring Data JPA, Querydsl, Spring Security
- **Database** : MySQL
- **DevOps** : Krampoline, Grafana, Prometheus
- **버전 및 이슈관리** : Guthub, Jira
- **협업 툴** : Confluence, Discord
- **API 도구** : Swagger

## 서비스 아키텍처
![image](https://github.com/dogfoot-birdfoot/footprint-back/assets/124570553/1557c79e-401c-48cc-beaf-2bc432a29b55)

## 시연 영상

### 메인 페이지
![메인페이지](https://github.com/dogfoot-birdfoot/footprint-front/assets/86706630/0da2f21a-8bcf-4970-ba1e-dc0c2dd05a5a)

### 어행 일정 작성
![일정 생성 페이지](https://github.com/dogfoot-birdfoot/footprint-front/assets/86706630/2234c7d5-2b79-4207-9b67-e4dd6582249b)

### 리뷰 작성
![리뷰 작성페이지](https://github.com/dogfoot-birdfoot/footprint-front/assets/86706630/d25f862d-6e73-47bf-b393-ee0103439582)

### 일정 공유 페이지
![일정 공유 페이지](https://github.com/dogfoot-birdfoot/footprint-front/assets/86706630/c6a521d0-63fb-4074-886b-8f48ff081761)


### 리뷰 공유 페이지
![리뷰 공유 페이지](https://github.com/dogfoot-birdfoot/footprint-front/assets/86706630/e2e6a1e5-c7ae-4d5c-bb44-838eaeb447fe)

### 검색 페이지
![검색 페이지](https://github.com/dogfoot-birdfoot/footprint-front/assets/86706630/c985b1a9-253f-4534-aa0d-1ffd65bba810)

### 마이 페이지
![마이페이지](https://github.com/dogfoot-birdfoot/footprint-front/assets/86706630/3f406f30-9000-4b38-a604-c7a9e15c72fe)


### 회원가입 및 로그인
![회원가입 및 로그인](https://github.com/dogfoot-birdfoot/footprint-front/assets/86706630/7ef2f6b4-7667-4c9a-947d-dad943393bf6)

<br/>

![녹화_2024_04_23_02_49_32_196](https://github.com/dogfoot-birdfoot/footprint-front/assets/86706630/72d819b9-cdde-4648-ba7b-c9c4dece4ce9)


