## 📚 Book Record 프로젝트
- 도서 검색, 리뷰 작성 기능을 포함한 Spring Boot 기반 웹 애플리케이션입니다.  
- Naver 검색 API를 활용하여 도서 정보를 검색할 수 있습니다.
- 로그인 된 회원은 리뷰를 조회하고, 작성할 수 있습니다.

<br>

## ⚙️ 기술 스택
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)<img src="https://img.shields.io/badge/17-515151?style=for-the-badge">
<img src="https://img.shields.io/badge/springboot-%236DB33F.svg?&style=for-the-badge&logo=springboot&logoColor=white" /><img src="https://img.shields.io/badge/3.3.1-515151?style=for-the-badge">
<img src="https://img.shields.io/badge/springsecurity-%236DB33F.svg?&style=for-the-badge&logo=springsecurity&logoColor=white" /><img src="https://img.shields.io/badge/6:3.1.1-515151?style=for-the-badge"><br>
<img src="https://img.shields.io/badge/thymeleaf-%23005F0F.svg?&style=for-the-badge&logo=thymeleaf&logoColor=white" />
<img src="https://img.shields.io/badge/html5-%23E34F26.svg?&style=for-the-badge&logo=html5&logoColor=white" />
<img src="https://img.shields.io/badge/css3-%231572B6.svg?&style=for-the-badge&logo=css3&logoColor=white" />
<img src="https://img.shields.io/badge/javascript-%23F7DF1E.svg?&style=for-the-badge&logo=javascript&logoColor=black" />
<img src="https://img.shields.io/badge/axios-%235A29E4.svg?&style=for-the-badge&logo=axios&logoColor=black" /><br>
<img src="https://img.shields.io/badge/mysql-%234479A1.svg?&style=for-the-badge&logo=mysql&logoColor=white" /><img src="https://img.shields.io/badge/8.0-515151?style=for-the-badge">
<img src="https://img.shields.io/badge/hibernate-%2359666C.svg?&style=for-the-badge&logo=hibernate&logoColor=white" />
![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)<img src="https://img.shields.io/badge/8.13-515151?style=for-the-badge"><br>
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white)

<br>

## ✍️ 프로젝트 아키텍처
<img width="900" height="400" src="https://github.com/user-attachments/assets/8782e662-0415-4b41-be7e-f43c1f4b369a" />

<br>

