# 🌈 전국 관광지 검색 서비스
* 전국 관광지 명을 검색하면 한국관광공사 API를 통해 가져온 해당 관광지의 정보와 기상청 API를 통해 가져온 3일치의 날씨를 보여주어 전국 여행을 계획할 수 있게 도와드리는 서비스입니다.

### 기능
* 인증
  * 회원가입, 로그인(JWT), 로그아웃(Redis)
* 게시판 및 댓글
  * 각 게시글에 대한 댓글 CRUD, JPA Pagination, 게시글 최신순 및 좋아요순 정렬, 게시글 검색, 조회수, 좋아요
* 홈 및 검색
  * 찜 & 리뷰 많은 순 Top 10, 관광지명 또는 지역명으로 검색
* 관광지 상세페이지 및 리뷰
  * 위도 및 경도를 이용한 해당 지역의 3일치 날씨(기상청 API), 해당 관광지의 정보(한국관광공사 API), 각 관광지 페이지에 대한 리뷰 CRUD, JPA Pagination, 찜하기
* 마이페이지
  * 비밀번호 변경, 회원탈퇴, 내가 찜한 관광지 목록, 내가 쓴 리뷰 목록

### Back-End
* JDK 11, Spring Boot 2.7.8, Gradle, IntelliJ 2022.3
* MySQL, JPA, Lombok, AOP, JUnit, Spring Security, JWT, Redis, HTTPS SSL, CORS
* AWS EC2, AWS RDS, Git/Github, GitKraken, Postman
* AWS 배포 링크
 * https://54.180.213.159/

### Front-End
* React.js, Recoil, Axios, Styled Components
* Netlify
  * https://travel-ateam.netlify.app/
* Repository
  * https://github.com/fastcampus-febe/TeamA-FE

### Open API
* 기상청_단기예보 ((구)_동네예보) 조회서비스 
https://www.data.go.kr/data/15084084/openapi.do
* 한국관광공사_국문 관광정보 서비스_GW
https://www.data.go.kr/data/15101578/openapi.do

### WBS (Google Spread Sheet)
* https://docs.google.com/spreadsheets/d/13Uhy4D88h1SoyTVAYitUmI0FKBMYtotlbUUkHzAbPBk/edit#gid=1503897991

### Postman API Documentation
* https://documenter.getpostman.com/view/24159832/2s935hRSo5#7d0eba59-6283-42dd-8fa0-06e71af4caf7
