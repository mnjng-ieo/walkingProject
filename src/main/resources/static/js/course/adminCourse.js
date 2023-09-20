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
    
    // 이미지 업로드를 위한 FormData 객체 생성 
    // -> 사용자가 파일을 선택하지 않았거나 이미지 업로드 시도하지 않으면 undefined,
    //    FormData에 실리지 않게 된다.
    const formData = new FormData();
    const imageUploadInput = document.getElementById('imageUploadInput');
    
    // (최종 업로드 취소되지 않은) 새로 업로드된 파일이 있는지 확인
    if (imageUploadInput.files.length === 0) {
        if (boardImage.src == '/images/defaultCourseMainImg_modify.png'){
            // 이미지를 최종 선택하지 않은 경우 (이미지 삭제)
            formData.append('ifNewImageExists', 0);
        } else {
            // 이미지가 변경되지 않은 경우 (이미지 유지)
            formData.append('ifNewImageExists', 2);
        }
   } else {
      // 이미지를 최종 선택한 경우 (이미지 변경)
      formData.append('ifNewImageExists', 1);
      formData.append('file', imageUploadInput.files[0]);
   }
    
    // HH:mm:ss 형식으로 들어가기 위한 설정
    // 세 개의 input 태그가 모두 '' 이 아닐 경우 사이에 :가 들어간 문자열 데이터가 들어감.
    // 아니면 coursTimeCn 전체가 null로 들어감.
    let hours = document.getElementById('hours').value;
    let minutes = document.getElementById('minutes').value;
    let seconds = document.getElementById('seconds').value;
    
    let coursTimeCn;
    if (hours != '' && minutes != '' && seconds != ''){
        coursTimeCn = hours + ':' + minutes + ':' + seconds;
    } else {
        coursTimeCn = null;
    }
    
    // 산책로 데이터를 포함한 요청 데이터 생성
    const dto = {
        courseId: courseId,
        wlkCoursFlagNm: document.getElementById('wlkCoursFlagNm').value,
        wlkCoursNm: document.getElementById('wlkCoursNm').value,
        coursDc: document.getElementById('coursDc').value,
        signguCn: document.getElementById('signguCn').value,
        coursLevelNm: document.getElementById('coursLevelNm').value,
        coursLtCn: document.getElementById('coursLtCn').value,
        coursDetailLtCn: document.getElementById('coursDetailLtCn').value,
        aditDc: document.getElementById('aditDc').value,
        coursTimeCn: coursTimeCn,
        toiletDc: document.getElementById('toiletDc').value,
        cvntlNm: document.getElementById('cvntlNm').value,
        lnmAddr: document.getElementById('lnmAddr').value,
        coursSpotLa: document.getElementById('coursSpotLa').value,
        coursSpotLo: document.getElementById('coursSpotLo').value
    };
    
    // 문자열 데이터를 JSON으로 변환하여 FormData에 추가
    // - Blob : 데이터를 처리하는 객체
    formData.append('dto', 
            new Blob([JSON.stringify(dto)], 
                    {type: "application/json"}));
    
    // 이미지 업로드 및 산책로 데이터 수정 요청        
    fetch(`/api/admin/courses/${courseId}`, {
        method: 'PATCH',
        body: formData
    })
    .then((response) => {
        if(!response.ok) {
            throw new Error('수정 오류 발생');
        }
        return response.json();
    })
    .then((data) => {
        alert('수정이 완료되었습니다.');
        location.replace(`/admin/courses/${courseId}`);
    })
    .catch(error => {
        console.error('수정 중 오류 발생 : ', error);
    });
}
// [등록페이지] 등록 기능
//  : 이미지를 먼저 업로드하고 이미지 URL을 받아온 후
//    이를 courseRequestDTO에 추가하여 전체 데이터를 서버로 보내는 방식
function insertCourse() {
    
    // 이미지 업로드를 위한 FormData 객체 생성
    const formData = new FormData();
    const imageUploadInput = document.getElementById('imageUploadInput');
    formData.append('file', imageUploadInput.files[0]);
    
    // HH:mm:ss 형식으로 들어가기 위한 설정
    // 세 개의 input 태그가 모두 '' 이 아닐 경우 사이에 :가 들어간 문자열 데이터가 들어감.
    // 아니면 coursTimeCn 전체가 null로 들어감.
    let hours = document.getElementById('hours').value;
    let minutes = document.getElementById('minutes').value;
    let seconds = document.getElementById('seconds').value;
    
    let coursTimeCn;
    if (hours != '' && minutes != '' && seconds != ''){
            coursTimeCn = hours + ':' + minutes + ':' + seconds;
        } else {
            coursTimeCn = null;
        }
        
    // 산책로 데이터를 포함한 요청 데이터 생성
    const dto = {
        wlkCoursFlagNm: document.getElementById('wlkCoursFlagNm').value,
        wlkCoursNm: document.getElementById('wlkCoursNm').value,
        coursDc: document.getElementById('coursDc').value,
        signguCn: document.getElementById('signguCn').value,
        coursLevelNm: document.getElementById('coursLevelNm').value,
        coursLtCn: document.getElementById('coursLtCn').value,
        coursDetailLtCn: document.getElementById('coursDetailLtCn').value,
        aditDc: document.getElementById('aditDc').value,
        coursTimeCn: coursTimeCn,
        toiletDc: document.getElementById('toiletDc').value,
        cvntlNm: document.getElementById('cvntlNm').value,
        lnmAddr: document.getElementById('lnmAddr').value,
        coursSpotLa: document.getElementById('coursSpotLa').value,
        coursSpotLo: document.getElementById('coursSpotLo').value
    };
    
    // 문자열 데이터를 JSON으로 변환하여 FormData에 추가
    // - Blob : 데이터를 처리하는 객체
    formData.append('dto', 
                    new Blob([JSON.stringify(dto)], 
                             {type: "application/json"}));
    // 생성 요청 처리하면서 헤더에 저장한 courseId - 외부에 먼저 정의
    let courseId;
    
    // 이미지 업로드 및 산책로 데이터 생성 요청
    fetch("/api/admin/courses", {
        method: 'POST',
        body: formData,   // 이미지를 담고 있는 FormData 객체
    })
    // 서버로부터의 HTTP 응답 객체, 응답 정보 포함하고 있음
    .then((response) => {
        if(!response.ok) {
            throw new Error('등록 오류 발생');
        }
        // 응답 헤더에서 courseId 값 가져오기
        courseId = response.headers.get('courseId');
        console.log('courseId : ' + courseId);
        return response.json();
    })
    // 서버에서 반환된 JSON 데이터. 
    .then((data) => {
        alert('등록이 완료되었습니다.');
        // `/admin/courses/${courseId}`로 이동 가능
        if(courseId) {
            location.replace(`/admin/courses/${courseId}`);
        } else {
            location.replace(`/admin/courses`);
        }
    })
    .catch(error => {
        console.error('등록 중 오류 발생 : ', error);
    });
}

