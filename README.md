# walkingProject
## 🤍 산책로 추천 커뮤니티
스프링 부트 + jpa 산책로 추천 커뮤니티

## 🤍 프로젝트 소개
건강이라는 키워드에 관심이 많아지면서 주변에서 쉽게 접할 수 있는 산책로를 소개해주는 서비스의 필요성이 증대되었다. 
주변의 맛집이나, 편의 시설 정보, 병원 등의 건물에 대한 정보는 즐비하지만 쉽게 걸을 만한 산책로의 정보를 요약해서 제공해주는 플랫폼은 익숙한 것이 없어보였다.
포털사이트에서 검색한다고 해도 어떤 키워드로 검색할지, 블로그를 뒤진다해도 원하는 수준의 정보를 얻기 위해서는 최소한 세 개 이상의 포스트를 참고해야 할 것이다.
걷고 싶다는 욕구를 충족하는 것은 절대 평면적이지 않다. 
사람들마다 위치 정보도 다르고, 걷기 원하는 길의 난이도 수준, 소요 시간, 주변 경관, 소음 수준, 안전도 수준 등 맞춤형 정보가 요약되어 제공되는 사이트의 필요성을 느꼈다. 
도심 속에서도 언제든 산책을 위한 공간을 찾을 수 있도록, 사용자의 현재 위치를 기반으로 가까운 산책로를 검색할 수 있는 기능을 구현하고자 했다.
산책이 일상의 일부가 되도록 돕는 웹 서비스이다. 
산책을 권장하는 사이트의 부가 효과로서, 건강증진과 스트레스 완화에 도움이 될 수 있고, 
기술과 자연의 결합(현대 기술을 활용하여 도심 속에서도 자연을 즐길 수 있는 방법을 찾는 것)에도 일조할 수 있다.

## 🤍 개발목표
사용자에게 산책이 일상의 일부가 될 수 있게 ‘도심 내 산책로 접근성 강화’, ‘사용자 맞춤형 서비스 제공’ 에 무게를 두고 기획을 시작했다. 위치 정보와 개인 취향(예: 산책로 난이도, 관련된 게시물 추천, 연관 해시태그 기능)을 기반으로 한 산책로 추천 서비스를 제공하여, 개개인에게 최적화된 경험을 제공하고자 했다.

## 🤍 STACKS

🌳 Language : Java 17
🌳 Web : HTML5, CSS3, Bootstrap 5.3.1, J-query 3.3.1, JavaScript,  Ajax 
🌳 Framework : Spring Tool Suite 4 (STS - 이클립스 기반의 IDE)
🌳 Library : Lombok 1.18.28, Thymeleaf 3.1.3, Spring Security 6.1.3, Spring Boot 3.1.3
🌳 Database : mariaDB, JPA 3.1.3
🌳 협업툴 & etc : 깃허브, ERD 클라우드, Notion, Trello

## 🤍 주요 기능

### 회원 가입 및 로그인 기능

- 회원 가입 시 아이디 중복 확인 및 정규식을 이용한 유효성 검사와 아이디에 대해 중복 검사를 적용한다
- 스프링 시큐리티 이용

### 인기 게시물

- 메인 페이지에 인기 있는 산책로 9가지, 해시태그 12가지가 업데이트 된다
- 사용자가 누른 좋아요 수에 따라 산책로가 자동으로 업데이트 된다
- 사용자에 의해 가장 많이 언급된 해시태그가 자동으로 업데이트 된다

### 검색

- 메인 페이지 & 헤더
    - 입력된 검색어가 포함되어 있는 해시태그, 산책로 목록 페이지, 커뮤니티 페이지의 게시물들을 조회 가능
- 커뮤니티
    - 특정 게시판(자유, 모임,  후기)과 검색조건(제목+본문, 제목, 본문, 작성자)을 선택할 수 있는 다중 필터링 옵션을 주어, 일반 검색 기능에 편의성 제공
    - 검색 결과는 ‘최신 순’, ‘조회 순’, ‘좋아요 순’의 정렬 기준을 선택할 수 있다
