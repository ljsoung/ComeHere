# CLAUDE.md

## 프로젝트 개요

이 프로젝트는 **대학생 팀원 모집 및 협업 준비 플랫폼**을 제작하는 개인 프로젝트이다.

서비스 목적은 대학생들이 공모전, 졸업프로젝트, 팀플, 스터디, 해커톤 등의 활동을 위해 팀원을 모집하고, 다른 사용자가 모집글에 지원하며, 모집자가 지원자를 관리할 수 있는 **반복 방문형 웹 플랫폼**을 만드는 것이다.

단순 게시판이 아니라 아래 흐름을 지원하는 **서비스형 웹사이트**를 목표로 한다.

- 회원가입 / 로그인
- 모집글 작성
- 모집글 조회 / 검색 / 필터
- 모집글 지원
- 지원 승인 / 거절
- 북마크
- 댓글
- 마이페이지에서 내 활동 관리

핵심은 사용자가 직접 사이트에 접속해서 지속적으로 활동하게 만드는 것이다.

---

## 프로젝트 목표

이 프로젝트의 목표는 다음과 같다.

1. 단순 CRUD 게시판이 아닌 **실사용 가능한 플랫폼형 웹사이트**를 만든다.
2. 백엔드 중심 포트폴리오로서 인증/인가, 도메인 설계, API 문서화, 배포, CI/CD까지 경험한다.
3. React + Spring Boot 기반의 **프론트엔드/백엔드 분리형 구조**를 구현한다.
4. 나중에 Docker, Jenkins, Redis, AWS Lightsail까지 확장 가능한 구조로 설계한다.

---

## 주요 사용자

### 1. 비회원
- 모집글 목록 조회
- 모집글 상세 조회
- 검색 및 필터 사용

### 2. 일반 회원
- 회원가입 / 로그인
- 모집글 작성
- 모집글 지원
- 북마크
- 댓글 작성
- 마이페이지 조회 / 수정

### 3. 모집글 작성자
- 본인이 작성한 모집글 수정 / 삭제
- 지원자 목록 조회
- 지원자 승인 / 거절
- 모집 상태 변경

### 4. 관리자
현재 MVP에서는 제외한다.
추후 확장 시 신고 처리, 게시글 블라인드, 사용자 제재 기능 등을 추가할 수 있다.

---

## 핵심 기능 범위

### MVP에 포함할 기능

#### 1. 회원 관리
- 회원가입
- 로그인 / 로그아웃
- 내 정보 조회
- 내 정보 수정
- JWT 기반 인증/인가

#### 2. 모집글 관리
- 모집글 작성
- 모집글 목록 조회
- 모집글 상세 조회
- 모집글 수정
- 모집글 삭제
- 모집 상태 변경

#### 3. 모집글 검색 및 필터
- 제목 키워드 검색
- 모집 유형별 필터
- 기술 스택 필터
- 모집중 여부 필터
- 최신순 정렬
- 마감일순 정렬

#### 4. 지원 기능
- 모집글 지원하기
- 지원 취소
- 모집글 작성자의 지원자 목록 조회
- 지원자 승인
- 지원자 거절

#### 5. 북마크 기능
- 북마크 추가
- 북마크 해제
- 내 북마크 목록 조회

#### 6. 댓글 기능
- 댓글 작성
- 댓글 목록 조회
- 댓글 수정
- 댓글 삭제

#### 7. 마이페이지
- 내 프로필 조회 / 수정
- 내가 작성한 모집글 조회
- 내가 지원한 모집글 조회
- 내가 북마크한 모집글 조회

---

## 추후 확장 기능

- 알림 기능
- 인기 모집글 기능
- 관심 기술 스택 기반 추천 기능
- 학교/학과 기반 필터
- 역할별 모집 기능 강화
- 관리자 기능
- 신고 기능
- 팀 전용 협업 공간
- Redis 캐시
- Jenkins 기반 CI/CD 자동화
- Docker 컨테이너 배포
- AWS Lightsail 운영

---

## 기술 스택

### 백엔드
- Java 21
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Spring Security
- JWT
- MySQL
- JUnit
- Gradle
- Swagger / springdoc-openapi

### 프론트엔드
- TypeScript
- React 19
- Vite
- MUI
- Axios
- TanStack Query
- react-router-dom
- Vitest

### 인프라 / 운영
- Docker
- Jenkins
- AWS Lightsail
- Linux
- Nginx
- Redis (추후)

---

## 개발 방향

이 프로젝트는 처음부터 모든 기능을 한 번에 구현하지 않는다.

반드시 아래 순서대로 점진적으로 구현한다.

