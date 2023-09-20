window.onload = function() {
    
 // * 수정 버튼 클릭 시, 
 //   수정과 삭제 버튼 사라지게 하고, 등록 버튼 나타나도록.
 
    // JQuery 버튼 이벤트 -> display: none - 안보임 | display: block - 보임
    // 내소개 수정 버튼을 눌렀을 때 - 수정폼 태그만 남는다.
    $("#modifyBtn").on("click",
    function(){
        $("#getUserBox").css('display', 'none');
        $("#modifyUserBox").css('display', 'block');
    })
    
    // 수정완료 버튼을 눌렀을 때 - 조회 태그만 남는다.
    $("#modifySuccessBtn").on("click",
    function(){
        $("#modifyUserBox").css('display', 'none');
        $("#getUserBox").css('display', 'block');
    })
    
    // 이미지가 '/images/defaultUserImage.png'인 경우 uploadBtn이, 아니면 deleteBtn 뜨도록 
    const userProfileImage = document.getElementById('user-imgBox_image');
    const imageUploadBtn = document.querySelector('.imageUploadButton');
    const imageDeleteBtn = document.querySelector('.imageDeleteButton');
    if(userProfileImage.src.endsWith('/images/defaultUserImage.png')){
        imageUploadBtn.style.display = 'block';
        imageDeleteBtn.style.display = 'none';
    } else {
        imageUploadBtn.style.display = 'none';
        imageDeleteBtn.style.display = 'block';
    }
    
}

// 전역 범수 선언할 때 주의! 여러 JS파일 간에도 공유 가능하기 때문에 이름을 명확히 한다.
let userFile; 

// 이미지 업로드 기능 : 뷰에서 img의 src 속성 바꾸기
function uploadImage() {
    const imageUploadInput = document.getElementById('imageUploadInput');
    // input 내용에 변화가 생기면, courseMainImage 요소의 src 속성 변경시키기
    // 사용자가 input 요소에서 파일을 선택하거나 변경할 때 발생
    imageUploadInput.addEventListener('change', function() {
        // 선택된 파일을 가져와 file 변수에 저장 (this = imageUploadInput)
        userFile = this.files[0];
        
        const ifNewUserImageExists = document.getElementById('ifNewImageExists');
        ifNewUserImageExists.value = '1';
        
        // file이 선택되었을 때
        if (userFile) {
            // 서버로 파일 업로드 요청을 보내는 코드 작성
            // 파일 업로드 후, 이미지 경로를 받아와서 이미지를 변경
            const reader = new FileReader();   // 파일을 읽기 위한 객체 생성
            reader.onload = function(e) {      // 파일 읽기 완료되면 호출
                const userProfileImage = document.getElementById('user-imgBox_image');
                userProfileImage.src = e.target.result;  // 읽은 파일의 데이터
            };
            reader.readAsDataURL(userFile);
            
            // uploadBtn은 사라지고 deleteBtn이 뜨도록 
            const imageUploadBtn = document.querySelector('.imageUploadButton');
            const imageDeleteBtn = document.querySelector('.imageDeleteButton');
            imageUploadBtn.style.display = 'none';
            imageDeleteBtn.style.display = 'block';
            
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
    const userProfileImage = document.getElementById('user-imgBox_image');
    userProfileImage.src = '/images/defaultUserImage.png';
    
    const ifNewUserImageExists = document.getElementById('ifNewImageExists');
    ifNewUserImageExists.value = '0';
    
    // file 변수를 초기화(삭제)
    userFile = null;
    
    // uploadBtn은 나타나고 deleteBtn이 사라지도록
    const imageUploadBtn = document.querySelector('.imageUploadButton');
    const imageDeleteBtn = document.querySelector('.imageDeleteButton');
    imageUploadBtn.style.display = 'block';
    imageDeleteBtn.style.display = 'none';
}

/////////////////////////////////////////////////////////////
$(document).ready(function () {
    // 첫 페이지에서 게시물만 보이게
    $('#postContainer').collapse('hide');
    $('#imgContainer').collapse('show');
    
    // imgBtn 버튼을 클릭할 때
    $('#imgBtn').on('click', function () {
        // 열려있는지 확인
        if (!$('#imgContainer').hasClass('show')) {
            $('#postContainer').collapse('hide');
            $('#imgContainer').collapse('show');
        }
        
    });
    
    // postBtn 버튼을 클릭할 때
    $('#postBtn').on('click', function () {
        // id=imgContainer가 열려 있다면 닫기

        // id=postContainer가 닫혀 있다면 열기
        if (!$('#postContainer').hasClass('show')) {
            $('#postContainer').collapse('show');
            $('#imgContainer').collapse('hide');
        }
    });
});

    
// 내가 쓴 게시글 클릭하여 게시물 상세보기
function redirectToDetail(row) {
    let url = row.getAttribute("data-url");
            
    // 페이지 이동
    window.location.href = url;
}

// 페이지를 로드할 때 초기 데이터를 불러온다.
function loadPage(pageNumber) {
    let xhr = new XMLHttpRequest();

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) { // 요청이 완료됨
            if (xhr.status === 200) { // 요청이 성공적으로 처리됨
            
            let response = xhr.responseText;
            // response를 파싱해서 searchResults div 내용만 추출
            let parser = new DOMParser();
            let doc = parser.parseFromString(response, 'text/html');
            let searchResultsContent = 
                    doc.querySelector('#needToChange').innerHTML;
                    
            // searchResults div 내용 교체
            document.getElementById("needToChange").innerHTML = searchResultsContent;                      

            } else {
                // 요청이 실패한 경우의 처리
                console.log('Error:', xhr.status, xhr.statusText);
            }
        }
    };

    // GET 요청을 보냄
    xhr.open('GET', '/mypage?page=' + pageNumber, true);
    xhr.send();
}