$(document).ready(function () {
    // 첫 페이지에서 게시물만 보이게
    $('#postContainer').collapse('show');
    $('#imgContainer').collapse('hide')
    
    // imgBtn 버튼을 클릭할 때
    $('#imgBtn').on('click', function () {
        // 열려있는지 확인
        if (!$('#imgContainer').hasClass('show')) {
            $('#postContainer').collapse('hide');
            $('#imgContainer').collapse('show');
        }
        
    });
    
    // postBtn 버튼을 클릭할 때
    $('#postBtn').on('click', function () {
        // id=imgContainer가 열려 있다면 닫기

        // id=postContainer가 닫혀 있다면 열기
        if (!$('#postContainer').hasClass('show')) {
            $('#postContainer').collapse('show');
            $('#imgContainer').collapse('hide');
        }
    });


});

    
// 내가 쓴 게시글 클릭하여 게시물 상세보기
function redirectToDetail(row) {
    let url = row.getAttribute("data-url");
            
    // 페이지 이동
    window.location.href = url;
}

// 페이지를 로드할 때 초기 데이터를 불러온다.
function loadPage(pageNumber) {
    let xhr = new XMLHttpRequest();

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) { // 요청이 완료됨
            if (xhr.status === 200) { // 요청이 성공적으로 처리됨
            
            let response = xhr.responseText;
            // response를 파싱해서 searchResults div 내용만 추출
            let parser = new DOMParser();
            let doc = parser.parseFromString(response, 'text/html');
            let searchResultsContent = 
                    doc.querySelector('#needToChange').innerHTML;
                    
            // searchResults div 내용 교체
            document.getElementById("needToChange").innerHTML = searchResultsContent;                      

            } else {
                // 요청이 실패한 경우의 처리
                console.log('Error:', xhr.status, xhr.statusText);
            }
        }
    };

    // GET 요청을 보냄
    xhr.open('GET', '/main/mypage?page=' + pageNumber, true);
    xhr.send();
}