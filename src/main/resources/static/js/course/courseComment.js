/**
 * 
 */
    console.log("자바스크립트 작동중")
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
                alert('댓글을 작성하려면 로그인 해주세요.');
                location.replace(window.location.href);
            }
        })
        
    })