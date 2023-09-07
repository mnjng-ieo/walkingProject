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