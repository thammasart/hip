var submitStatus = true;

function validateSemester(evt) {

  if (event.keyCode == 13) {
        submitButtonClick();
        document.forms["answerForm"].submit();
    }

  var theEvent = evt || window.event;
  var key = theEvent.keyCode || theEvent.which;
  key = String.fromCharCode( key );
  var regex = /[0-9]/;
  if( !regex.test(key) ) {
    theEvent.returnValue = false;
    if(theEvent.preventDefault) theEvent.preventDefault();
  }
}
function submitButtonClick(){
    
    submitStatus = true;
    if(document.getElementById("firstnameInput").value ==""){
        document.getElementById("firstnameAlert").style.visibility = "visible";
        submitStatus = false;
    }else  document.getElementById("firstnameAlert").style.visibility = "hidden";

    if(document.getElementById("lastnameInput").value ==""){
        document.getElementById("lastnameAlert").style.visibility = "visible";
        submitStatus = false;
    }else  document.getElementById("lastnameAlert").style.visibility = "hidden";

    if(document.getElementById("sectionInput").value ==""){
        document.getElementById("sectionAlert").style.visibility = "visible";
        submitStatus = false;
    }else  document.getElementById("sectionAlert").style.visibility = "hidden";

    if(document.getElementById("semesterInput").value ==""){
        document.getElementById("semesterAlert").style.visibility = "visible";
        submitStatus = false;
    }else  document.getElementById("semesterAlert").style.visibility = "hidden";

    if(document.getElementById("yearInput").value ==""){
        document.getElementById("yearAlert").style.visibility = "visible";
        submitStatus = false;
    }else  document.getElementById("yearAlert").style.visibility = "hidden";

    if(document.getElementById("emailInput").value ==""){
        document.getElementById("emailAlert").style.visibility = "visible";
        submitStatus = false;
    }else  document.getElementById("emailAlert").style.visibility = "hidden";

    if(document.getElementById("facultyInput").value ==""){
        document.getElementById("facultyAlert").style.visibility = "visible";
        submitStatus = false;
    }else  document.getElementById("facultyAlert").style.visibility = "hidden";

    if(document.getElementById("departmentInput").value ==""){
        document.getElementById("departmentAlert").style.visibility = "visible";
        submitStatus = false;
    }else  document.getElementById("departmentAlert").style.visibility = "hidden";

    var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

    if(!(re.test(document.getElementById("emailInput").value))){
        document.getElementById("emailAlert").style.visibility = "visible";
        submitStatus = false;
    }else  document.getElementById("emailAlert").style.visibility = "hidden";

    if(document.getElementById("other").checked){
        document.getElementById("genderAlert").style.visibility = "visible";
        submitStatus = false;
    }
    document.getElementById("birthInputBox").disabled = false;
    return submitStatus;
}
