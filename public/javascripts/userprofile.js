var submitStatus = true;

function submitButtonClick(){
    
    submitStatus = true; 
    if(document.getElementById("newPassword").value == "" || 
       document.getElementById("newPassword").value.length < 4 ||
       document.getElementById("confirmPassword").value == "" || 
       document.getElementById("confirmPassword").value.length < 4){

       document.getElementById("passwordAlert").style.visibility = "visible";
       submitStatus = false;
    } else  document.getElementById("passwordAlert").style.visibility = "hidden";
  
    return submitStatus;
}
