// 현재 페이지 초기화
let currentPage = 0; 

// 출력된 검색 결과에서 상세화면 보기
// 클릭된 컬럼에서 data-url 속성 값을 가져옴
function redirectToDetail(row) {
    let url = row.getAttribute("data-url");
        
    // 페이지 이동
    window.location.href = url;
}

