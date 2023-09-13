
// 캐러셀 적용
$(document).ready(function(){
	$('#course-carousel').carousel();
});


// 각 상세페이지로 경로 넘어가는 클릭 이벤트
function redirectToCourseDetails(courseId){
    let url = "/course/" + courseId;
    window.location.href = url;
}


// 해시태그 클릭 시, Ajax 이용하여 관련된 게시물 출력되도록.

// 현재 페이지 초기화
let currentPage = 0; 

// 검색 조건 수집
function loadPostsByTag(clickedElement) {
	//alert('해시태그 클릭');
	let tagContent = $(clickedElement).html();	// 클릭한 요소에서 tagId 추출 
	//console.log("Clicked tagContent: ", tagContent);

  	// XMLHttpRequest 객체 생성
    let xhr = new XMLHttpRequest();
   
    // 요청을 열고 설정
    xhr.open("GET", `/api/tag/board?tagContent=${tagContent}&page=${currentPage}`, true);

    // 응답 처리
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            // 서버 응답이 성공적으로 도착한 경우, AJAX 요청의 응답 데이터를 가져옴 
            let response = xhr.responseText;
            
            let parser = new DOMParser();
            let doc = parser.parseFromString(response, 'text/html');
            
            // AJAX로 가져온 새로운 데이터를 표시할 곳 : searchTagBoard
            let searchTagBoard = doc.getElementById("needToChange").innerHTML;
            
            // searchTagBoard div 내용 교체 
            document.getElementById("needToChange").innerHTML = searchTagBoard;
        }
    };

    // 요청 보내기
    xhr.send();
}


// 출력된 결과에서 상세화면 보기
// 클릭된 컬럼에서 data-url 속성 값을 가져옴
function redirectToDetail(row) {
    let url = row.getAttribute("data-url");
        
    // 페이지 이동
    window.location.href = url;
}