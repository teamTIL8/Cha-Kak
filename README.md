# 멋쟁이사자처럼 웹 프로젝트
# 🚗 차칵!

```
불법 주정차 제보 게시판

Kakao 지도 API를 활용하여 제보 위치를 기록하고,
Spring Boot + Thymeleaf + JWT 인증을 통해 안전하게 운영되는
간단한 불법 주정차 제보 게시판입니다.
```

<br>

---

<br>

## 📍 프로젝트 소개

**차칵!** 은 누구나 불법 주정차 차량을 간단히 제보하고, 위치와 사진을 공유할 수 있는 **주정차 제보 게시판**입니다.

수집된 제보 데이터는 시각화되어 보다 안전한 도시 교통 환경을 조성하고, 도시 정책 수립에 참고 가능한 시민 참여형 플랫폼을 구축을 목표로 합니다.

<br>

---

<br>

## 👤 역할 분담
<table>
  <tr>
    <th>API 모듈</th>
    <th>담당자</th>
  </tr>
  <tr>
    <td>사용자 신고접수, 파일 처리 API</td>
    <td>황인선</td>
  </tr>
  <tr>
    <td>사용자 신고 조회, 인터랙션 API</td>
    <td>김지우</td>
  </tr>
  <tr>
    <td>관리자 신고 처리, 상태 관리 API</td>
    <td>신유경</td>
  </tr>
  <tr>
    <td>회원/권한 관리, 인증/인가 API</td>
    <td>윤혜진</td>
  </tr>
  <tr>
    <td>통계, 시스템 설정 관리 API</td>
    <td>심우석</td>
  </tr>
  <tr>
    <td>데이터베이스 설계, 공통 모듈</td>
    <td>최은수</td>
  </tr>
</table>

<br>

---

<br>

## ⚙️ 기술 스택

### Language & Framework
![Java](https://img.shields.io/badge/Java-21-blue?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?logo=springboot)

### Database & ORM
![MySQL](https://img.shields.io/badge/MySQL-8.x-blue?logo=mysql)
![JPA](https://img.shields.io/badge/Spring%20Data%20JPA-Enabled-blueviolet)

### Security
![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT-yellowgreen?logo=spring)
![Validation](https://img.shields.io/badge/Validation-Bean%20Validation-green?logo=spring)
![JavaMail](https://img.shields.io/badge/JavaMail-Enabled-lightgrey?logo=gmail)

### Template & API
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.x-brightgreen?logo=thymeleaf)
![Kakao Maps API](https://img.shields.io/badge/Kakao%20Maps-JS%20API-orange?logo=kakao)

### etc
![Lombok](https://img.shields.io/badge/Lombok-Enabled-red?logo=java)

### Collaboration & Tool
![Figma](https://img.shields.io/badge/Figma-Design-blueviolet?logo=figma&logoColor=white)
![Eclipse](https://img.shields.io/badge/Eclipse-IDE-2C2255?logo=eclipseide&logoColor=white)
![Git](https://img.shields.io/badge/Git-Version%20Control-F05032?logo=git&logoColor=white)

<br>

---

<br>

## 📝 주요 기능

### 🧑 사용자 기능
- 회원가입 및 이메일 인증
- JWT 기반 로그인/로그아웃
- 불법 주정차 차량 제보 등록/수정/삭제
    - 사진: 로컬 디렉토리
    - 위치: Kakao Maps JavaScript API 사용
    - 설명: 차량번호, 유형, 상세 설명 등록
- 제보글 목록 및 상세 정보 조회
    - 전체 제보글 목록 조회/검색
    - 필터링: 차량 번호, 위치, 신고일 기준
    - 마이페이지를 통해 내가 제보한 글, 반응한 글 목록, 댓글 관리

<br>

### ⛑️ 관리자 기능
- 전체 제보 리스트 조회/상세 분석
- 제보된 차량 번호 중복 이력 조회
- 지역/시간대/유형 기반 통계 확인
- 공지사항 및 가이드 등록/조회/수정/삭제

<br>

### 📊 통계 기능
- 지역별 제보 히트맵 시각화
- 차량 번호별 누적 제보 수 차트
- 시간대별/요일별 제보 빈도 분석
- 유형별 제보 비율

<br>

---

<br>

## 🚩 프로젝트 설치 및 실행

### 1. 프로젝트 클론
```bash
git clone https://github.com/user_name/project_name.git
cd project_name
```

### 2. DB 및 환경설정
(프로젝트 완성 후 추가 작성)

### 3. 프로젝트 빌드 및 실행
```bash
mvn clean install         # Maven 빌드
mvn spring-boot:run       # Spring Boot 실행
```

### 4. 접속
> 기본 포트 : http://localhost:8080
(프로젝트 완성 후 추가 작성)