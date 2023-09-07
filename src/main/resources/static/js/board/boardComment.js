/**
 * 
 */
window.onload = function(){

 // * 수정 버튼 클릭 시, 
 //   수정과 삭제 버튼 사라지게 하고, 등록 버튼 나타나도록.
 		
 // JQuery 버튼 이벤트
	// 수정 버튼을 눌렀을 때
	$(".modifybtn").on("click",
	function(){
		$(this).parent().parent().css('display', 'none');
		$(this).parent().parent().prev().css('display', 'block');
	})
	// 취소 버튼을 눌렀을 때
	$(".cancelbtn").on("click",
	function(){
		$(this).parent().parent().parent().css('display', 'none');
		$(this).parent().parent().parent().next().css('display', 'block');
	})

	// 삭제버튼을 눌렀을때
	$(".deletebtn").on("click",
	function(){
		let commentId = $(this).parent().parent().prev().children().children("#commentId").val()
		//alert(`삭제 버튼 클릭, commentId = ${commentId}`)
		fetch('/api/comment/'+commentId,{
			method: 'DELETE'
		})
		.then(()=>	{
			alert('삭제가 완료되었습니다.');
			location.replace(window.location.href);
		})
		
	})
	
	



}