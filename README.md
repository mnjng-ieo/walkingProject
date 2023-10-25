# Walk Around You (WAY) Project
## 💚 산책로 추천 & 커뮤니티 사이트
스프링 부트 + JPA 산책로 추천 커뮤니티

## 💚 프로젝트 소개
건강이라는 키워드에 관심이 많아지면서 주변에서 쉽게 접할 수 있는 산책로를 소개해주는 서비스의 필요성이 증대되었다. 
주변의 맛집이나, 편의 시설 정보, 병원 등의 건물에 대한 정보는 즐비하지만 쉽게 걸을 만한 산책로의 정보를 요약해서 제공해주는 플랫폼은 익숙한 것이 없어보였다.
포털사이트에서 검색한다고 해도 어떤 키워드로 검색할지, 블로그를 뒤진다해도 원하는 수준의 정보를 얻기 위해서는 최소한 세 개 이상의 포스트를 참고해야 할 것이다.
걷고 싶다는 욕구를 충족하는 것은 절대 평면적이지 않다. 
사람들마다 위치 정보도 다르고, 걷기 원하는 길의 난이도 수준, 소요 시간, 주변 경관, 소음 수준, 안전도 수준 등 맞춤형 정보가 요약되어 제공되는 사이트의 필요성을 느꼈다. 
도심 속에서도 언제든 산책을 위한 공간을 찾을 수 있도록, 사용자의 현재 위치를 기반으로 가까운 산책로를 검색할 수 있는 기능을 구현하고자 했다.
산책이 일상의 일부가 되도록 돕는 웹 서비스이다. 
산책을 권장하는 사이트의 부가 효과로서, 건강증진과 스트레스 완화에 도움이 될 수 있고, 
기술과 자연의 결합(현대 기술을 활용하여 도심 속에서도 자연을 즐길 수 있는 방법을 찾는 것)에도 일조할 수 있다.

## 💚 개발목표
사용자에게 산책이 일상의 일부가 될 수 있게 ‘도심 내 산책로 접근성 강화’, ‘사용자 맞춤형 서비스 제공’ 에 무게를 두고 기획을 시작했다. 
위치 정보와 개인 취향(예: 산책로 난이도, 관련된 게시물 추천, 연관 해시태그 기능)을 기반으로 한 산책로 추천 서비스를 제공하여, 개개인에게 최적화된 경험을 제공하고자 했다.

## 💚 STACKS

🌳 Language : Java 17

🌳 Web : HTML5, CSS3, Bootstrap 5.3.1, J-query 3.3.1, JavaScript,  Ajax 

🌳 Framework : Spring Tool Suite 4 (STS - 이클립스 기반의 IDE)

🌳 Library : Lombok 1.18.28, Thymeleaf 3.1.3, Spring Security 6.1.3, Spring Boot 3.1.3

🌳 Database : mariaDB, JPA 3.1.3

🌳 협업툴 & etc : 깃허브, ERD 클라우드, Notion, Trello

## 💚 주요 기능

### 회원 가입 및 로그인 기능

- 로그인/로그아웃, 아이디/비번 찾기, 회원탈퇴/비번 변경
- 회원 가입 시 아이디 중복 확인 및 정규식을 이용한 유효성 검사와 아이디에 대해 중복 검사를 적용한다
- 사용자의 ID, PW 등 데이터 저장 보관
- 스프링 시큐리티로 사용자 비밀번호 암호화와 로그인 상태 관리
- 관리자와 일반 사용자로 권한 나눠 사용 가능한 페이지 및 서비스 구별

### 인기 게시물

- 메인 페이지에 인기 있는 산책로 9가지, 해시태그 12가지가 업데이트 된다
- 사용자가 누른 좋아요 수에 따라 산책로가 자동으로 업데이트 된다
- 사용자에 의해 가장 많이 언급된 해시태그가 자동으로 업데이트 된다

### 검색

- 메인 페이지 & 헤더
    - 입력된 검색어가 포함되어 있는 해시태그, 산책로 목록 페이지, 커뮤니티 페이지의 게시물들을 조회 가능
- 커뮤니티
    - 특정 게시판(자유, 모임,  후기)과 검색조건(제목+본문, 제목, 본문, 작성자)을 선택할 수 있는
      다중 필터링 옵션을 주어, 일반 검색 기능에 편의성 제공
    - 검색 결과는 ‘최신 순’, ‘조회 순’, ‘좋아요 순’의 정렬 기준을 선택할 수 있다
- 산책로
    - 지역, 난이도, 소요 시간, 거리, 검색어 등 다중 조건 설정과 정렬 기준 설정으로 상세한 검색 결과 제공
    - 검색 결과는 ‘가나다 순’, ‘조회수’, ‘거리 짧은 순’, ‘거리 먼 순’의 정렬 기준은 선택할 수 있다
    

### 상세조회(산책로)

- 위치, 코스, 거리, 소요 시간, 화장실, 편의시설, 상세설명이 나와 있다
- 조회 수, 좋아요 수가 조회 가능하며 좋아요 버튼을 눌러 마이페이지에서 확인 가능하다
- 댓글, 게시물 작성하기, 공유 버튼을 이용할 수 있다
- 하단에 해당 산책로가 언급된 게시물을 모아볼 수 있다

### 상세조회(게시물)

- 사용자 간에 활발한 정보 공유 및 소통할 수 있도록 게시글과 댓글 CRUD 기능 제공
- 작성된 해시태그는 글 아래 버튼으로 모아 보이며 해당 버튼을 눌러 같은 해시태그가 포함된 게시물을 모아볼 수 있다
- 언급한 산책길을 지도로 볼 수 있다

### 지도 기능

- 카카오 Map API 을 이용하여 특정 산책로 위치가 자동으로 포커스되고
  상단에 요약 정보가 표기된 플래그 창이 생성되어 지도와 산책로 정보를 동시에 제공

### 좋아요

- 로그인한 회원이 산책로, 게시글, 댓글에 좋아요를 누를 수 있고 
  마이페이지에서 좋아요를 누른 산책로와 게시글을 모아볼 수 있는 기능 제공

### 이미지 업로드

- 관리자용 산책로 등록/수정 페이지와 게시물 등록/수정 페이지,
  각 유저의 마이페이지에서 프로필 사진 이미지 업로드 가능
- 업로드한 사진들은 모든 페이지의 산책로/게시물/현재 접속자/게시물이나 댓글 작성자 등에 조회됨

### 해시태그

- 게시글 작성 시 띄어쓰기 없이 단어나 문구를 쓰고 앞에 해시기호(#)를 붙여 넣어 활용한다
- 이 해시태그를 클릭하면 해당 해시태그가 포함된 내용물이 모두 표시되며 검색 기능으로도 활용 가능하다

### 마이페이지

- 내가 작성한 글과 업로드한 이미지를 모아볼 수 있다
- 내가 좋아요 한 산책로와 게시글을 모아볼 수 있다
- 내가 댓글 단 게시물을 모아볼 수 있다
- 나의 정보 조회 및 수정, 탈퇴가 가능하다
- 다른 회원의 프로필의 경우 해당 회원이 작성한 글, 업로드한 이미지의 단순 조회가 가능하다.

## 💚 시연영상
https://drive.google.com/file/d/1D711NKUN83ytbPaG0dMT75a3MTfKgk9-/view