- 산책로
    - 지역, 난이도, 소요 시간, 거리, 검색어 등 다중 조건 설정과 정렬 기준 설정으로 상세한 검색 결과 제공
    - 검색 결과는 ‘가나다 순’, ‘조회수’, ‘거리 짧은 순’, ‘거리 먼 순’의 정렬 기준은 선택할 수 있다
    

### 상세조회(산책로)

- 위치, 코스, 거리, 소요 시간, 화장실, 편의시설, 상세설명이 나와있다
- 조회 수, 좋아요 수가 조회 가능하며 좋아요 버튼을 눌러 마이페이지에서 확인 가능하다
- 댓글, 게시물 작성하기, 공유 버튼을 이용할 수 있다
- 하단에 해당 산책로가 언급된 게시물을 모아볼 수 있다

### 상세조회(게시물)

- 사용자 간에 활발한 정보 공유 및 소통할 수 있도록 게시글과 댓글 CRUD 기능 제공
- 작성된 해시태그는 글 아래 버튼으로 모아 보이며 해당 버튼을 눌러 같은 해시태그가 포함된 게시물을 모아볼 수 있다
- 언급한 산책길을 지도로 볼 수 있다

### 지도 기능

- 카카오 Map API 을 이용하여 특정 산책로 위치가 자동으로 포커스되고 상단에 요약 정보가 표기된 플래그창이 생성되어 지도와 산책로 정보를 동시에 제공

### 좋아요

- 로그인한 회원이 산책로, 게시글, 댓글에 좋아요를 누를 수 있고 마이페이지에서 좋아요를 누른 산책로와 게시글을 모아볼 수 있는 기능 제공

### 이미지 업로드

- 게시글, 회원 프로필, 산책로 메인 이미지 업로드 및 수정 가능

### 해시태그

