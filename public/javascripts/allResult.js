
 function overlayTrial() { 
    document.getElementById("overLay").style.visibility = "visible";  
 } 
 function closeOverlay() { 
    document.getElementById("overLay").style.visibility = "hidden";  
 } 

$('#myTab a').click(function (e) {
  e.preventDefault()
  $(this).tab('show')
})
