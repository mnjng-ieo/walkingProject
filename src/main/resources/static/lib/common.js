document.addEventListener("DOMContentLoaded", function () {
   let searchBtn = document.getElementById('headerSearchBtn')
   let searchBox = document.getElementById('searchBox')
   
   searchBtn.addEventListener("click", function () {
      //alert('돋보기 클릭');
      if (searchBox.style.height == "70px") {
           // 콜랩스를 숨김
           //alert('searchBox 숨김')
           searchBox.style.height = "0px";
      } else {
         // 콜랩스를 보이게 함
           //alert('searchBox 보이게 함');
           searchBox.style.height = "70px";
      }
   });
});