window.onload = function() {
    
 // * 수정 버튼 클릭 시, 
 //   수정과 삭제 버튼 사라지게 하고, 등록 버튼 나타나도록.
 
    // JQuery 버튼 이벤트 -> display: none - 안보임 | display: block - 보임
    // 내소개 수정 버튼을 눌렀을 때 - 수정폼 태그만 남는다.
    $("#modifyBtn").on("click",
    function(){
        //$(this).parent().parent().parent().parent().parent().parent().css('display', 'none');
        //$(this).parent().parent().parent().parent().parent().parent().next().css('display', 'block');
    })
    
    // 수정완료 버튼을 눌렀을 때 - 조회 태그만 남는다.
    $("#modifySuccessBtn").on("click",
    function(){
        //$(this).parent().parent().parent().parent().parent().parent().css('display', 'none');
        //$(this).parent().parent().parent().parent().parent().parent().prev().css('display', 'block');
    })
    
    
    // 삭제 버튼을 눌렀을 때 
    //--- 이건 내가 작성한게 아니라! 수정 버튼 클릭할 때 이렇게 바꾸고 싶어서 잠깐 가져옴!
    $(".deletebtn").on("click",
    function(){
        let commentId = $(this).parent().parent().prev().children().children("#commentId").val()
        //alert(`삭제 버튼 클릭, commentId = ${commentId}`)
        fetch('/api/comment/'+commentId,{
            method: 'DELETE'
        })
        .then(()=>  {
            alert('삭제가 완료되었습니다.');
            location.replace(window.location.href);
        })
    })
}

let reader;

// 이미지 업로드 기능 : 뷰에서 img의 src 속성 바꾸기
function uploadImage() {
    const imageUploadInput = document.getElementById('imageUploadInput');
    // input 내용에 변화가 생기면, courseMainImage 요소의 src 속성 변경시키기
    // 사용자가 input 요소에서 파일을 선택하거나 변경할 때 발생
    imageUploadInput.addEventListener('change', function() {
        // 선택된 파일을 가져와 file 변수에 저장 (this = imageUploadInput)
        const file = this.files[0];
        // file이 선택되었을 때
        if (file) {
            // 서버로 파일 업로드 요청을 보내는 코드 작성
            // 파일 업로드 후, 이미지 경로를 받아와서 이미지를 변경
            reader = new FileReader();   // 파일을 읽기 위한 객체 생성
            reader.onload = function(e) {      // 파일 읽기 완료되면 호출
                const userProfileImage = document.getElementById('user-imgBox_image');
                userProfileImage.src = e.target.result;  // 읽은 파일의 데이터
            };
            reader.readAsDataURL(file);
        } else {
            // 파일이 선택되지 않고 기존 사진을 유지하는 방법을 적자!!!
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
    
    // 이미지 업로드 취소 시 reader.abort() 호출
    if(reader){
        reader.abort();
    }
}