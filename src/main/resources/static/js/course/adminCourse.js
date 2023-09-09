//*** 관리자 산책로 상세 페이지에서 사용 */

// 목록으로 돌아가기 버튼
function redirectToCourses() {
	window.location.href = '/admin/courses';
}

// 사용자 페이지에서 보기 - 새 탭으로 이동
function openUserPageInNewTab(courseId) {
	window.open('/course/' + courseId, '_blank');
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
        })
        .catch(error => {
                console.error('삭제 중 오류 발생 : ', error);
        });
    } else {
        // 아니오를 선택한 경우 아무 작업 수행하지 않고 현재 페이지에 머무르기
    }
}

// [수정 페이지] 수정 기능 - 수정 완료 버튼
function updateCourse(courseId) {
    console.log('courseId : ' + courseId);
            
    fetch(`/api/admin/courses/${courseId}`, {
        method: 'PATCH',
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            courseId: courseId,
            wlkCoursFlagNm: document.getElementById('wlkCoursFlagNm').value,
            wlkCoursNm: document.getElementById('wlkCoursNm').value,
            coursDc: document.getElementById('coursDc').value,
            signguCn: document.getElementById('signguCn').value,
            coursLevelNm: document.getElementById('coursLevelNm').value,
            coursLtCn: document.getElementById('coursLtCn').value,
            coursDetailLtCn: document.getElementById('coursDetailLtCn').value,
            aditDc: document.getElementById('aditDc').value,
            coursTimeCn: document.getElementById('hours').value
                         + ':' + document.getElementById('minutes').value
                         + ':' + document.getElementById('seconds').value,
            toiletDc: document.getElementById('toiletDc').value,
            cvntlNm: document.getElementById('cvntlNm').value,
            lnmAddr: document.getElementById('lnmAddr').value,
            coursSpotLa: document.getElementById('coursSpotLa').value,
            coursSpotLo: document.getElementById('coursSpotLo').value
        })
    })
    .then(response => response.json())
    .then(data => {
        alert('수정이 완료되었습니다.');
        location.replace(`/admin/courses/${courseId}`);
    })
    .catch(error => {
        console.error('수정 중 오류 발생 : ', error);
    });
}

// [등록페이지] 등록 기능
function insertCourse() {
    fetch("/api/admin/courses", {
        method: 'POST',
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            wlkCoursFlagNm: document.getElementById('wlkCoursFlagNm').value,
            wlkCoursNm: document.getElementById('wlkCoursNm').value,
            coursDc: document.getElementById('coursDc').value,
            signguCn: document.getElementById('signguCn').value,
            coursLevelNm: document.getElementById('coursLevelNm').value,
            coursLtCn: document.getElementById('coursLtCn').value,
            coursDetailLtCn: document.getElementById('coursDetailLtCn').value,
            aditDc: document.getElementById('aditDc').value,
            coursTimeCn: document.getElementById('hours').value
                         + ':' + document.getElementById('minutes').value
                         + ':' + document.getElementById('seconds').value,
            toiletDc: document.getElementById('toiletDc').value,
            cvntlNm: document.getElementById('cvntlNm').value,
            lnmAddr: document.getElementById('lnmAddr').value,
            coursSpotLa: document.getElementById('coursSpotLa').value,
            coursSpotLo: document.getElementById('coursSpotLo').value
        })
    })
    .then(response => response.json())
    .then(data => {
        alert('등록이 완료되었습니다.');
        // `/admin/courses/${courseId}`로 이동하면 좋겠는데 courseId는 어떻게 바로 얻을까?
        location.replace(`/admin/courses`);
    })
    .catch(error => {
        console.error('등록 중 오류 발생 : ', error);
    });
}

// 필수 입력 필드의 유효성 검사 -> 왜인지 수정페이지에서는 값이 다 지워져도 적용되지 않는다. 
window.onload = function() {
    
    // 필수 입력 필드의 배열 가져오기
    const requiredFields = document.querySelectorAll('[required]');
    
    // 등록, 수정 버튼 가져오기
    const updateFinishButton = document.getElementById('updateFinishButton');
    const insertFinishButton = document.getElementById('insertFinishButton');
    
    // 필수 입력 필드가 변경될 때마다 확인
    requiredFields.forEach(field => {
        field.addEventListener('input', validateFields);  // 값 수정할 때마다 발생 ; 등록페이지에서 비어있는 필드를 채울 때 확인
        field.addEventListener('change', validateFields); // 요소 변경이 끝나면 발생; 수정페이지에서 원래 채워져있다가 값 수정(지울 때)도 확인 
    });
    
    // 입력 필드 검증 함수
    function validateFields() {
        let allFieldsValid = true;
    
        requiredFields.forEach(field => {
    // field : 현재 검증 중인 입력 필드를 나타냄. 
    // value : 입력 필드에 입력된 값 가져옴
    // trim() : 자바스크립트 문자열 메서드 중 하나. 문자열 앞뒤 공백 제거.
    // -> 입력 필드에서 입력된 값을 앞뒤 공백 제거한 형태로 반환. 
    //    사용자가 공백만 입력하더라도 해당 필드가 비어있는 것으로 간주해서 입력값의 유효성 검증.
    // -> if(!field.value.trim()) : 입력 필드값이 비어있거나 공백으로만 이루어져 있다면 ~
            if (!field.value.trim()) {
                allFieldsValid = false;
            }
        });
    
        // 모든 필수 입력 필드가 채워져 있으면 버튼 활성화, 그렇지 않으면 비활성화
        if (allFieldsValid) {
            insertFinishButton.removeAttribute('disabled');
            updateFinishButton.removeAttribute('disabled');
        } else {
            insertFinishButton.setAttribute('disabled', 'true');
            updateFinishButton.removeAttribute('disabled', 'true');
            //alert('필수 입력 조건이 충족되지 않았습니다.');
            // => 아예 해당 버튼이 클릭되지 않도록 함!
        }
    }
    
    // 초기에도 검증 함수 실행 (페이지 로드시 활성/비활성 상태 설정)
    validateFields();
}
