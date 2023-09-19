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
		courseTime = result.coursTimeCn;

		let savedImageName = document.getElementById("savedImageName").value;
		let imagePath;
	    if (savedImageName) {
	        imagePath = '/upload-images/course/' + savedImageName;
	    } else {
	        imagePath = '/images/defaultCourseMainImg.jpg';
	    }
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
								    <img src="${imagePath}"
								        style="width:100%; height:100%;">
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
            
            // 이미지 업로드를 위한 FormData 객체 생성
            const formData = new FormData();
            const imageUploadInput = document.getElementById('imageUploadInput');
            const boardImage = document.getElementById('boardImage');
   
            // (최종 업로드 취소되지 않은) 새로 업로드된 파일이 있는지 확인
		    if (imageUploadInput.files.length === 0) {
				if( boardImage.src == '/images/board/defaultBoardImage.jpg'){
					// 이미지를 최종 선택하지 않은 경우(이미지 삭제)
					formData.append('ifNewImageExists', 0);					
				}else{
					// 이미지가 변경되지 않는 경우(이미지 유지)
					formData.append('ifNewImageExists', 2);					
				}
			} else {
				// 이미지를 최종 선택한 경우(이미지 변경)
				formData.append('ifNewImageExists', 1);
				formData.append('files', imageUploadInput.files[0]);
			}
            
            // board 데이터
            const dto = {
                boardType : document.getElementById("boardType").value,
                boardTitle : document.getElementById("boardTitle").value,
                boardContent : document.getElementById("boardContent").value,
                courseId : document.getElementById("courseId").value
            };
            // 문자열 데이터를 JSON으로 변환하여 FormData에 추가
            // - Blob : 데이터를 처리하는 객체
            formData.append('dto',
                            new Blob([JSON.stringify(dto)], 
                                     {type: "application/json"}));
			fetch('/api'+window.location.pathname,{
				method: 'PUT',
				body: formData,
			})
			.then((response)=>	{
				if(!response.ok) {
                    throw new Error('등록 오류 발생');
                }
                // 응답 헤더에서 boardId 값 가져오기
                boardId = response.headers.get('boardId');
                //console.log('boardId : ' + boardId);
                return response.json();
            })
			.then((data) =>	{
				alert('등록/수정이 완료되었습니다.');
				if(boardId) {
                    location.replace(`/board/${boardId}`);
                } else {
                    location.replace('/board');
                }
			})
			.catch(error => {
                console.error('등록 중 오류 발생 : ', error);
			})
	})

}


// 전역 범수 선언할 때 주의! 여러 JS파일 간에도 공유 가능하기 때문에 이름을 명확히 한다.
let boardFile; 

// 이미지 업로드 기능 : 뷰에서 img의 src 속성 바꾸기
function uploadImage() {
    //console.log('이미지 업로드 버튼이 눌러졌습니다.');
    let imageUploadInput = document.getElementById('imageUploadInput');
    // input 내용에 변화가 생기면, courseMainImage 요소의 src 속성 변경시키기
    // 사용자가 input 요소에서 파일을 선택하거나 변경할 때 발생
    imageUploadInput.addEventListener('change', function() {
        // 선택된 파일을 가져와 file 변수에 저장 (this = imageUploadInput)
        boardFile = this.files[0];
        
        // file이 선택되었을 때
        if (boardFile) {
            //console.log('File 객체 안에 파일이 들어갔습니다.');
            // 서버로 파일 업로드 요청을 보내는 코드 작성
            // 파일 업로드 후, 이미지 경로를 받아와서 이미지를 변경 (const였다가 수정)
            const reader = new FileReader();   // 파일을 읽기 위한 객체 생성
            reader.onload = function(e) {      // 파일 읽기 완료되면 호출
                const boardImage = document.getElementById('boardImage');
                boardImage.src = e.target.result;  // 읽은 파일의 데이터
            };
            reader.readAsDataURL(boardFile);
        } else {
            // 파일 선택이 취소되었을 경우 file을 어떻게 제거할까? - deleteImage()
        }
    });
    
    // input 요소 클릭하여 파일 선택 다이얼로그 열기 
    imageUploadInput.click(); // ---> 클릭한 것과 같은 효과!
}

// 이미지 업로드 취소 기능 ; 이미지를 기본 이미지로 변경
// 취소하면 다시 이미지를 등록해주세요와 기본이미지 두 개 보이기
function deleteImage() {
    //console.log('이미지 업로드 취소 버튼이 눌러졌습니다.');
    const boardImage = document.getElementById('boardImage');
    boardImage.src = '/images/board/defaultBoardImage.jpg';
    
    // file 변수를 초기화(삭제)
    boardFile = null;
    //console.log('File 객체를 초기화했습니다. null');
}