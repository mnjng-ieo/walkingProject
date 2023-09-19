let courseId;
let userId;
let isLiked;

// 좋아요 확인 + 지도 기능 구현 
window.onload = function(){
    courseId = document.getElementById('courseId').value;
    userId = document.getElementById('userId').value;
    let isLikedString = document.getElementById('isLiked').value;
    // isLikedString 이 "true"면 true를, 아니면 false 반환
    isLiked = (isLikedString === "true");
    
    //console.log("isLiked : " + isLiked);
    //console.log("userId : " + userId);
    //console.log("courseId: " + courseId);
    
    // isLiked 의 상태 확인하기
    if(isLiked) {
            //console.log(`이 ${courseId} 코스는 ${userId}의 좋아요 상태입니다.`);
        } else {
            //console.log(`이 ${courseId} 코스는 ${userId}의 좋아요 상태가 아닙니다.`);
        }
    
    // 지도 기능 구현
    function getValue(valueName){
		//console.log(valueName + " : " + document.getElementById(valueName).value)
        return document.getElementById(valueName).value
    }
    let flagName = getValue("flagName")
    let courseName = getValue("courseName")
    let courseLat = getValue("courseLat")
    let courseLng = getValue("courseLng")
    let courseAddr = getValue("courseAddr")
    let courseLength = getValue("courseLength")
    let courseTime = getValue("courseTime")
    let otherCourseName = document.getElementsByClassName("otherCourseName")
    let otherCourseId = document.getElementsByClassName("otherCourseId")
    
    // 경도, 위도 변수 
    let mapContainer = document.getElementById('map'), // 지도를 표시할 div 
    mapOption = { 
        center: new kakao.maps.LatLng(parseFloat(courseLat)+0.003, courseLng), // 지도의 중심좌표
        level: 5 // 지도의 확대 레벨
    };

    let map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다
    
    // 지도에 마커를 표시합니다 
    let marker = new kakao.maps.Marker({
        map: map, 
        position: new kakao.maps.LatLng(courseLat, courseLng)
    });
    
    // 커스텀 오버레이에 표시할 컨텐츠 입니다
    // 커스텀 오버레이는 아래와 같이 사용자가 자유롭게 컨텐츠를 구성하고 이벤트를 제어할 수 있기 때문에
    // 별도의 이벤트 메소드를 제공하지 않습니다 
    let content = '<div class="wrap fa-bounce">' + 
                '    <div class="info">' + 
                '        <div class="title ellipsis">' +
                '           <i class="icon fa-solid  fa-bounce fa-person-walking fa-lg"></i> ' + 
                '            <span class="fa-bounce">' + flagName + '</span>' +
                '            <div class="close " onclick="closeOverlay()" title="닫기"><i class="fa-solid fa-x"></i></div>' + 
                '        </div>' + 
                '        <div class="body">' + 
                '            <div class="desc">'
    for(let i = 0; i < Math.min(5, otherCourseName.length); i++){
		//console.log('content 만드는 중')
        content += '                <div class="textsm ellipsis'
        if(courseId == otherCourseId[i].value){
            content += ' now'
        }
        content += '"><a href="/course/' + otherCourseId[i].value + '">'
                    + '<i class="fa-solid fa-arrow-up-right-from-square icon fa-xs"></i> ' + otherCourseName[i].value + ' </a></div>'
    }
    if(otherCourseName.length > 5){
        content += '                <div class="moreClose" id="moreClose"><i class="fa-solid fa-angle-down"></i> 더보기</div>'
        content += '                <div class="moreOpenContent" id="moreOpenContent">'
            for(let i = 5; i < otherCourseName.length; i++){
                content += '                <div class="textsm ellipsis'
                    if(courseId == otherCourseId[i].value){
                        content += ' now'
                    }
                    content += '"><a href="/course/' + otherCourseId[i].value + '">'
                    +'<i class="fa-solid fa-arrow-up-right-from-square icon fa-xs"></i> ' + otherCourseName[i].value + '</a></div>'
                
            }
        content += '                <div class="moreOpen" id="moreOpen"><i class="fa-solid fa-angle-up"></i> 더보기</div>'
        content += '                </div>' 
    }          
    content +=  '            </div>' + 
                '        </div>' + 
                '    </div>' +    
                '</div>';
                       

    // 마커 위에 커스텀오버레이를 표시합니다
    // 마커를 중심으로 커스텀 오버레이를 표시하기위해 CSS를 이용해 위치를 설정했습니다
    let overlay = new kakao.maps.CustomOverlay({
        content: content,
        map: map,
        position: marker.getPosition()       
    });
    // 마커를 클릭했을 때 커스텀 오버레이를 표시합니다
    kakao.maps.event.addListener(marker, 'click', function() {
        overlay.setMap(map);
    });
    
    let moreOpenContent = document.getElementById("moreOpenContent")
    let moreClose = document.getElementById("moreClose")
    let moreOpen = document.getElementById("moreOpen")
    if(moreClose != null){		
	    moreClose.addEventListener('click', function(){
	        moreOpenContent.style.display ="block"
	        moreClose.style.display="none"
	    })
	}
	if(moreOpen != null){		
	    moreOpen.addEventListener('click', function(){
	        moreOpenContent.style.display ="none"
	        moreClose.style.display="block"
	    })
	}
    // 커스텀 오버레이를 닫기 위해 호출되는 함수입니다 
    function closeOverlay() {
        overlay.setMap(null);     
    }
}

