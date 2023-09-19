// 지도 기능 구현 
window.onload = function(){
    function getValue(valueName){
        return document.getElementById(valueName)
    }
    
    // 커스텀 오버레이에 표시할 컨텐츠 변수 
    let courseId = getValue("courseId").value
    let flagName = getValue("flagName").innerHTML
    let courseName = getValue("courseName").innerHTML
    let courseLat = getValue("courseLat").innerHTML
    let courseLng = getValue("courseLng").innerHTML
    let courseAddr = getValue("courseAddr").innerHTML
    let courseLength = getValue("courseLength").innerHTML
    let courseTime = getValue("courseTime").innerHTML
    
    let savedImageName = getValue("savedImageName").value;
    let imagePath;
    if (savedImageName) {
        imagePath = '/upload-images/course/' + savedImageName;
    } else {
        imagePath = '/images/defaultCourseMainImg.jpg';
    }
    
    // 경도, 위도 변수 

    let mapContainer = document.getElementById('map'), // 지도를 표시할 div 
    mapOption = { 
        center: new kakao.maps.LatLng(courseLat, courseLng), // 지도의 중심좌표
        level: 3 // 지도의 확대 레벨
    };

    let map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다
    
    // 지도에 마커 표시
    let marker = new kakao.maps.Marker({
        map: map, 
        position: new kakao.maps.LatLng(courseLat, courseLng)
    });
    
    // 커스텀 오버레이에 표시할 컨텐츠
    // 커스텀 오버레이는 아래와 같이 사용자가 자유롭게 컨텐츠를 구성하고 이벤트를 제어할 수 있기 때문에
    // 별도의 이벤트 메소드를 제공하지 않습니다 
    let content = `<div class="wrap fa-bounce">
                      <div class="info">
                        <div class="title">
                            <i class="icon fa-solid fa-person-walking fa-lg fa-bounce"></i>
                            ${flagName} 
                            <div class="close" id="close" title="닫기"><i class="fa-solid fa-x"></i></div>
                        </div>
                        <div class="body">
                            <div class="img">
                                <img src="${imagePath}"
                                    style="width:100%; height:100%;">
                            </div>
                            <div class="desc">
                                <div class="ellipsis">${courseName}</div>
                                <div class="textsm ellipsis">${courseAddr}</div> 
                                <div class="textsm ellipsis">${courseLength} | ${courseTime}</div> 
                                <div><a href="/course/${courseId}" class="link">
                                <i class="fa-solid fa-arrow-up-right-from-square icon fa-xs"></i> 바로가기</a></div>
                            </div>
                        </div>
                    </div>  
                </div>`;
                
    // 마커 위에 커스텀오버레이를 표시합니다
    // 마커를 중심으로 커스텀 오버레이를 표시하기위해 CSS를 이용해 위치를 설정했습니다
    let overlay = new kakao.maps.CustomOverlay({
        content: content,
        map: map,
        position: marker.getPosition()       
    });
    //document.getElementById("close").addEventListener("click", closeOverlay);
    
    // 마커를 클릭했을 때 커스텀 오버레이를 표시합니다
    kakao.maps.event.addListener(marker, 'click', function() {
        overlay.setMap(map);
    });

    // 커스텀 오버레이를 닫기 위해 호출되는 함수입니다 
    function closeOverlay() {
        overlay.setMap(null);     
    }
}/**
 * 
 */