- 게시글 작성 시 띄어쓰기 없이 단어나 문구를 쓰고 앞에 해시기호(#)를 붙여 넣어 활용한다
- 이 해시태그를 클릭하면 해당 해시태그가 포함된 내용물이 모두 표시되며 검색 기능으로도 활용 가능하다

### 마이페이지

- 내가 작성한 글과 업로드한 이미지를 모아볼 수 있다
- 다른 회원의 프로필을 누르면 해당 회원이 작성한 글, 업로드한 이미지를 모아볼 수 있다
- 내가 좋아요 한 산책로와 게시글을 모아볼 수 있다
- 내가 댓글 단 게시물을 모아볼 수 있다
- 나의 정보 조회 및 수정, 탈퇴가 가능하다

## 🤍 화면구성
![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/93de1692-aeab-4da2-9564-3a56f930da30/046651e7-4f0d-4f88-b58c-2febe2a73bea/Untitled.png)

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/93de1692-aeab-4da2-9564-3a56f930da30/4a0522ac-28b1-4697-a0cb-37a917aefb87/Untitled.png)

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/93de1692-aeab-4da2-9564-3a56f930da30/c97cc704-aba8-4d18-aad9-3c85b3bcd118/Untitled.png)

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/93de1692-aeab-4da2-9564-3a56f930da30/acf62fc3-1404-4464-b2fb-77ed03188999/Untitled.png)

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/93de1692-aeab-4da2-9564-3a56f930da30/1b9f0822-06a9-41aa-a5e1-4492e24151d9/Untitled.png)

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/93de1692-aeab-4da2-9564-3a56f930da30/ca2f9e1a-84d9-466a-9ccb-7a3da43e5a4e/Untitled.png)

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/93de1692-aeab-4da2-9564-3a56f930da30/5149027b-bf59-453f-ae5b-45e77386e74c/Untitled.png)

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/93de1692-aeab-4da2-9564-3a56f930da30/faabf7dd-1bee-402d-9ca3-a46c9749fc3b/Untitled.png)

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/93de1692-aeab-4da2-9564-3a56f930da30/63f4bb25-9b0c-4149-8c6a-1806e72584c6/Untitled.png)

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/93de1692-aeab-4da2-9564-3a56f930da30/f92410aa-cb49-4bc1-8e0f-68740cd83701/Untitled.png)

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/93de1692-aeab-4da2-9564-3a56f930da30/f35cf736-75ac-40d1-8011-42ea1ab99472/Untitled.png)

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/93de1692-aeab-4da2-9564-3a56f930da30/1223f1bd-fcd9-4f43-8d1e-b6c3ca743f5b/Untitled.png)

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/93de1692-aeab-4da2-9564-3a56f930da30/a6b4ce39-cde0-455f-a238-233a14356c9f/Untitled.png)

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/93de1692-aeab-4da2-9564-3a56f930da30/2b2e1a6e-dc79-46b1-8bd4-37c7694fcdcb/Untitled.png)

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/93de1692-aeab-4da2-9564-3a56f930da30/529750bf-db51-44cc-92d7-3cc833c762e1/Untitled.png)

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/93de1692-aeab-4da2-9564-3a56f930da30/93674e0c-cb6f-4263-87f1-c4338df0e706/Untitled.png)

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/93de1692-aeab-4da2-9564-3a56f930da30/8e0a53ff-c073-4ed5-bc60-ece79767e6a5/Untitled.png)

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/93de1692-aeab-4da2-9564-3a56f930da30/9f459cea-5b04-48ca-8663-3044e05dfd5b/Untitled.png)

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/93de1692-aeab-4da2-9564-3a56f930da30/f5a5a466-4d06-4e4a-bafe-bdf107c95e2f/Untitled.png)

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/93de1692-aeab-4da2-9564-3a56f930da30/e9324c79-21f5-450c-bdcb-58aae5c4144d/Untitled.png)

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/93de1692-aeab-4da2-9564-3a56f930da30/54477f88-fe62-46c4-a371-ebc540d70a8f/Untitled.png)

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/93de1692-aeab-4da2-9564-3a56f930da30/332b271a-6b51-4585-a89e-3b949edb98b7/Untitled.png)

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/93de1692-aeab-4da2-9564-3a56f930da30/0a3b8068-a737-4854-9762-3bb67a47ce9f/Untitled.png)

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/93de1692-aeab-4da2-9564-3a56f930da30/eab113aa-7c4a-46ff-a5fd-cb156e5b6d5e/Untitled.png)

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/93de1692-aeab-4da2-9564-3a56f930da30/e9916dd1-a0de-4cbb-b071-257dc1bcc5bb/Untitled.png)

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/93de1692-aeab-4da2-9564-3a56f930da30/4ecd876a-a0e9-4e5a-bcdd-3be0ec3fb144/Untitled.png)

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/93de1692-aeab-4da2-9564-3a56f930da30/d94734b5-8f22-4bb2-9e0d-3107b1f71863/Untitled.png)

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/93de1692-aeab-4da2-9564-3a56f930da30/d10af8b9-d8a3-479f-bd41-44d623065f23/Untitled.png)

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/93de1692-aeab-4da2-9564-3a56f930da30/ac415668-16b6-4f5b-bdc0-8cdf62e9a34a/Untitled.png)

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/93de1692-aeab-4da2-9564-3a56f930da30/57d40536-b096-4cce-8964-0ade636f3c73/Untitled.png)

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/93de1692-aeab-4da2-9564-3a56f930da30/4c575af3-f071-49d4-b0ee-946f8ab1a459/Untitled.png)

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/93de1692-aeab-4da2-9564-3a56f930da30/63933427-8748-4e8f-afcf-b2d45ad30782/Untitled.png)

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/93de1692-aeab-4da2-9564-3a56f930da30/0a9efe09-0d81-4b1e-9919-b7b2661d2758/Untitled.png)

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/93de1692-aeab-4da2-9564-3a56f930da30/542d0381-f10d-4029-80f2-fd224679c32f/Untitled.png)

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/93de1692-aeab-4da2-9564-3a56f930da30/c296484d-d0af-4f81-a515-79ccde9178ed/Untitled.png)
