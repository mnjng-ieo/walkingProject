// 메인페이지 검색 결과 페이지 이동하는 자바스크립트

// 출력된 검색 결과에서 상세화면 보기
// 산책로, 커뮤니티에 출력된 하나의 컬럼에 사용, 해시태그는 a태그 활용
// 클릭된 컬럼에서 data-url 속성 값을 가져옴
function redirectToDetail(row) {
    let url = row.getAttribute("data-url");
        
    // 페이지 이동
    window.location.href = url;
}