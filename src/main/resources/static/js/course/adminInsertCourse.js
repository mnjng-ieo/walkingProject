window.addEventListener('load', function() {
    
    let courseLat = document.getElementById('coursSpotLa')
    let courseLng = document.getElementById('coursSpotLo')
    
    let mapContainer = document.getElementById('map'), // 지도를 표시할 div 
        mapOption = { 
            center: new kakao.maps.LatLng(37.5655445437, 126.9773396358), // 지도의 중심좌표
            level: 8 // 지도의 확대 레벨
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
        
        let latlng = mouseEvent.latLng; 
        
        // 마커 위치를 클릭한 위치로 옮깁니다
        marker.setPosition(latlng);
        
        courseLat.value = latlng.getLat()
        courseLng.value = latlng.getLng()
        
        let resultDiv = document.getElementById('clickLatlng'); 
    });   
})