// 현재 페이지 초기화
let currentPage = 0; 

// 메인 페이지 검색 결과에서 게시물 더보기 클릭하면 나오는 페이지(게시물 리스트)
// 검색 조건 수집
function boardWithSearchResults() {
    let sort = document.getElementById('boardSort').value;
    let keyword = document.getElementById('keyword').innerText;
    
    let xhr = new XMLHttpRequest();
    xhr.open("GET", `/board/search/${keyword}?sort=${sort}&page=${currentPage}`, true);
            
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {
            // AJAX 요청이 성공적으로 완료되었을 때 뷰 업데이트
            let response = xhr.responseText;
            
            // response를 파싱해서 searchResults div 내용만 추출
            let parser = new DOMParser();
            let doc = parser.parseFromString(response, 'text/html');
            let searchResultsContent = 
                    doc.querySelector('#needToChange').innerHTML;
                    
            // searchResults div 내용 교체
            document.getElementById("needToChange").innerHTML = searchResultsContent;  
        }
    };
    
    xhr.send();
}

// 페이지 번호를 클릭할 때 해당 페이지로 Ajax 요청 보내기(HTML파일에서 사용할 함수)
function loadPage(newPage) {
    // 페이지 번호 업데이트
    currentPage = newPage;    
    boardWithSearchResults()
}


// 클릭된 컬럼에서 data-url 속성 값을 가져옴
function redirectToDetail(row) {
    let url = row.getAttribute("data-url");
        
    // 페이지 이동
    window.location.href = url;
}