1. 기능 명세 확정
2. ERD 설계
3. MySQL 테이블 생성
4. Spring Boot JPA 엔티티 설계
5. 회원가입 / 로그인 / JWT 인증 구현
6. 모집글 CRUD 구현
7. 지원 기능 구현
8. 북마크 기능 구현
9. 댓글 기능 구현
10. 마이페이지 구현
11. Swagger 문서화
12. React 프론트 연결
13. Docker 배포
14. Jenkins 자동 배포
15. Redis 캐시 추가

Claude는 항상 **MVP 범위를 먼저 완성하고, 그 이후 확장 기능을 제안하는 방식**으로 작업해야 한다.

---

## 서비스 도메인 설명

이 서비스는 단순 게시판이 아니다.

핵심 도메인은 다음과 같다.

- 사용자(User)
- 모집글(RecruitmentPost)
- 지원(Application)
- 북마크(Bookmark)
- 댓글(Comment)
- 기술 스택(PostStack)

핵심 비즈니스 흐름은 다음과 같다.

1. 사용자가 회원가입한다.
2. 사용자가 모집글을 작성한다.
3. 다른 사용자가 모집글에 지원한다.
4. 모집글 작성자는 지원자를 조회하고 승인/거절한다.
5. 사용자는 관심 있는 모집글을 북마크한다.
6. 사용자는 댓글을 통해 소통한다.
7. 사용자는 마이페이지에서 자신의 활동을 조회한다.

Claude는 이 프로젝트를 구현할 때 **게시판이 아니라 지원 프로세스를 포함한 플랫폼**으로 이해해야 한다.

---

## 현재 확정된 ERD 구조

### 1. users
회원 정보를 저장하는 테이블

컬럼:
- id BIGINT PK
- email VARCHAR(255) UNIQUE NOT NULL
- password VARCHAR(255) NOT NULL
- nickname VARCHAR(100) UNIQUE NOT NULL
- university VARCHAR(100) NOT NULL
- major VARCHAR(100) NOT NULL
- bio TEXT NULL
- github_url VARCHAR(255) NULL
- portfolio_url VARCHAR(255) NULL
- role VARCHAR(50) NOT NULL DEFAULT 'USER'
- created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
- updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP

설명:
- email은 로그인 ID
- password는 암호화 저장
- role은 USER / ADMIN 구분용이며 MVP에서는 USER만 사용해도 됨

---

### 2. recruitment_posts
모집글 정보를 저장하는 테이블

컬럼:
- id BIGINT PK
- author_id BIGINT FK -> users.id
- title VARCHAR(255) NOT NULL
- content TEXT NOT NULL
- category VARCHAR(50) NOT NULL
- recruitment_count INT NOT NULL
- period VARCHAR(100) NOT NULL
- deadline DATE NOT NULL
- meeting_type VARCHAR(50) NOT NULL
- status VARCHAR(50) NOT NULL DEFAULT 'OPEN'
- view_count INT NOT NULL DEFAULT 0
- created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
- updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP

설명:
- 모집글 작성자와 연결
- category 예시: CONTEST, GRADUATION_PROJECT, TEAM_PROJECT, STUDY, HACKATHON, ETC
- meeting_type 예시: ONLINE, OFFLINE, HYBRID
- status 예시: OPEN, CLOSED

---

### 3. post_stacks
모집글의 기술 스택을 저장하는 테이블

컬럼:
- id BIGINT PK
- post_id BIGINT FK -> recruitment_posts.id
- stack_name VARCHAR(100) NOT NULL

설명:
- 모집글 하나에 여러 기술 스택이 들어갈 수 있으므로 별도 테이블로 분리함

---

### 4. applications
지원 내역을 저장하는 테이블

컬럼:
- id BIGINT PK
- applicant_id BIGINT FK -> users.id
- post_id BIGINT FK -> recruitment_posts.id
- message TEXT NULL
- status VARCHAR(50) NOT NULL DEFAULT 'PENDING'
- created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
- updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP

제약조건:
- UNIQUE (post_id, applicant_id)

설명:
- 같은 사용자는 같은 모집글에 중복 지원할 수 없음
- status 예시: PENDING, APPROVED, REJECTED, CANCELED

---

### 5. bookmarks
북마크 정보를 저장하는 테이블

컬럼:
- id BIGINT PK
- user_id BIGINT FK -> users.id
- post_id BIGINT FK -> recruitment_posts.id
- created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP

제약조건:
- UNIQUE (user_id, post_id)

설명:
- 같은 사용자가 같은 글을 여러 번 북마크할 수 없음

---

### 6. comments
댓글 정보를 저장하는 테이블

컬럼:
- id BIGINT PK
- user_id BIGINT FK -> users.id
- post_id BIGINT FK -> recruitment_posts.id
- content TEXT NOT NULL
- created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
- updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP

