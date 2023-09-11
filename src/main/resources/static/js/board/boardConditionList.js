let currentPage = 0; // currentPage 변수 초기화

// 버튼 클릭 이벤트 핸들러
document.getElementById('allBtn').addEventListener('click', function() {
    // 'allBtn' 클릭 시 모든 게시판 보이도록 설정
    window.location.href = 'board';
    //showAllBoards(); (미사용)
});

document.getElementById('freeBtn').addEventListener('click', function(event) {
    boardTypeChange(event);
});

document.getElementById('communityBtn').addEventListener('click', function(event) {
    boardTypeChange(event);
});

document.getElementById('reviewBtn').addEventListener('click', function(event) {
    boardTypeChange(event);
});

// XMLHttpRequest를 사용하여 서버로부터 데이터를 가져오는 함수
function boardTypeChange(event) {
    let xhr = new XMLHttpRequest();

    let type = event.target.getAttribute('data-type');
    
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {
            // 요청이 완료되고 성공적으로 데이터를 받았을 때 실행되는 코드
            let response = xhr.responseText;
            
            // response를 파싱해서 needToChange div 내용만 추출
            let parser = new DOMParser();
            let doc = parser.parseFromString(response, 'text/html');
            let typeResult = 
                    doc.querySelector('#needToChange').innerHTML;            
            // 결과를 표시할 요소를 찾아서 내용을 업데이트합니다.
            document.getElementById('needToChange').innerHTML = typeResult;
        }
    };

    // GET 요청을 보냅니다.
    xhr.open('GET', "/board?type=" + type + 
        '&page=' + currentPage, true);

    xhr.send();
}

// 검색 조건 수집
function boardWithSearchResults() {
    let sort = document.getElementById('boardSort').value;
    
    let xhr = new XMLHttpRequest();
    xhr.open('GET', `/board?sort=${sort}&page=${currentPage}`, true);
            
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
    boardTypeChange();
}

// ajax로 이동하는 방법(전체보기) - 사용하지 않고 window.location.href = 'board';를 사용해도 됨
function showAllBoards() {
    // 모든 게시물을 화면에 보이도록 설정
    let xhr = new XMLHttpRequest();
    
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {
            // 요청이 완료되고 성공적으로 데이터를 받았을 때 실행되는 코드
            let response = xhr.responseText;
            
            // response를 파싱해서 needToChange div 내용만 추출
            let parser = new DOMParser();
            let doc = parser.parseFromString(response, 'text/html');
            let typeResult = 
                    doc.querySelector('#needToChange').innerHTML;            
            // 결과를 표시할 요소를 찾아서 내용을 업데이트합니다.
            document.getElementById('needToChange').innerHTML = typeResult;
        }
    };

    // GET 요청을 보냅니다.
    xhr.open('GET', '/board');  

    xhr.send();
}

// 클릭된 컬럼에서 data-url 속성 값을 가져옴
function redirectToDetail(row) {
    let url = row.getAttribute("data-url");
        
    // 페이지 이동
    window.location.href = url;
}