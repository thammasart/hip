
 function overlayTrial() { 
    document.getElementById("overLay").style.visibility = "visible";  
 } 
 function closeOverlay() { 
    document.getElementById("overLay").style.visibility = "hidden";  
 } 

 function selectExperiment(expName) { 
    document.getElementById("expName").innerHTML = expName +' <span class="caret"></span>';
    document.getElementById("overlayHeader").innerHTML = expName;
    
    for (var i=0;i<document.getElementsByName('gearButton').length;i++){
        document.getElementsByName('gearButton')[i].disabled=false;
        document.getElementsByName('gearButton')[i].style.backgroundColor = "white";
    }
 } 
$('#myTab a').click(function (e) {
  e.preventDefault()
  $(this).tab('show')
})