설명:
- MVP에서는 대댓글 기능 없이 일반 댓글만 구현함

---

## 테이블 관계

- users 1 : N recruitment_posts
- users 1 : N applications
- users 1 : N bookmarks
- users 1 : N comments

- recruitment_posts 1 : N post_stacks
- recruitment_posts 1 : N applications
- recruitment_posts 1 : N bookmarks
- recruitment_posts 1 : N comments

Claude는 엔티티 설계 시 이 관계를 기준으로 JPA 매핑을 구성해야 한다.

---

## MySQL DDL 기준

Claude가 DB 테이블 또는 JPA 엔티티를 생성할 때는 아래 원칙을 따라야 한다.

1. MySQL 8 기준으로 작성한다.
2. 문자셋은 utf8mb4를 사용한다.
3. created_at / updated_at 컬럼은 기본값을 둔다.
4. 외래키를 명확히 설정한다.
5. applications, bookmarks에는 UNIQUE 제약조건을 반드시 넣는다.
6. FK 컬럼명은 의미 있는 이름을 사용한다.
    - applicant_id
    - author_id
    - user_id
    - post_id
7. 애매한 이름(id2, id3 등)은 절대 사용하지 않는다.

---

## 백엔드 설계 원칙

Claude는 Spring Boot 백엔드 구현 시 아래 원칙을 따라야 한다.

### 구조
- Controller
- Service
- Repository
- Entity
- DTO
- Security
- Exception
- Config

### 원칙
1. Entity와 API 요청/응답 DTO를 분리한다.
2. 모든 API는 명확한 Request DTO / Response DTO를 사용한다.
3. 인증/인가가 필요한 기능은 JWT 기반으로 처리한다.
4. 작성자 본인만 수정/삭제할 수 있도록 권한 검증을 넣는다.
5. 서비스 계층에서 비즈니스 로직을 처리한다.
6. 예외 메시지는 명확하게 작성한다.
7. Enum을 적극 활용한다.

### Enum 후보
- UserRole
- PostCategory
- MeetingType
- PostStatus
- ApplicationStatus

---

## 비즈니스 규칙

Claude는 아래 비즈니스 규칙을 반드시 지켜야 한다.

### 회원
- 이메일 중복 가입 불가
- 닉네임 중복 불가
- 비밀번호는 암호화 저장

### 모집글
- 로그인한 사용자만 작성 가능
- 작성자 본인만 수정/삭제 가능
- 제목과 내용은 필수
- 마감일은 현재보다 이전 날짜가 될 수 없음

### 지원
- 로그인한 사용자만 지원 가능
- 작성자는 자신의 모집글에 지원할 수 없음
- 동일 사용자는 같은 모집글에 중복 지원할 수 없음
- 모집 마감 상태이면 지원할 수 없음
- 지원 상태 기본값은 PENDING
- 모집글 작성자만 지원 승인/거절 가능

### 북마크
- 로그인한 사용자만 가능
- 동일 글 중복 북마크 불가

### 댓글
- 로그인한 사용자만 작성 가능
- 댓글 작성자 본인만 수정/삭제 가능

---

## API 설계 방향

Claude는 RESTful API 스타일로 구현한다.

예시:

### 인증
- POST /api/auth/signup
- POST /api/auth/login
- GET /api/users/me
- PATCH /api/users/me

### 모집글
- POST /api/posts
- GET /api/posts
- GET /api/posts/{postId}
- PATCH /api/posts/{postId}
- DELETE /api/posts/{postId}
- PATCH /api/posts/{postId}/status

### 지원
- POST /api/posts/{postId}/applications
- DELETE /api/applications/{applicationId}
- GET /api/posts/{postId}/applications
- PATCH /api/applications/{applicationId}/status

### 북마크
- POST /api/posts/{postId}/bookmark
- DELETE /api/posts/{postId}/bookmark
- GET /api/users/me/bookmarks

### 댓글
- POST /api/posts/{postId}/comments
- GET /api/posts/{postId}/comments
- PATCH /api/comments/{commentId}
- DELETE /api/comments/{commentId}

### 마이페이지
- GET /api/users/me/posts
- GET /api/users/me/applications
- GET /api/users/me/bookmarks

Claude는 실제 구현 시 URI와 HTTP Method의 의미를 최대한 일관되게 유지해야 한다.

---

## 프론트엔드 설계 방향

Claude는 프론트엔드 구현 시 아래 구조를 염두에 둔다.

### 페이지 예시
- /
- /login
- /signup
- /posts
- /posts/:id
- /posts/write
- /mypage
- /mypage/posts
- /mypage/applications
- /mypage/bookmarks

