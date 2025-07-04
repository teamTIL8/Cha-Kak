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
`src\main\resources` 경로에 `application.properties` 파일 생성 후 아래와 같이 작성
```bash
spring.application.name=ChaKak

# MySQL Datasource
spring.datasource.url=jdbc:mysql://localhost:3306/schema_name?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8&allowPublicKeyRetrieval=true
spring.datasource.username=username
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect

# Logging
logging.level.org.hibernate.SQL=debug
logging.level.org.hibernate.type.descriptor.sql.BasicExtractor=trace

# Thymeleaf Cache
spring.thymeleaf.cache=false

# JWT Configuration
jwt.secret=myVerySecretKeyForJWTTokenGenerationThatShouldBeLongEnough
jwt.expiration=86400000

#server    포트 변경 시, 작성
server.port=port_number
```

<br>

아래의 의존성이 포함되었는지 확인
| Dependency | Version | 기능 설명 |
|------------|---------|-----------|
| `spring-boot-starter-web` | 3.5.0 | 기본 웹 MVC, REST API |
| `spring-boot-devtools` | 3.5.0 | 개발용 자동 재시작 & LiveReload |
| `spring-boot-starter-thymeleaf` | 3.5.0 | Thymeleaf 템플릿 엔진 연동 |
| `spring-boot-starter-data-jpa` | 3.5.0 | Spring Data JPA ORM 사용 |
| `mysql-connector-j` (runtime) | 9.2.0 | MySQL DB 드라이버 |
| `spring-boot-starter-security` | 3.5.0 | Spring Security (인증/인가) |
| `jjwt-api` | 0.12.5 | JWT 토큰 API |
| `jjwt-impl` | 0.12.5 | JWT 내부 구현체 |
| `jjwt-jackson` | 0.12.5 | JWT 파싱 Jackson 모듈 |
| `spring-boot-starter-validation` | 3.5.0 | Bean Validation (입력값 검증) |
| `lombok` (provided) | 1.18.38 | Getter/Setter 등 보일러플레이트 제거 |
| `thymeleaf-extras-springsecurity6` | 3.1.3 | Thymeleaf에서 Security 속성 사용 |


### 3. 프로젝트 빌드 및 실행
```bash
mvn clean install         # Maven 빌드
mvn spring-boot:run       # Spring Boot 실행
```

### 4. 접속
> 기본 포트 : http://localhost:port <br>

---