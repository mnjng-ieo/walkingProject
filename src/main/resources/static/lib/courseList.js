let courseSearchBtn = document.getElementById('courseSearchBtn');

function sendSelectedValue() {
    let region = document.getElementById('region').value;
    let level = document.getElementById('level').value;
    let distance = document.getElementById('distance').value;
    
    let time = document.getElementById('time').value;
    let startTime;
    let endTime;
    
    if(time == 1) {
        startTime = "00:00:00";
        endTime = "00:59:00";
    } else if (time == 2) {
        startTime = "01:00:00";
        endTime = "01:59:00";
    } else if (time == 3) {
        startTime = "02:00:00";
        endTime = "02:59:00";
    } else if (time == 4) {
        startTime = "03:00:00";
        endTime = "03:59:00";
    } else if (time == 5) {
        startTime = "04:00:00";
        endTime = "99:00:00";
    }
    
    let searchTargetAttr = document.getElementById('searchTargetAttr').value;
    let searchKeyword = document.getElementById('searchKeyword').value;
    let sort = document.getElementById('sort').value;
    let page = 0;
    
    // 검색 조건을 JSON 형식으로 만들기
    let searchConditions = {
        region: region,
        level: level,
        distance: distance,
        startTime: startTime,
        endTime: endTime,
        searchTargetAttr: searchTargetAttr,
        searchKeyword: searchKeyword,
        sort: sort,
        page: page
    };
    
    let jsonData = JSON.stringify(searchConditions);
    
    // 서버로 데이터 보내는 Ajax POST 요청 생성
    const xhr = new XMLHttpRequest();
    xhr.open('POST', '/courses', true); // POST 요청으로 변경

    xhr.setRequestHeader('Content-Type', 'application/json;charset=UTF-8');
    xhr.send(jsonData); // JSON 데이터를 Request Body에 넣음

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                console.log('성공적으로 전송되었습니다.');
                // 응답 데이터 처리
                const responseData = xhr.responseText;
                console.log('응답 데이터:', responseData);
            }
        }
    };
    
};

courseSearchBtn.addEventListener('click', sendSelectedValue);