### 주요 화면
1. 홈
2. 모집글 목록
3. 모집글 상세
4. 모집글 작성
5. 로그인 / 회원가입
6. 마이페이지

### 상태 관리
- 서버 상태는 TanStack Query로 관리
- API 호출은 Axios 사용
- 인증 토큰은 추후 정책에 따라 localStorage 또는 cookie 방식 결정 가능

### UI 방향
- MUI 중심으로 구현
- 목록/카드/다이얼로그/폼/페이지네이션을 적극 활용
- 복잡한 디자인보다 사용성과 기능 명확성을 우선

---

## 우선순위

Claude는 항상 아래 우선순위를 따른다.

### 1순위
- 회원가입 / 로그인 / JWT
- 모집글 CRUD
- 모집글 목록 / 상세 / 검색
- 지원 기능
- 마이페이지

### 2순위
- 북마크
- 댓글
- 모집 상태 변경

### 3순위
- 알림
- 인기글
- Redis 캐시
- 추천 기능

### 4순위
- 관리자 기능
- 신고 기능
- 학교 인증 기능
- 협업 공간

---

## 코드 작성 스타일

Claude는 코드를 작성할 때 다음 스타일을 따른다.

1. 의미 있는 클래스명, 메서드명, 변수명을 사용한다.
2. 애매한 이름(id2, tmp, data1 등)을 사용하지 않는다.
3. DTO 이름은 역할이 분명해야 한다.
    - SignUpRequest
    - LoginRequest
    - PostCreateRequest
    - PostDetailResponse
    - ApplicationStatusUpdateRequest
4. 주석은 꼭 필요한 곳에만 작성한다.
5. 구조를 단순하게 유지하되, 확장 가능성을 고려한다.
6. 처음부터 과도한 추상화는 하지 않는다.
7. MVP 완성을 우선한다.

---

## Claude에게 기대하는 역할

Claude는 이 프로젝트에서 다음 역할을 수행해야 한다.

1. 현재 확정된 기획과 ERD를 기준으로 코드를 생성한다.
2. 구현 순서를 어기지 않고 단계적으로 개발을 돕는다.
3. 불필요하게 범위를 확장하지 않는다.
4. 기존 구조를 존중하면서 필요한 부분만 보완한다.
5. 백엔드와 프론트엔드 모두에서 일관된 네이밍과 구조를 유지한다.
6. 내가 초보자일 수 있다는 점을 고려해, 변경사항과 이유를 이해하기 쉽게 설명한다.
7. 모호한 부분은 임의로 과하게 확장하기보다, 현재 MVP 기준으로 합리적인 선택을 제안한다.

---

## 지금까지 결정된 핵심 요약

- 프로젝트 주제: 대학생 팀원 모집 및 협업 준비 플랫폼
- 핵심 목표: 반복 방문형 서비스형 웹사이트 제작
- MVP 범위:
    - 회원가입 / 로그인 / JWT
    - 모집글 CRUD
    - 검색 / 필터
    - 지원 기능
    - 북마크
    - 댓글
    - 마이페이지
- 핵심 DB 테이블:
    - users
    - recruitment_posts
    - post_stacks
    - applications
    - bookmarks
    - comments
- 설계 방향:
    - 단순 게시판이 아니라 지원 프로세스를 포함한 플랫폼
    - 백엔드 우선 구현 후 프론트 연결
    - 이후 Docker / Jenkins / Redis / AWS로 확장

---

## 작업 시작 시 우선 요청할 내용

Claude가 실제 작업을 시작할 때는 아래 순서로 진행하는 것이 좋다.

1. Spring Boot 프로젝트 초기 세팅
2. build.gradle 의존성 구성
3. application.properties 설정
4. 엔티티 생성
5. Repository 생성
6. DTO 생성
7. 회원가입 / 로그인 기능 구현
8. JWT 인증/인가 구현
9. 모집글 기능 구현
10. 지원 기능 구현
11. 북마크 / 댓글 / 마이페이지 구현
12. Swagger 문서화
13. React 프론트 연결
14. Docker / Jenkins / Lightsail 배포

Claude는 각 단계마다 현재 상태를 정리하면서 다음 단계로 넘어가야 한다.

## 작업 규칙

- 백엔드와 프론트엔드는 분리된 디렉터리로 관리한다.
- 백엔드는 IntelliJ 기준, 프론트는 VS Code 기준으로 작업한다.
- 설명 없이 기존 파일 구조를 크게 바꾸지 않는다.
- 내가 요청하지 않은 라이브러리를 임의로 추가하지 않는다.
- 먼저 MVP를 완성하고, 이후 확장 기능을 제안한다.
- DB 스키마와 API 설계가 충돌하면 반드시 먼저 이유를 설명한다.