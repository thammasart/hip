var t = 0;
var myVar = setInterval(function() {timer()},1000);


function timer(){
	t = t+1;
	if (t==5){
		document.getElementById("word1").innerHTML="";
		document.getElementById("word2").innerHTML="";
		document.getElementById("word3").innerHTML="";
		//document.getElementById("hopRabbit").animation = 0;
		document.getElementById("counter").innerHTML=1000;
	}
	if (t==10){
		
		document.getElementById("counter").innerHTML="";
		document.getElementById("hopRabbit").style.visibility = "visible";
		//document.getElementById("hopRabbit").animation = 0;
	}
	if (t==15){
		document.getElementById("hopRabbit").style.visibility = "hidden";
		document.getElementById("text1").disabled = false;
		document.getElementById("text2").disabled = false;
		document.getElementById("text3").disabled = false;
		document.getElementById("submitButton").disabled = false;
	}
	if (t==20){
		document.getElementById("text1").disabled = true;
		document.getElementById("text2").disabled = true;
		document.getElementById("text3").disabled = true;
		document.getElementById("submitButton").disabled = true;
	}
}
