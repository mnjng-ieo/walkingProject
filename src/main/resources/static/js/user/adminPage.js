window.onload = function(){
	// MoveUserPage 버튼 클릭 시, 사용자 관리 페이지로 이동 
	document.getElementById('MoveUserPage').addEventListener("click", function() {
	      window.location.href = '/admin/users';
	});
	
	// MoveAdminCoursePage 버튼 클릭 시, 사용자 관리 페이지로 이동 
	document.getElementById('MoveAdminCoursePage').addEventListener("click", function() {
	      window.location.href = '/admin/courses';
	});
}