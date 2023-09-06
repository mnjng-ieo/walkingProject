/**
 * 
 */
window.onload = function(){

 // * 수정 버튼 클릭 시, 
 //   수정과 삭제 버튼 사라지게 하고, 등록 버튼 나타나도록.
 		let commentList = document.querySelector(".commentList").getElementsByTagName("div");
		for( i=0; i<commentList.length; i++){
			 $(commentList[i]);
		}
 		
 		
 		
		// 코멘트 내용 
		//let commentContent = document.getElementsByName('commentContent');
 		let commentContent = document.querySelector(".commentContent");
 		// 코멘트 작성란 
 		//let commentForm = document.getElementsByName('commentForm');
 		let commentForm = document.querySelector(".commentForm");
 		// 이전 댓글 내용 
 		//let previousComment = document.getElementsByName('content').innerHTML;
 		let content = document.querySelector(".content");
 		// 수정 후 댓글 내용 
 		//let savedComment = document.getElementsByName('savedContent').innerHTML;
 		let savedContent = document.querySelector(".savedContent");
 		
		// 수정, 삭제, 등록 버튼 
		//let modifyBtn = document.getElementsByName('modifybtn');
		let modifyBtn = document.querySelector(".modifybtn");
 		//let deleteBtn = document.getElementsByName('deletebtn');
		let deletebtn = document.querySelector(".deletebtn");
 		//let registerBtn = document.getElementsByName('registerbtn');
		let registerbtn = document.querySelector(".registerbtn");
 
 
	// 수정 클릭 이벤트 
		$(function() {
			$(modifybtn).on(("click"), function(){
			
			})
		})
		
 		function modify_click(){
 			//modifyBtn.addEventListener('click', function() {
			// let no = $(this).index();
			//alert('수정버튼 클릭이벤트 발생 ' + no + '버튼 클릭!');
	 		// 수정 버튼 클릭 시, 코멘트 내용이 코멘트 작성란으로 변경됨 
	 		commentContent.style.display = "none";
	 		commentForm.style.display = "block";
	 
		 	// 수정 및 삭제 버튼 대신 등록 버튼으로 변경됨 
			modifyBtn.style.display = "none";
	    	deleteBtn.style.display = "none";
			registerBtn.style.display = "block";
			
			// 이전 코멘트 내용 출력되도록.
			commentForm.querySelector('input').value = previousComment;	
    	}
    	
    	
	// 수정 후, 등록 클릭 이벤트 
    	function register_click(){
			alert('수정된 댓글 내용이 저장되었습니다.');
			// 등록 버튼 클릭 시, 코멘트 작성란이 코멘트 내용으로 변경됨  
	 		commentContent.style.display = "block";
	 		commentForm.style.display = "none";
	 		
		 	// 등록 버튼 대신 수정 및 삭제 버튼으로 변경됨 
			modifyBtn.style.display = "block";
	    	deleteBtn.style.display = "block";
			registerBtn.style.display = "none";
			
			// 변경된 코멘트 내용이 출력되도록.
			//savedCommentContent.getElementById('savedContent').value = savedComment;	
			
 		}
 		
 	// JQuery로 버튼 이벤트

	$(".commentList > div ").on("click",
	function(){
		let no = $(this).index();
		alert('수정버튼 클릭이벤트 발생 ' + no + '버튼 클릭!');
		commentContent[no].style.display = "none";
 		commentForm[no].style.display = "block";
 
	 	// 수정 및 삭제 버튼 대신 등록 버튼으로 변경됨 
		modifyBtn[no].style.display = "none";
    	deleteBtn[no].style.display = "none";
		registerBtn[no].style.display = "block";
	})
		 
	
}