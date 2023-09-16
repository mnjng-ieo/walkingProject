/**
 * 자바스크립트 추가하기
 */
let boardId;
let userId;
let isLiked;

window.addEventListener('load', function() {
    boardId = document.getElementById('boardId').value;
    userId = document.getElementById('userId').value;
    let isLikedString = document.getElementById('isLiked').value;
    // isLikedString 이 "true"면 true를, 아니면 false 반환
    isLiked = (isLikedString === "true");
    
//    console.log("isLiked : " + isLiked);
//    console.log("userId : " + userId);
//    console.log("courseId: " + courseId);
    
    // isLiked 의 상태 확인하기
    if(isLiked) {
//            console.log(`이 ${courseId} 코스는 ${userId}의 좋아요 상태입니다.`);
        } else {
//            console.log(`이 ${courseId} 코스는 ${userId}의 좋아요 상태가 아닙니다.`);
        }
    
});

function toggleLikeImage(element) {
    boardId = document.getElementById('boardId').value;
    userId = document.getElementById('userId').value;
    let img = element.querySelector('img');
    // 회원이 아니면 confirm 대화상자 표시
    if(userId != null && userId != '') {
        // 좋아요 처리 요청
        // 경로를 처리하는 컨트롤러 메소드에서 @RequestBody 어노테이션이 붙은 매개변수가 있어서
        fetch(`/api/board/${boardId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({})
        })
        .then(() => {
            // 좋아요 아이콘 상태 업데이트
            if (isLiked) {
                img.src = "/images/common/heart-nonclick.png";
                isLiked = false;
                //console.log(`이 코스는 ${boardId}의 좋아요가 취소되었습니다.`);
            } else {
                img.src = "/images/common/heart-click.png";
                isLiked = true;
                //console.log(`이 코스는 ${boardId}의 좋아요가 추가되었습니다.`);
            }
            // 좋아요 수(text) 업데이트
            updateLikeCount(boardId);
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
}

// 좋아요 수 업데이트 함수
function updateLikeCount(boardId) {
    // 좋아요 수 업데이트를 위한 Ajax 요청
    fetch(`/api/board/${boardId}/like-count`, {
        method: 'GET'
    })
    .then(response => response.json())
    .then(data => {
        const likeCountElement = document.getElementById('likeCnt');
        likeCountElement.textContent = data.likeCount; // 좋아요 수 업데이트
        //console.log('좋아요 수 : ' + data.likeCount);
    })
    .catch(error => {
        console.error('좋아요 수 업데이트 중 오류 발생: ', error);
    });
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
