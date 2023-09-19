// 관리자용 수정 시 지도 출력되는 함수 호출 - localhost/admin/courses/update/{id}
window.addEventListener('load', function() {
    let courseLat = document.getElementById('coursSpotLa')
    let courseLng = document.getElementById('coursSpotLo')

    let mapContainer = document.getElementById('map'), // 지도를 표시할 div 
    mapOption = { 
        center: new kakao.maps.LatLng(courseLat.value, courseLng.value), // 지도의 중심좌표
        level: 3 // 지도의 확대 레벨
    };

    let map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다

    // 지도를 클릭한 위치에 표출할 마커입니다
    let marker = new kakao.maps.Marker({ 
        // 지도 중심좌표에 마커를 생성합니다 
        position: map.getCenter() 
    }); 
    // 지도에 마커를 표시합니다
    marker.setMap(map);
    
    // 지도에 클릭 이벤트를 등록합니다
    // 지도를 클릭하면 마지막 파라미터로 넘어온 함수를 호출합니다
    kakao.maps.event.addListener(map, 'click', function(mouseEvent) {        
        
        // 클릭한 위도, 경도 정보를 가져옵니다 
        let latlng = mouseEvent.latLng; 
        
        // 마커 위치를 클릭한 위치로 옮깁니다
        marker.setPosition(latlng);
        
        
        courseLat.value = latlng.getLat()
        courseLng.value = latlng.getLng()
        
    });
})