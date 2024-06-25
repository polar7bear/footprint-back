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
- 손승기 - DB 설계, 일정 CRUD, 여행 일정 복사 기능, 스프링 시큐리티 설정, 회원가입 및 회원탈퇴, Jwt 기반 로그인 및 로그아웃, Kakao OAuth 로그인, AWS와 Github Action을 활용하여 CI/CD 파이프라인 구축 및 배포
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
- **DevOps** : Krampoline, Grafana, Prometheus, AWS EC2, S3, Cloudfront
- **버전 및 이슈관리** : Guthub, Jira
- **협업 툴** : Confluence, Discord
- **API 도구** : Swagger

<br>

## ERD 설계
![image](https://github.com/polar7bear/footprint-back/assets/124570553/b3eb2f4d-db83-4c15-9d0f-6ca91cbc1c3c)

<br>

## API 명세

| 기능             | 유형   | 메소드 | URI              | 요청 데이터                                | 응답 코드     | 예시 응답 |
|------------------|--------|--------|------------------|-------------------------------------------|---------------|-----------|
| 회원 가입        | 회원   | POST   | /api/signup      | `{ "email": String, "password": String, "nickname": String }` | 201 Created  | `{ "email" : "abcdefg@naver.com", "nickname" : "nickname", "password" : "암호화된 문자열"}` |
| 로그인           | 회원   | POST   | /api/login       | `{ "email": String, "password": String }` | 200 OK        | `{ "email" : "abcdefg@naver.com", "accessToken" : "accessToken123123", "refreshToken" : "refreshToken123123", "expire" : 86400 }` |
| 로그아웃         | 회원   | POST   | /api/logout      | `{ "refreshToken" : "refreshToken123123" }`| 200 OK        |           |
| 토큰 재발급     | 회원   | POST   | /api/refresh     | `{ "refreshToken" : "refreshToken123123" }`| 200 OK        | `{ "accessToken" : "accessToken123123" }` |
| 회원 탈퇴        | 회원   | DELETE | /api/delete      | `{ "password" : String }`                  | 200 OK        | `{ "userId" : 1, "password" : "암호화된 문자열" }` |
| 프로필 작성      | 회원   | POST   | /api/profile     | `{ "memberId": Long, "nickname": String, "kakaoId": String, "image": String }` | 201 Created  |           |
| 프로필 수정      | 회원   | PUT    | /api/profile     | `{ "nickname": String, "kakaoId": String, "image": String }` | 200 OK        | `{ "message": String }` |
| 프로필 조회      | 회원   | GET    | /api/profile/{userId} |                                         | 200 OK        | `{ "memberId": "long", "email": "string", "nickname": "string", "kakaoId": "string", "image": "string"}` |
| 일정 추가        | 회원   | POST   | /api/plans       | `{ "memberId": Long, "planTitle": String, "startDate": DateTime, "endDate": DateTime, "region": String, "visible": Boolean, "copyAllowed": Boolean }` | 201 Created  | `{ "planId": Long, "message": String }` |
| 일정 상세 조회   | 회원   | GET    | /api/plans{planId} |                                          | 200 OK        | `{ "planId": Long, "memberId": Long, "planTitle": String, "startDate": DateTime, "endDate": DateTime, "region": String, "schedules": List<Schedule>, "visible": Boolean, "copyAllowed": Boolean }` |
| 공개된 일정 조회 | 사용자 | GET    | /api/plans       | `{ "page": 0, "size": 1, "sort": ["string"] }` | 200 OK        | `{ "totalElements": 0, "totalPages": 0, "first": true, "last": true, "size": 0, "content": [], "number": 0, "sort": { "empty": true, "sorted": true, "unsorted": true }, "numberOfElements": 0, "pageable": { "offset": 0, "sort": { "empty": true, "sorted": true, "unsorted": true }, "pageNumber": 0, "pageSize": 0, "paged": true, "unpaged": true }, "empty": true }` |
| 자신의 일정 조회 | 회원   | GET    | /api/my/plans    |                                           | 200 OK        | `[ { "planId": Long, "memberId": Long, "planTitle": String, "startDate": DateTime, "endDate": DateTime, "region": String, "visible": Boolean, "copyAllowed": Boolean }, ... ]` |
| 일정 수정        | 작성자 | PUT    | /api/plans       | `{ "planId": Long, "planTitle": String, "startDate": DateTime, "endDate": DateTime, "region": String, "visible": Boolean, "copyAllowed": Boolean }` | 200 OK        | `{ "message": String }` |
| 일정 삭제        | 작성자 | DELETE | /api/plans/{planId} |                                         | 200 OK        | `{ "message": String }` |
| 상세일정 작성    | 작성자 | POST   | /api/schedules{planId} | `{ "day": Int, "scheduleContent": String, "scheduleCost": Int , "schedulePlace": String }` | 201 Created  | `{ "scheduleId": Long, "message": String }` |
| 상세일정 조회    | 작성자 | GET    | /api/schedules/{scheduleId} |                                      | 200 OK        | `{ "scheduleId": Long, "planId": Long, "day": Int, "scheduleContent": String, "scheduleCost": Int, "schedulePlace": String }` |
| 상세일정 수정    | 작성자 | PUT    | /api/schedules   | `{ "scheduleId": Long, "day": Int, "scheduleContent": String, "scheduleCost": Int, "schedulePlace": String }` | 200 OK        | `{ "message": String }` |
| 상세일정 삭제    | 작성자 | DELETE | /api/schedules/{scheduleId} |                                      | 200 OK        | `{ "message": String }` |
| 장소 추가        | 작성자 | POST   | /api/places/{scheduleId} | `{ "kakaoPlaceId": String, "placeName": String, "latitude": Double, "longitude": Double, "address": String, "visitTime": Time }` | 201 Created  | `{ "placeId": Long, "message": String }` |
| 장소 세부 정보 추가 | 작성자 | POST   | /api/places/{placesId} | `{ "memo": String, "cost": Int }`       | 201 Created  | `{ "placeDetailId": Long, "message": String }` |
| 일정 즐겨찾기    | 회원   | POST   | /api/bookmark/{planId} |                                         | 201 Created  | `{ "bookmarkId": Long, "planId": Long, "memberId": Long }` |
| 즐겨찾기 삭제    | 회원   | DELETE | /api/bookmark/{planId} |                                         | 200 OK        | `{ "message": String }` |
| 즐겨찾기한 목록 조회 | 회원   | GET    | /api/my/bookmarks |                                         | 200 OK        | `[ { "bookmarkId": Long, "planId": Long, "memberId": Long, "planTitle": String, "startDate": DateTime, "endDate": DateTime, "region": String }, ... ]` |
| 일정 복사        | 회원   | POST   | /api/copy/{planId} |                                         | 201 Created  | `{ "copyId": Long, "originalPlanId": Long, "copiedPlanId": Long, "message": String }` |
| 일정 좋아요      | 회원   | POST   | /api/plans/like/{planId} |                                         | 201 Created  |           |
| 좋아요한 목록 조회 | 회원   | GET    | /api/plans/likes |                                         | 200 OK        | `[ { "likeId": "Long", "planId": "Long", "memberId": "Long", "planTitle": "String", "startDate": "DateTime", "endDate": "DateTime", "region": "String" }, ... ]` |
| 리뷰 작성        | 회원   | POST   | /api/reviews     | `{ "memberId": 0, "title": "string", "content": "string", "imageIds": [ 0 ] }` | 201 Created  | `1 // 리뷰 id` |
| 리뷰 상세 조회   | 작성자 | GET    | /api/reviews/{reviewId} |                                         | 200 OK        | `{ "memberId": 0, "title": "string", "content": "string", "images": [ 0 ] }` |
| 자신의 리뷰 조회 | 작성자 | GET    | api/my/reviews   | `memberId : 회원id page: 받아올 번호의 page size: 한 페이지에 들어갈 글 개수, 사이즈` | 200 OK        | `{ "totalElements": 0, "totalPages": 0, "size": 0, "content": [ { "memberId": 0, "reviewId": 0, "title": "string", "previewImageUrl": "string" } ], "number": 0, "sort": { "empty": true, "sorted": true, "unsorted": true }, "first": true, "last": true, "numberOfElements": 0, "pageable": { "offset": 0, "sort": { "empty": true, "sorted": true, "unsorted": true }, "pageNumber": 0, "pageSize": 0, "paged": true, "unpaged": true }, "empty": true }` |
| 리뷰 수정        | 작성자 | PUT    | /api/reviews     | `{ "reviewId": 0, "memberId": 0, "title": "string", "content": "string", "imageIds": [ 0 ] }` | 200 OK        | `{ "SUCCESS" }` |
| 리뷰 삭제        | 작성자 | DELETE | /api/reviews/{reviewId} |                                         | 200 OK        | `{ "SUCCESS" }` |
| 리뷰 좋아요      | 회원   | POST   | /api/reviews/like/{reviewId} |                                         | 201 Created  | `{ "likeId": Long, "message": String }` |
| 좋아요한 목록 조회 | 회원   | GET    | /api/reviews/likes |                                         | 200 OK        | `[ { "likeId": Long, "reviewId": Long, "memberId": Long, "reviewTitle": String, "reviewContent": String }, ... ]` |
| 검색             | 회원   | POST   | /api/search      | `{ "word" : String, "type" : String }`    | 200 OK        |           |
| 리뷰 정렬        | 회원   | GET    | /api/reviews     | `{ "sort": String }`                      | 200 OK        |           |

<br>

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


