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
	// 등록 버튼을 눌렀을 때
/*	$("등록버튼").on("click",
	function(){
		let reqData = new Object();
		reqData = $(this).parent().parent().serializeArray();
		let xhr = new XMLHttpRequest()
		xhr.onreadystatechange = function(){
			if(xhr.readyState === XMLHttpRequest.DONE){
				if(xhr.status === 200){
					let result = xhr.responseText
					$(".commentList") = xhr 
					// list 전체 넣기
					let parser = new DOMParser();
					let doc = parser.parseFromString(result, 'text/html');
           	 		let searchResultsContent = 
                    doc.querySelector('#searchResults').innerHTML;
                    
            // searchResults div 내용 교체
            		document.getElementById("searchResults").innerHTML = searchResultsContent; 
				} else {
					/// 서버쪽 예외 응답코드 출력
					alert(`Ajax 서버 요청처리 예외 발생(${xhr.status})!`)
				}
			}
		} // onreadystatechange 이벤트리스너 끝
		xhr.open(
				"POST",
				"/ajax/postAgeByName"
				)
		xhr.responseType = "json";
		xhr.setRequestHeader(
				'Content-Type'
				, 'application/json')
		xhr.send(JSON.stringify(reqData))
	})*/
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