var timer;


$(document).ready(function() {
$('.submitButton').click(function() {
  $(this).parent().toggleClass('animation'); 
  timer = setInterval(function(){submitFunc()},500);
});
});

function submitFunc(){
    clearInterval(timer);
    document.forms["userForm"].submit(); 
}