// 좋아요 아이콘 클릭할 때마다 바뀌는 이미지 설정
function toggleLikeImage(element) {
    courseId = document.getElementById('courseId').value;
    userId = document.getElementById('userId').value;
    let img = element.querySelector('img');
    
    // 회원이 아니면 confirm 대화상자 표시
    if(userId != null && userId != '') {
        // 좋아요 처리 요청
        // 경로를 처리하는 컨트롤러 메소드에서 @RequestBody 어노테이션이 붙은 매개변수가 있어서
        fetch(`/api/courses/${courseId}`, {
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
                //console.log(`이 코스는 ${courseId}의 좋아요가 취소되었습니다.`);
            } else {
                img.src = "/images/common/heart-click.png";
                isLiked = true;
                //console.log(`이 코스는 ${courseId}의 좋아요가 추가되었습니다.`);
            }
            // 좋아요 수(text) 업데이트
            updateLikeCount(courseId);
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
function updateLikeCount(courseId) {
    // 좋아요 수 업데이트를 위한 Ajax 요청
    fetch(`/api/courses/${courseId}/like-count`, {
        method: 'GET'
    })
    .then(response => response.json())
    .then(data => {
        const likeCountElement = document.getElementById('likeCnt');
        likeCountElement.textContent = '좋아요  ' + data.likeCount; // 좋아요 수 업데이트
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

// 게시물 작성하기 클릭 이벤트
function goToBoardEditor(courseId) {
    // 회원이 아니면 confirm 대화상자 표시
    userId = document.getElementById('userId').value;
    if(userId != null && userId != '') {
        window.location.href = '/board-editor?course=' + courseId;
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

// 모달 창 닫기 (사용안함)
window.onclick = function(event) {
    var modal = document.getElementById('loginModal');
    if (event.target == modal) {
        modal.style.display = "none";
    }
}


// 링크 복사하기 클릭 이벤트
function copyToClipboard() {
    // 현재 페이지의 URL 가져오기
    let currentPageURL = window.location.href;
    
    // URL을 클립보드에 복사
    navigator.clipboard.writeText(currentPageURL).then(function() {
        alert('산책로의 링크가 클립보드에 복사되었습니다.');
    }).catch(function(err) {
        console.error('링크 복사 중 오류 발생: ', err);
    });
}

// 코멘트 작성폼으로 이동
function commentFocus(){
	let createContent = document.getElementById("createContent")
	createContent.focus()
}

