# WAP-OR: 와포어

# 0. Getting Started (시작하기)
```bash
$ 명령어
```
[서비스 링크]()

<br/>
<br/>

# 1. Project Overview (프로젝트 개요)
- 프로젝트 이름: WalleT ProtectOR: WAP-OR
- 프로젝트 설명: 커뮤니티 기반 소비 기록 가계부 앱

<br/>
<br/>

# 2. Team Members (팀원 및 팀 소개)
| 김도현   | 김유정   | 이강민   | 이동현   | 이동훈   |
|:--------:|:--------:|:--------:|:--------:|:--------:|
| <img src="https://github.com/pu2rile.png?size=150" alt="김도현" width="100"> | <img src="https://github.com/Kyujeong776.png?size=150" alt="김유정" width="100"> | <img src="https://github.com/mututu17.png?size=150" alt="이강민" width="100"> | <img src="https://github.com/dhlee777.png?size=150" alt="이동현" width="100"> | <img src="https://github.com/Hun0906.png?size=150" alt="이동훈" width="100"> |
| **서버** | **디자이너** | **클라이언트** | **서버** | **클라이언트** |
| [pu2rile](https://github.com/pu2rile) | [Kyujeong776](https://github.com/Kyujeong776) | [mututu17](https://github.com/mututu17) | [dhlee777](https://github.com/dhlee777) | [Hun0906](https://github.com/Hun0906) |
<br/>
<br/>

# 3. Key Features (주요 기능)
- **회원 가입**:
  - 회원 가입 시 DB에 유저 정보가 등록됩니다.

- **로그인**:
  - 사용자 인증 정보를 통해 로그인합니다.

**_추가 예정_**

<br/>
<br/>

# 4. Tasks & Responsibilities (작업 및 역할 분담)
|  |  |  |
|-----------------|-----------------|-----------------|
| 김도현    |  <img src="https://github.com/pu2rile.png?size=150" alt="김도현" width="100"> | <ul><li>프로젝트 계획 및 관리</li><li>팀 리딩 및 커뮤니케이션</li><li>흠냐</li></ul>     |
| 김유정   |  <img src="https://github.com/Kyujeong776.png?size=150" alt="김유정" width="100">| <ul><li>UI/UX 디자인</li></ul> |
| 이강민   |  <img src="https://github.com/mututu17.png?size=150" alt="이강민" width="100">    |<ul><li>메인 페이지 개발</li><li>로그인 페이지 개발</li></ul>  |
| 이동현    |  <img src="https://github.com/dhlee777.png?size=150" alt="이동현" width="100">    | <ul><li>회원 가입 페이지 개발</li><li>마이 페이지 개발</li><li>흠냐</li></ul>    |
| 이동훈    |  <img src="https://github.com/Hun0906.png?size=150" alt="이동훈" width="100">    | <ul><li>회원 가입 페이지 개발</li><li>마이 페이지 개발</li><li>흠냐</li></ul>    |
<br/>
<br/>



# 5. Technology Stack (기술 스택)
## 5.1 Language
|  |  |
|-----------------|-----------------|
| Kotlin    |<img src="https://upload.wikimedia.org/wikipedia/commons/7/74/Kotlin_Icon.png" alt="Kotlin" width="100">| 
| Java    |   <img src="https://upload.wikimedia.org/wikipedia/en/3/30/Java_programming_language_logo.svg" alt="Java" width="100">|

<br/>

## 5.2 Client
|  |  |  |
|-----------------|-----------------|-----------------|
| Android Studio    |  <img src="https://upload.wikimedia.org/wikipedia/commons/9/92/Android_Studio_Trademark.svg" alt="Android Studio" width="100"> | 2023.1.1    |

<br/>

## 5.3 Server
|  |  |  |
|-----------------|-----------------|-----------------|
| Spring Boot    |  <img src="https://seeklogo.com/images/S/spring-logo-9A2BC78AAF-seeklogo.com.png" alt="Spring Boot" width="100">    | 10.12.5    |
| MySQL          |  <img src="https://upload.wikimedia.org/wikipedia/en/d/dd/MySQL_logo.svg" alt="MySQL" width="100"> | 8.0.27    |

<br/>

## 5.4 Cooperation
|  |  |
|-----------------|-----------------|
| Git    |  <img src="https://github.com/user-attachments/assets/483abc38-ed4d-487c-b43a-3963b33430e6" alt="git" width="100">    |
| Figma    |  <img src="https://upload.wikimedia.org/wikipedia/commons/3/33/Figma-logo.svg" alt="Figma" width="100">    |
| Notion    |  <img src="https://github.com/user-attachments/assets/34141eb9-deca-416a-a83f-ff9543cc2f9a" alt="Notion" width="100">    |

<br/>

# 6. Project Structure (프로젝트 구조)
> 예시입니다!!!!

```plaintext
project/
├── public/
│   ├── index.html           # HTML 템플릿 파일
│   └── favicon.ico          # 아이콘 파일
├── src/
│   ├── assets/              # 이미지, 폰트 등 정적 파일
│   ├── components/          # 재사용 가능한 UI 컴포넌트
│   ├── hooks/               # 커스텀 훅 모음
│   ├── pages/               # 각 페이지별 컴포넌트
│   ├── App.js               # 메인 애플리케이션 컴포넌트
│   ├── index.js             # 엔트리 포인트 파일
│   ├── index.css            # 전역 css 파일
│   ├── firebaseConfig.js    # firebase 인스턴스 초기화 파일
│   package-lock.json    # 정확한 종속성 버전이 기록된 파일로, 일관된 빌드를 보장
│   package.json         # 프로젝트 종속성 및 스크립트 정의
├── .gitignore               # Git 무시 파일 목록
└── README.md                # 프로젝트 개요 및 사용법
```

<br/>
<br/>

# 7. Development Workflow (개발 워크플로우)
## 브랜치 전략 (Branch Strategy)
우리의 브랜치 전략은 Git Flow를 기반으로 하며, 다음과 같은 브랜치를 사용합니다.

- Main Branch
  - 배포 가능한 상태의 코드를 유지합니다.
  - 모든 배포는 이 브랜치에서 이루어집니다.
  
- {name} Branch
  - 팀원 각자의 개발 브랜치입니다.
  - 모든 기능 개발은 이 브랜치에서 이루어집니다.

<br/>
<br/>

# 8. Coding Convention
> 예시입니다!!!!

## 문장 종료
```
// 세미콜론(;)
console.log("Hello World!");
```

<br/>


## 명명 규칙
* 상수 : 영문 대문자 + 스네이크 케이스
```
const NAME_ROLE;
```
* 변수 & 함수 : 카멜케이스
```
// state
const [isLoading, setIsLoading] = useState(false);
const [isLoggedIn, setIsLoggedIn] = useState(false);
const [errorMessage, setErrorMessage] = useState('');
const [currentUser, setCurrentUser] = useState(null);

// 배열 - 복수형 이름 사용
const datas = [];

// 정규표현식: 'r'로 시작
const = rName = /.*/;

// 이벤트 핸들러: 'on'으로 시작
const onClick = () => {};
const onChange = () => {};

// 반환 값이 불린인 경우: 'is'로 시작
const isLoading = false;

// Fetch함수: method(get, post, put, del)로 시작
const getEnginList = () => {...}
```

<br/>

## 블록 구문
```
// 한 줄짜리 블록일 경우라도 {}를 생략하지 않고, 명확히 줄 바꿈 하여 사용한다
// good
if(true){
  return 'hello'
}

// bad
if(true) return 'hello'
```

<br/>

## 함수
```
함수는 함수 표현식을 사용하며, 화살표 함수를 사용한다.
// Good
const fnName = () => {};

// Bad
function fnName() {};
```

<br/>

## 태그 네이밍
Styled-component태그 생성 시 아래 네이밍 규칙을 준수하여 의미 전달을 명확하게 한다.<br/>
태그명이 길어지더라도 의미 전달의 명확성에 목적을 두어 작성한다.<br/>
전체 영역: Container<br/>
영역의 묶음: {Name}Area<br/>
의미없는 태그: <><br/>
```
<Container>
  <ContentsArea>
    <Contents>...</Contents>
    <Contents>...</Contents>
  </ContentsArea>
</Container>
```

<br/>

## 폴더 네이밍
카멜 케이스를 기본으로 하며, 컴포넌트 폴더일 경우에만 파스칼 케이스로 사용한다.
```
// 카멜 케이스
camelCase
// 파스칼 케이스
PascalCase
```

<br/>

## 파일 네이밍
```
컴포넌트일 경우만 .jsx 확장자를 사용한다. (그 외에는 .js)
customHook을 사용하는 경우 : use + 함수명
```

<br/>
<br/>

# 9. 커밋 컨벤션
## 기본 구조
```
type: subject

body 
```

<br/>

## type 종류
```
main : 최종 배포, 메인 브랜치
feat : 기능 추가
fix : 에러 수정, 버그 수정
test : 테스트 코드 추가
design : 사용자 UI 디자인 변경
docs : 문서 수정
refactor : 코드 리팩토링
remove : 파일 삭제
rename : 파일 또는 폴더 이름 수정
chore : gradle 세팅, 빌드 업무 수정 등 이외의 모든 것
```

<br/>

## 커밋 예시
```
== ex1
feat: 회원 가입 기능 구현

SMS, 이메일 중복확인 API 개발

== ex2
chore: styled-components 라이브러리 설치

UI개발을 위한 라이브러리 styled-components 설치
```

<br/>
<br/>

# 10. 컨벤션 수행 결과
