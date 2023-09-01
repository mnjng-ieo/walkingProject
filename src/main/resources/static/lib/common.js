window.onload = function(){
/*	// generate random whilist items (for the demo)
	var randomStringsArr = Array.apply(null, Array(100)).map(function() {
	  return Array.apply(null, Array(~~(Math.random() * 10  + 3))).map(function() {
	    return String.fromCharCode(Math.random() * (123 - 97) + 97)
	  }).join('') + '@gmail.com'
	})
	
	var input = document.querySelector('.customLook'),
	    tagify = new Tagify(input, {
	      // email address validation (https://stackoverflow.com/a/46181/104380)
	      pattern: /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,
	      whitelist: randomStringsArr,
	      callbacks: {
	        "invalid": onInvalidTag
	      },
	      dropdown : {
	        position: 'text',
	        enabled: 1 // show suggestions dropdown after 1 typed character
	      }
	    }),
	    button = input.nextElementSibling;  // "add new tag" action-button
	
	button.addEventListener("click", onAddButtonClick)
	
	function onAddButtonClick(){
	    tagify.addEmptyTag()
	}
	
	function onInvalidTag(e){
	  console.log("invalid", e.detail)
	}*/
	
	// The DOM element you wish to replace with Tagify
	// var input = document.querySelector('input[name=basic]');
	
	// initialize Tagify on the above input node reference
	
	var tagify = new Tagify(input)
	
	tagify.on('add', function() {
  	console.log(tagify.value); // 입력된 태그 정보 객체
	})
	

	var input = document.querySelector('.customLook'),
    tagify = new Tagify(input),
    button = input.nextElementSibling;  // "add new tag" action-button

	button.addEventListener("click", onAddButtonClick)
	
	function onAddButtonClick(){
	    tagify.addEmptyTag()
	}

	let btn = document.getElementById("submit")
	btn.addEventListener('click', function(){
		console.log(tagify.value)	
/*	const formData = new FormData(document.getElementById("tag"));
		console.log(formData);*/
	})

}