let courseSearchBtn = document.getElementById('courseSearchBtn');
let courseResetBtn = document.getElementById('courseResetBtn');

// 현재 페이지 초기화
let currentPage = 0;

// AJAX 요청 : 검색 조건 수집
function updateViewWithSearchResults(){
    let region = document.getElementById('region').value;
    let level = document.getElementById('level').value;
    let distance = document.getElementById('distance').value;
    let time = document.getElementById('time').value;
    let searchTargetAttr = document.getElementById('searchTargetAttr').value;
    let searchKeyword = document.getElementById('searchKeyword').value;
    let sort = document.getElementById('sort').value;
    
    let xhr = new XMLHttpRequest();
    // 페이지 번호까지 쿼리 파라미터로 추가
    xhr.open("GET", "/admin/courses?region=" + region + 
            "&level=" + level + "&time=" + time + "&distance=" + distance
            + "&searchTargetAttr=" + searchTargetAttr
            + "&searchKeyword=" + searchKeyword
            + "&sort=" + sort + "&page=" + currentPage, true);
    
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

// 초기화 버튼 클릭 이벤트
function loadReset() {
    window.location.href = '/admin/courses';
}

// 사용자 페이지에서 목록 확인하기 버튼 클릭 이벤트
function openUserPageInNewTab() {
	window.open('/course', '_blank');
}

// [목록, 상세 페이지] 수정 페이지로 이동
function loadUpdatePage(courseId) {
    window.location.href = '/admin/courses/update/' + courseId;
}

// [목록, 상세, 수정 페이지] 삭제 기능
function deleteCourse(courseId) {
        
    // 삭제 요청 확인 대화 상자 표시
    const confirmDelete = confirm('정말로 ' + courseId + '번 산책로를 삭제하시겠습니까?');
    
    if(confirmDelete) {
        fetch(`/api/admin/courses/${courseId}`, {
        method: 'DELETE'
        })
        .then(() => {
            alert('삭제가 완료되었습니다.');
            location.replace('/admin/courses');
        });
    } else {
        // 아니오를 선택한 경우 아무 작업 수행하지 않고 현재 페이지에 머무르기
    }
}