// 전역 범수 선언할 때 주의! 여러 JS파일 간에도 공유 가능하기 때문에 이름을 명확히 한다.
let courseFile; 

// 이미지 업로드 기능 : 뷰에서 img의 src 속성 바꾸기
function uploadImage() {
	const imageUploadInput = document.getElementById('imageUploadInput');
	// input 내용에 변화가 생기면, courseMainImage 요소의 src 속성 변경시키기
	// 사용자가 input 요소에서 파일을 선택하거나 변경할 때 발생
	imageUploadInput.addEventListener('change', function() {
		// 선택된 파일을 가져와 file 변수에 저장 (this = imageUploadInput)
		courseFile = this.files[0];
		// file이 선택되었을 때
		if (courseFile) {
			// 서버로 파일 업로드 요청을 보내는 코드 작성
			// 파일 업로드 후, 이미지 경로를 받아와서 이미지를 변경
			const reader = new FileReader();   // 파일을 읽기 위한 객체 생성
			reader.onload = function(e) {      // 파일 읽기 완료되면 호출
				const courseMainImage = document.getElementById('courseMainImage');
				courseMainImage.src = e.target.result;  // 읽은 파일의 데이터로 src 속성 설정
			};
			reader.readAsDataURL(courseFile);
		} else {
            // 파일 선택이 취소되었을 경우 file을 어떻게 제거할까? - deleteImage()
        }
	});
	
	// input 요소 클릭하여 파일 선택 다이얼로그 열기 
	imageUploadInput.click(); // ---> 클릭한 것과 같은 효과!
}

// 이미지 업로드 취소 기능 ; 이미지를 기본 이미지로 변경
// 취소하면 다시 이미지를 등록해주세요와 기본이미지 두 개 보이기
function deleteImage() {
	const courseMainImage = document.getElementById('courseMainImage');
	courseMainImage.src = '/images/defaultCourseMainImg.jpg';
    
    // file 변수를 초기화(삭제)
    courseFile = null;
}

// 이미지 업로드 input에 기존 이미지 경로를 채우는 함수
// ↳ 수정 폼에서 이미지의 변화가 없었을 때도 처리하기 위해 필요!
/*function fillImageInputWithExistingImage(imagePath) {
    const imageUploadInput = document.getElementById('imageUploadInput');
    // 이미지 업로드 input의 value를 기존 이미지 경로로 설정
    imageUploadInput.value = imagePath;
}*/

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
        
        if(insertFinishButton != null){
	        // 모든 필수 입력 필드가 채워져 있으면 버튼 활성화, 그렇지 않으면 비활성화
	        if (allFieldsValid) {
	            insertFinishButton.removeAttribute('disabled');
	        } else {
	            insertFinishButton.setAttribute('disabled', 'true');
	            //alert('필수 입력 조건이 충족되지 않았습니다.');
	            // => 아예 해당 버튼이 클릭되지 않도록 함!			
	        }
		}
		if(updateFinishButton != null){
	        // 모든 필수 입력 필드가 채워져 있으면 버튼 활성화, 그렇지 않으면 비활성화
	        if (allFieldsValid) {
	            updateFinishButton.removeAttribute('disabled');
	        } else {
	            updateFinishButton.removeAttribute('disabled', 'true');
	            //alert('필수 입력 조건이 충족되지 않았습니다.');
	            // => 아예 해당 버튼이 클릭되지 않도록 함!			
			}

        }
    }
    
    // 초기에도 검증 함수 실행 (페이지 로드시 활성/비활성 상태 설정)
    validateFields();
}