## 🐳 Docker 구성 방식
![도커구성](https://github.com/user-attachments/assets/0f457c99-fd13-42f8-9b45-807ce6469dca)

<br>

## 📌 API 명세
<img width="788" alt="Image" src="https://github.com/user-attachments/assets/1e4a9a64-7298-431c-b665-58ffcc69ebc9" />

<br>   

## 📂 프로젝트 구조
```
.
├── build.gradle
├── docker-compose.yml
├── Dockerfile
├── gradlew
├── gradlew.bat
├── HELP.md
├── settings.gradle
└── src
├── main
│   ├── java
│   │   └── com
│   │       └── boot
│   │           ├── BookRecordApplication.java
│   │           ├── config
│   │           │   ├── DataInitializer.java
│   │           │   ├── security
│   │           │   │   ├── CustomMemberDetails.java
│   │           │   │   └── CustomMemberDetailsService.java
│   │           │   └── SecurityConfig.java
│   │           ├── controller
│   │           │   ├── BookController.java
│   │           │   ├── IndexController.java
│   │           │   ├── MemberController.java
│   │           │   ├── MemberRestController.java
│   │           │   └── ReviewRestController.java
│   │           ├── dto
│   │           │   ├── BookDTO.java
│   │           │   ├── MemberDTO.java
│   │           │   ├── MemberSignUpDTO.java
│   │           │   ├── MemberUpdateDTO.java
│   │           │   ├── PasswordCheckDTO.java
│   │           │   ├── ReviewBookDTO.java
│   │           │   └── ReviewDTO.java
│   │           ├── entity
│   │           │   ├── BaseTime.java
│   │           │   ├── Book.java
│   │           │   ├── Member.java
│   │           │   └── Review.java
│   │           ├── repository
│   │           │   ├── BookRepository.java
│   │           │   ├── MemberRepository.java
│   │           │   └── ReviewRepository.java
│   │           └── service
│   │               ├── BookService.java
│   │               ├── MemberService.java
│   │               ├── NaverApi.java
│   │               └── ReviewService.java
│   └── resources
│       ├── application.properties
│       ├── static
│       │   ├── assets
│       │   │   └── favicon.ico
│       │   ├── css
│       │   │   └── styles.css
│       │   └── js
│       │       └── detail.js
│       └── templates
│           ├── book
│           │   ├── detail.html
│           │   └── search.html
│           ├── fragments
│           │   ├── footer.html
│           │   └── header.html
│           ├── index.html
│           ├── layouts
│           │   └── default.html
│           └── member
│               ├── login.html
│               ├── mypage.html
│               ├── signup.html
│               └── update.html
└── test
└── java
└── com
└── boot
├── BbApplicationTests.java
├── BookServiceTest.java
├── IndexControllerTest.java
├── MemberServiceTest.java
└── ReviewServiceTest.java
```
<br>

## 🛠️ 주요 기능  

### 🔐 회원 기능 (Spring Security)
- Spring Security 기반의 세션 로그인 방식 사용
- `UserDetails`, `UserDetailsService` 커스터마이징하여 사용자 조회 및 인증 처리
- 이메일을 아이디(`username`)로 사용하며, 비밀번호는 `BCrypt`로 암호화하여 저장
- 로그인 성공/실패 시 `SecurityFilterChain`을 통해 동작 설정
- 로그아웃 시 세션 무효화 및 쿠키 삭제
- 회원가입 및 회원수정 시 `@Valid`와 `BindingResult`를 활용해 서버 측 유효성 검사 수행
- 회원가입 시 이메일 중복 여부 비동기 검사

### 👤 마이페이지
- 로그인한 사용자의 정보를 확인할 수 있는 개인 전용 페이지
- 본인 정보(이름, 비밀번호) 수정 가능
- 내가 작성한 리뷰 목록 확인
- 탈퇴 기능 제공 (세션 만료 및 자동 로그아웃)

### 🔎 도서 검색 (Naver Search API)
  - 사용자가 책 제목이나 저자명으로 검색 시, Naver Open API를 통해 도서 정보 조회
  - 프론트엔드에서 사용자의 입력값을 쿼리 파라미터로 전송하고, 백엔드에서 외부 API(Naver Search API)를 호출
  - Naver API로부터 받은 JSON 응답은 Gson 라이브러리로 파싱하여 DTO로 변환
  - 변환된 도서 목록은 Thymeleaf 템플릿으로 화면에 렌더링되며, 검색 결과를 카드 형식으로 출력
  - 검색 결과의 각 도서는 ISBN 기반으로 상세 페이지 연결

### 📝 리뷰 CRUD
  - 로그인 한 사용자만 리뷰 조회, 작성, 수정, 삭제 가능
  - 도서 상세 페이지에서 해당 도서에 대한 리뷰 작성 가능
  - 리뷰는 최소 50자 이상 입력해야 저장되며, 서버 측 유효성 검사 수행
  - 리뷰 목록은 AJAX를 통해 비동기적으로 요청
  - 본인의 리뷰에만 수정/삭제 버튼이 표시되며, 서버에서 추가 권한 검증
  - 리뷰 목록은 AJAX 기반 페이지네이션을 통해 최신순으로 비동기 조회

<br>

## 🖥️ 화면 구성
#### 📌 메인 · 검색 · 도서 상세

![메인화면](https://github.com/user-attachments/assets/aa0d3b19-dffa-42ec-af5c-0399655736d4)
>**메인화면**
> - 작성된 모든 리뷰가 최신순으로 정렬
> - 로그인 여부와 관계없이 누구나 열람 가능
<br>

![책 검색 목록](https://github.com/user-attachments/assets/70c3c617-ee63-4e55-b727-077ca0237808)
>**도서 검색 결과 화면**
> - 제목 또는 저자명으로 검색 가능
> - 10개 단위로 페이지 처리
> - 도서 정보와 설명을 130자 이하로 축약하여 표시
<br>

![상세페이지](https://github.com/user-attachments/assets/7eade93e-7be7-47b8-9b1f-d229944a84bc)
>**도서 상세 페이지**
> - 도서 정보와 이미지 표시
> - 로그인한 사용자는 리뷰 작성 가능
<br>

#### 📝 리뷰 목록 · 페이징

![리뷰목록](https://github.com/user-attachments/assets/870676ea-6157-441f-8f4c-c58df6765d0f)
> **리뷰 목록**
> - 해당 도서에 등록된 리뷰 목록을 최신순으로 정렬
> - 본인의 리뷰에만 수정/삭제 버튼 표시
> - AJAX를 이용한 비동기 처리
<br>

![페이지네이션](https://github.com/user-attachments/assets/afa6aadb-111d-4576-92a2-cc3ccbaac444)

![페이지네이션](https://github.com/user-attachments/assets/84208a74-80d7-45a5-9cbd-6599e9323ee1)

![페이지네이션](https://github.com/user-attachments/assets/726b4900-183a-4483-9bfd-77efd0928742)
>**페이지네이션**
> - 현재 페이지 기준으로 좌우 이동 가능
> - 메인, 리뷰, 마이페이지 모두에 적용
<br>

#### 🔐 회원 기능

![로그인](https://github.com/user-attachments/assets/b9e95eae-7033-4b83-b9bf-8b9a1785d2e7)
>**로그인**
> - 이메일, 비밀번호로 인증
<br>

![회원가입](https://github.com/user-attachments/assets/b1d946ab-fab5-4bf9-a622-19f41c3c268a)
>**회원가입**
> - 이름, 이메일, 비밀번호 입력  
> - 유효성 검사 및 이메일 중복 검사 수행
<br>

#### ✅ 유효성 검사 예시

![이름유효성검사](https://github.com/user-attachments/assets/901ec53a-7d5a-41e8-a8e8-dfe3a0845aa4)
> 이름 길이 검사 (2~15자 이내)
<br>

![이메일유효성검사](https://github.com/user-attachments/assets/d5a4a4bf-d651-4f6a-96b5-003406d43fa1)
> 이메일 중복 여부 확인
<br>

![비밀번호유효성검사](https://github.com/user-attachments/assets/36dd6fef-7ab6-4c51-8b35-5692ee56e4ff)
> 비밀번호 길이 검사 (8자 이상 20자 이하)
<br>

![비밀번호이메일유효성검사](https://github.com/user-attachments/assets/a3bc987b-7dd3-4cdf-bf23-2e23026b87a3)
> 비밀번호에 이메일 포함 여부 검사
<br>

#### 👤 마이페이지 및 정보 수정

![마이페이지](https://github.com/user-attachments/assets/46192871-86d3-422e-b67d-32b6bd5171fa)
>**마이페이지**
> - 회원 정보 수정 및 탈퇴 가능
> - 본인의 리뷰 목록 확인 가능
<br>

![인증페이지](https://github.com/user-attachments/assets/89e11f76-b767-416e-bced-abc9055ee91b)
>**회원 정보 수정 인증 페이지**
> - 비밀번호 입력을 통한 본인 인증
<br>

![회원정보수정](https://github.com/user-attachments/assets/c10d4a0e-6244-41c1-a665-f03ff47add89)
>**회원 정보 수정 페이지**
> - 이름, 비밀번호 수정 시 유효성 검사 적용
<br>

![개인정보수정유효성검사](https://github.com/user-attachments/assets/705c4f54-c900-42ef-9cb2-caa1022b77bc)
>**회원 정보 수정 유효성 검사 예시**
> - 회원가입 시와 동일한 기준으로 이름/비밀번호 검사 수행
