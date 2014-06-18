var questionTime = 5000;
var countDownTime = 5000;
var flashTime;
var answerTime = 5000;
var startTimer = setInterval(function() {startTimerFunction()},3000);
var questionTimer;
var flashTimer;
var answerTimer;
var finTimer;
var expDuration = 0;
var d;
var isStore = 0;

function startTimerFunction(){
        clearInterval(startTimer);
        questionTime = document.getElementById("appear_time").value;
        document.getElementById("word").style.visibility="visible";
        questionTimer = setInterval(function() {answerTimerFunction()},questionTime);
}

function answerTimerFunction(){
        clearInterval(questionTimer);
        document.getElementById("word").style.visibility = "hidden";
        document.getElementById("title").style.visibility="visible";
        document.getElementById("black").style.visibility="visible";
    	document.getElementById("red").style.visibility="visible";
    	document.getElementById("blue").style.visibility="visible";
    	document.getElementById("yellow").style.visibility="visible";
    	document.getElementById("green").style.visibility="visible";
    	document.getElementById("purple").style.visibility="visible";
    	finTimer = setInterval(function() {finishTimerFunction()},answerTime);
    	d = new Date();
    	expDuration = d.getTime();

}

function finishTimerFunction(){
        clearInterval(finTimer);
        d = new Date();
        expDuration = d.getTime()-expDuration;
        document.getElementById("usedTime").value = expDuration/1000;
        document.forms["answerForm"].submit();
}

function submitButtonClick(clicked_name){
     d = new Date();
     expDuration = d.getTime()-expDuration;
     document.getElementById("usedTime").value = expDuration/1000;
     document.getElementById("answer").value = clicked_name;
     if (clicked_name.toUpperCase() == document.getElementById("inkColor").innerHTML.toUpperCase()){
        document.getElementById("isCorrect").checked = true;
     }
     else{
        document.getElementById("isCorrect").checked = false;
     }
}
