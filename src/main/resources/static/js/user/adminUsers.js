// 페이지
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
    xhr.open('GET', '/admin/users?page=' + pageNumber, true);
    xhr.send();
}