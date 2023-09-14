let courseId;
let userId;
let isLiked;


window.onload = function() {
    courseId = document.getElementById('courseId').value;
    userId = document.getElementById('userId').value;
    let isLikedString = document.getElementById('isLiked').value;
    // isLikedString 이 "true"면 true를, 아니면 false 반환
    isLiked = isLikedString === "true";
    
    console.log("isLiked : " + isLiked);
    console.log("userId : " + userId);
    console.log("courseId: " + courseId);
    
    // isLiked 의 상태 확인하기
    if(isLiked) {
            console.log(`이 코스는 ${courseId}의 좋아요 상태입니다.`);
        } else {
            console.log(`이 코스는 ${courseId}의 좋아요 상태가 아닙니다.`);
        }
}

// 좋아요 아이콘 클릭할 때마다 바뀌는 이미지 설정
function toggleLikeImage(element) {
    let img = element.querySelector('img');
    // 경로를 처리하는 컨트롤러 메소드에서 @RequestBody 어노테이션이 붙은 매개변수가 있어서
    // 요청
    fetch(`http://localhost/api/courses/${courseId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({})
    })
    .then(() => {
        if (isLiked) {
        img.src = "/images/common/heart-nonclick.png";
        isLiked = false;
        console.log(`이 코스는 ${courseId}의 좋아요가 취소되었습니다.`);
        } else {
            img.src = "/images/common/heart-click.png";
            isLiked = true;
            console.log(`이 코스는 ${courseId}의 좋아요가 추가되었습니다.`);
        }
        
    })
}

// 좋아요 아이콘 마우스 오버 시 이미지 변경
function changeLikeImage(element) {
    let img = element.querySelector('img');
    //if (!isLiked) {
        img.src = "/images/common/heart-hover.png";
    //}
}

// 좋아요 아이콘 마우스 아웃 시 이미지 원래대로 변경
function restoreLikeImage(element) {
    let img = element.querySelector('img');
    if (!isLiked) {
        img.src = "/images/common/heart-nonclick.png";
    } else if(isLiked) {
        img.src = "/images/common/heart-click.png";
    }
}







