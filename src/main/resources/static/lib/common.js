// 각 상세페이지로 경로 넘어가는 클릭 이벤트
function redirectToCourseDetails(courseId){
    let url = "/course/" + courseId;
    window.location.href = url;
}