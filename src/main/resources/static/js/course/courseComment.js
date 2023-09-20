/**
 * 
 */
    //console.log("자바스크립트 작동중")
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
        $(this).parent().parent().css('display', 'none');
        $(this).parent().parent().next().css('display', 'block');
    })

    // 삭제버튼을 눌렀을때
    $(".deletebtn").on("click",
    function(){
        let commentId = $(this).parent().parent().prev().children("#commentId").val()
        //alert(`삭제 버튼 클릭, commentId = ${commentId}`)
        fetch('/api/comment/'+commentId,{
            method: 'DELETE'
        })
        .then((response)=>  {
            if(response.ok){
                alert('삭제가 완료되었습니다.');
                location.replace(window.location.href);
            } else{
                alert('잘못된 접근입니다.');
            }
        })
        
    })
    // 수정등록버튼을 눌렀을때
    $(".registerbtn").on("click",
    function(){
        let commentId = $(this).parent().parent().children("#commentId").val()
        let modifyContent = $(this).parent().parent().children("#commentContent").val()
        //alert(`수정등록 버튼 클릭, commentId = ${commentId}`)
        fetch('/api/comment/'+commentId,{
            method: 'PUT',
            headers:{
                    "Content-Type": "application/json"
            },
            body: JSON.stringify({
                commentContent : modifyContent,
            })
        })
        .then((response)=>  {
            if(response.ok){
                alert('수정이 완료되었습니다.');
                location.replace(window.location.href);
            } else{
                alert('잘못된 접근입니다.');
                location.replace(window.location.href);
            }
        })
        
    })

    // 등록버튼을 눌렀을때
    $("#createbtn").on("click",
    function(){
        // alert(`등록 버튼 클릭`)
        fetch('/api/comment'+window.location.pathname,{
            method: 'POST',
            headers:{   
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                commentContent : document.getElementById("createContent").value
            })
        })
        .then((response)=>  {
            if(response.ok){
                alert('등록이 완료되었습니다.');
                location.replace(window.location.href);
            } else{
                alert('잘못된 접근입니다.');
                location.replace(window.location.href);
            }
        })
        
    })
    
    	// 좋아요를 클릭했을때
	$(".commentLike").on("click",
	function(){
		let commentId = $(this).parent().parent().prev().children("#commentId").val()
		let userId = document.getElementById('userId').value;
		
		// 회원이 아니면 confirm 대화상자 표시
	    if(userId != null && userId != '') {
	        // 좋아요 처리 요청
	        // 경로를 처리하는 컨트롤러 메소드에서 @RequestBody 어노테이션이 붙은 매개변수가 있어서
	        fetch(`/api/comment/like/${commentId}`, {
	            method: 'POST',
	            headers: {
	                'Content-Type': 'application/json'
	            },
	            body: JSON.stringify({})
	        })
	        .then(() => {
	            // 좋아요 아이콘 상태 업데이트
	            if (isLiked) {
	                //console.log(`이 코스는 ${commentId}의 좋아요가 취소되었습니다.`);
	            } else {
	                //console.log(`이 코스는 ${commentId}의 좋아요가 추가되었습니다.`);
	            }
	            // 좋아요 수(text) 업데이트
	            fetch(`/api/comment/like/${commentId}/like-count`, {
			        method: 'GET'
			    })
			    .then(response => response.json())
			    .then(data => {
			        const likeCountElement = $(this).children('#commentLikeCnt');
			        likeCountElement.text(data.likeCount); // 좋아요 수 업데이트
			        //console.log('좋아요 수 : ' + data.likeCount);
			    })
			    .catch(error => {
			        console.error('좋아요 수 업데이트 중 오류 발생: ', error);
			    });
	        })
	    } else {
	        let confirmation = 
	            confirm('로그인이 필요한 서비스입니다. 로그인 페이지로 이동하시겠습니까?');
	        if(confirmation) {
	            window.location.href = '/login';
	        } else {
	            // 취소 버튼 누르면 아무 동작 없이 현재 페이지에 머무르기
	        }
	    }
		
	})

	// 좋아요를 마우스오버했을때
	$(".commentLike").on("mouseover",
	function(){
		let img = $(this).children('img');	
		img.attr("src", "/images/common/heart-hover.png");
	})
	
	// 좋아요를 마우스아웃했을때
	$(".commentLike").on("mouseout",
	function(){
		let img = $(this).children('img');	
		img.attr("src", "/images/common/heart-click.png");
	})
	
	function goMemberPage(dataUrl){
		let url = dataUrl.getAttribute("data-url");
		// 페이지 이동
    	window.location.href = url;
	}