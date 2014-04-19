var isSubmitABCD = false;
window.onload = function() {
	var hipForms = document.getElementsByTagName("form");
	if (hipForms.length > 0){
		hipForms[0].onsubmit = 
		function(){
		    if (!isSubmitABCD){
		        isSubmitABCD = true;
		        event.returnValue = true;
		    }
		    else{
		        event.returnValue = false;
		    }
		}
	}
}