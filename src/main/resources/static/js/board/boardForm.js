/**
 * 
 */
window.onload = function(){
	
	// 해시태그 처리 
	const boardContent = document.getElementById("boardContent")
	const tagList = document.getElementById("tagList")
	tagList.innerHTML = "";
	if(boardContent != ''){
		let str =  boardContent.value
		let arr = str.match(/(#[^\s#]+)/g)
		const set = new Set(arr)
		arr = [...set]
		let result = ""
		if(arr != null){
			arr.forEach(function(tag){
				result += " <span class='badge px-2 py-1 my-1 fs-6 rounded-5 tagItem'>" + tag + "</span> "
			})
			tagList.innerHTML = result
		}
	}
	boardContent.addEventListener('keyup', function(){
		let str =  boardContent.value
		let arr = str.match(/(#[^\s#]+)/g)
		const set = new Set(arr)
		arr = [...set]
		let result = ""
		if(arr != null){
			arr.forEach(function(tag){
				result += " <span class='badge px-2 py-1 my-1 fs-6 rounded-5 tagItem'>" + tag + "</span> "
			})
			tagList.innerHTML = result
		}
	})
	
	// 산책로 처리
	const signguCn = document.getElementById("signguCn")
	const wlkCoursFlagNm = document.getElementById("wlkCoursFlagNm")
	const wlkCoursNm = document.getElementById("wlkCoursNm")
	const selectedCourse = document.getElementById("selectedCourse")
	const aCourseId = document.getElementById("courseId")
	// 산책로이름 가져오기
	signguCn.addEventListener('change', function(){
		if(signguCn.value != null){
			let xhr = new XMLHttpRequest();
			xhr.onreadystatechange = function(){
				if(xhr.readyState === XMLHttpRequest.DONE){
					if(xhr.status === 200){
						let result = xhr.response
						let flagContext = '<option>경로 선택</option>';
						result.forEach(function(flagName){
								flagContext += `<option value="${flagName}">${flagName}</option>`
						})
						wlkCoursFlagNm.innerHTML = flagContext;
					} else {
						alert("Ajax 요청처리 에러!")
					}
				}
			}
			xhr.open("GET", `/api/courses/flagname?signguCn=${signguCn.value}`)
			xhr.responseType = "json"
			xhr.send()
		}
	})
	
	// 경로이름 가져오기
	wlkCoursFlagNm.addEventListener('change', function(){
		if(signguCn.value != null){
			let xhr = new XMLHttpRequest();
			xhr.onreadystatechange = function(){
				if(xhr.readyState === XMLHttpRequest.DONE){
					if(xhr.status === 200){
						let result = xhr.response
						let courseContext = '<option>산책로 선택</option>';
						result.forEach(function(course){
							courseContext += `<option value="${course.courseId}">${course.wlkCoursNm}</option>`
						})
						wlkCoursNm.innerHTML = courseContext;
					} else {
						alert("Ajax 요청처리 에러!")
					}
				}
			}
			xhr.open("GET", `/api/courses/coursename?wlkCoursFlagNm=${wlkCoursFlagNm.value}`)
			xhr.responseType = "json"
			xhr.send()
		}
	})
	
	// 산책로 선택결과 보여주기
	wlkCoursNm.addEventListener('change', function(){
		let flagOption = wlkCoursFlagNm.options[wlkCoursFlagNm.selectedIndex].innerHTML
		let courseOption = wlkCoursNm.options[wlkCoursNm.selectedIndex].innerHTML
		let selectedCourseCotent = `${flagOption} : ${courseOption}`
		selectedCourse.value = selectedCourseCotent;
		aCourseId.value = wlkCoursNm.options[wlkCoursNm.selectedIndex].value
		getCourseInfo()

	})
	
	let courseId
	let flagName
	let courseName
	let courseLat
	let courseLng
	let courseAddr
	let courseLength
	let courseTime
	
	
	let content
	let overlay
	
	// 지도 켜기
	let mapContainer = document.getElementById('map'), // 지도를 표시할 div 
    mapOption = { 
        center: new kakao.maps.LatLng(37.5656528, 126.977962), // 지도의 중심좌표
        level: 5 // 지도의 확대 레벨
    };
	let map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다
	let marker = new kakao.maps.Marker({
	    map: map, 
	    position: new kakao.maps.LatLng(37.5656528, 126.977962)
	});
	
	if(selectedCourse.value != '산책로를 선택해주세요'){
		getCourseInfo()
	}
	function getCourseInfo(){
		if(aCourseId.value != null){
			let xhr = new XMLHttpRequest();
			xhr.onreadystatechange = function(){
				if(xhr.readyState === XMLHttpRequest.DONE){
					if(xhr.status === 200){
						let result = xhr.response
						document.getElementById('map').style.display = 'block'
						moveMap(result)
					} else {
						alert("Ajax 요청처리 에러!")
					}
				}
			}
			xhr.open("GET", `/api/courses/${aCourseId.value}`)
			xhr.responseType = "json"
			xhr.send()
		}
			
	}
	function moveMap(result){
		map.relayout();
		courseId = result.courseId
		flagName = result.wlkCoursFlagNm
		courseName = result.wlkCoursNm
		courseLat = result.coursSpotLa
		courseLng = result.coursSpotLo
		courseAddr = result.signguCn
		courseLength = result.coursDetailLtCn
		courseTime = result.coursTimeCn
		// 지도 중심을 이동 시킵니다
	    map.setCenter(new kakao.maps.LatLng(parseFloat(courseLat)+0.003, courseLng));
		// 지도에 마커를 표시합니다 
	    marker.setPosition(new kakao.maps.LatLng(courseLat, courseLng))
		
		// 커스텀 오버레이에 표시할 컨텐츠 입니다
		// 커스텀 오버레이는 아래와 같이 사용자가 자유롭게 컨텐츠를 구성하고 이벤트를 제어할 수 있기 때문에
		// 별도의 이벤트 메소드를 제공하지 않습니다 
		content = `<div class="wrap fa-bounce">
						  <div class="info">
		                    <div class="title">
		                    	<i class="icon fa-solid fa-person-walking fa-lg fa-bounce"></i>
		                        ${flagName} 
		                        <div class="close" id="close" title="닫기"><i class="fa-solid fa-x"></i></div>
		                    </div>
		                    <div class="body">
		                        <div class="img">
		                            <img src="https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/thumnail.png" width="73" height="70">
		                       </div>
		                        <div class="desc">
		                            <div class="ellipsis">${courseName}</div>
		                            <div class="textsm ellipsis">${courseAddr}</div> 
		                            <div class="textsm ellipsis">${courseLength}km / ${courseTime}</div> 
		                            <div><a href="/course/${courseId}" target="_blank" class="link">
		                            <i class="fa-solid fa-arrow-up-right-from-square icon fa-xs"></i> 바로가기</a></div>
		                        </div>
		                    </div>
		                </div>  
		            </div>`;
		// 마커 위에 커스텀오버레이를 표시합니다
		// 마커를 중심으로 커스텀 오버레이를 표시하기위해 CSS를 이용해 위치를 설정했습니다
		overlay = new kakao.maps.CustomOverlay({
		    content: content,
		    map: map,
		    position: marker.getPosition()       
		});
		
		
		document.getElementById("close").addEventListener("click", closeOverlay);
		// 마커를 클릭했을 때 커스텀 오버레이를 표시합니다
		kakao.maps.event.addListener(marker, 'click', function() {
		    overlay.setMap(map);
		});
		
	}
	
	

	// 커스텀 오버레이를 닫기 위해 호출되는 함수입니다 
	function closeOverlay() {
	    overlay.setMap(null);     
	}
	
	
	
	
	// 생성처리
	document.getElementById("submitBtn").addEventListener("click",
		function(){
			fetch('/api'+window.location.pathname,{
				method: 'PUT',
				headers:{
					"Content-Type": "application/json"
				},
				body: JSON.stringify({
					boardType : document.getElementById("boardType").value,
					boardTitle : document.getElementById("boardTitle").value,
					boardContent : document.getElementById("boardContent").value,
					courseId : document.getElementById("courseId").value
				})
			})
			.then(()=>	{
				alert('등록/수정이 완료되었습니다.');
				location.replace('/board');
			})
	})

}