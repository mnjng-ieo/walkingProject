// 목록으로 돌아가기 버튼
function redirectToCourses() {
	window.location.href = '/admin/courses';
}

// 사용자 페이지에서 보기 - 새 탭으로 이동
function openUserPageInNewTab(courseId) {
	window.open('/course/' + courseId, '_blank');
}

// 삭제 기능
const deleteButton = document.getElementById('deleteButton');
if(deleteButton) {
	deleteButton.addEventListener('click', event => {
		
		let id = document.getElementById('courseId').value;
		
		// 삭제 요청 확인 대화 상자 표시
		const confirmDelete = confirm('정말로 ' + id + '번 산책로를 삭제하시겠습니까?');
		
		if(confirmDelete) {
			fetch(`/api/admin/courses/{id}`, {
			method: 'DELETE'
			})
			.then(() => {
				alert('삭제가 완료되었습니다.');
				location.replace('/admin/courses');
			});
		} else {
			// 아니오를 선택한 경우 아무 작없 수행하지 않고 현재 페이지에 머무르기
		}
	});
}

// 수정 기능
const updateFinishButton = document.getElementById('updateFinishButton');
if (modifyButton) {
    modifyFinishButton.addEventListener('click', event => {
        // 현재 페이지의 URL에서 쿼리 문자열 부분을 나타냄
        // get() 을 통해 해당 매개변수의 값을 가져올 수 있다.
        let params = new URLSearchParams(location.search);
        let id = params.get('id');
        
        fetch(`/api/admin/courses/${id}`, {
            method: 'PUT',
            header: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                title: document.getElementById('title').value,
                content: document.getElementById('content').value
            })
        })
        .then(() => {
            alert('수정이 완료되었습니다.');
            location.replace(`/admin/courses/${id}`);
        });
    });
}