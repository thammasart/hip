
$(document).ready(function(){
  $("#slide01").click(function(){
    $("#panel02").slideUp("fast");
	$("#panel03").slideUp("fast");
	$("#panel01").slideToggle("fast");
  });
});

$(document).ready(function(){
  $("#slide02").click(function(){
	$("#panel01").slideUp("fast");
	$("#panel03").slideUp("fast");
    $("#panel02").slideToggle("fast");
  });
});

$(document).ready(function(){
  $("#slide03").click(function(){
	$("#panel01").slideUp("fast");
	$("#panel02").slideUp("fast");
    $("#panel03").slideToggle("fast");
  });
});
