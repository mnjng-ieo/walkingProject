// 각 상세페이지로 경로 넘어가는 클릭 이벤트
function redirectToCourseDetails(courseId){
    let url = "/course/" + courseId;
    window.location.href = url;
}

// 현재 페이지 초기화
let currentPage = 0;

// AJAX 요청 : 검색 조건 수집
function updateViewWithSearchResults(){
    
    let xhr = new XMLHttpRequest();
    // 페이지 번호까지 쿼리 파라미터로 추가
    xhr.open("GET", "/mypage-course?page=" + currentPage, true);
    
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {
            // AJAX 요청이 성공적으로 완료되었을 때 뷰 업데이트
            let response = xhr.responseText;
            
            // response를 파싱해서 searchResults div 내용만 추출
            let parser = new DOMParser();
            let doc = parser.parseFromString(response, 'text/html');
            let searchResultsContent = 
                    doc.querySelector('#searchResults').innerHTML;
                    
            // searchResults div 내용 교체
            document.getElementById("searchResults").innerHTML = searchResultsContent;  
        }
    };
    
    xhr.send();
}

// 페이지 번호를 클릭할 때 해당 페이지로 Ajax 요청 보내기
function loadPage(newPage) {
    // 페이지 번호 업데이트
    currentPage = newPage;    
    updateViewWithSearchResults();
}