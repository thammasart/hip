var startTimer = setInterval(function() { showInstruction()},500);
var instructionTime = 5000;
var count = 5;
var showInstructionTime = 0;
var showTimerTime = 0;
var signalWordTime = 0;
var questionTime = 0;
var expDuration = 0;
var delayTime = 0;
var question = "";
var displayTime;
var isTimeToAnswer = false;

function showInstruction (){
    clearInterval(startTimer);
    showInstructionTime = setInterval(function(){showTimer()},1000);
}
function showTimer(){
    clearInterval(showInstructionTime);
    document.getElementById("question").style.width = "50%";
    document.getElementById("left").innerHTML = "O";
    document.getElementById("right").innerHTML = "?";
    showTimerTime =   setInterval(calculateTimeLeft,1000);
    delayTime = setInterval(function(){showBlank()},5000);
}

function showBlank(){
    clearInterval(delayTime);
    clearInterval(showTimerTime);
    document.getElementById("question").style.width = "40%";
    document.getElementById("word").innerHTML = "+";
    document.getElementById("word").style.fontSize = "100px";
    document.getElementById("top").innerHTML = "?";
    document.getElementById("bottom").innerHTML = "?";
    signalWordTime = setInterval(function(){showSignalWord()},1000);
}

function showSignalWord(){
    clearInterval(signalWordTime);
    clearInterval(showTimerTime);
    document.getElementById("left").style.visibility = "visible";
    document.getElementById("left").style.color = "green";
    questionTime = setInterval(function(){showQuestionWord()},500);
}
function showQuestionWord(){
    clearInterval(questionTime);
    document.getElementById("left").style.visibility = "hidden";
    d = new Date();
    expDuration = d.getTime();
    isTimeToAnswer = true;
}
function calculateAnswerTime(){
    d = new Date();
    expDuration = d.getTime()-expDuration;
    document.getElementById("usedTime").value = expDuration/1000;
}

document.onkeydown = checkKey;

function checkKey(e) {
    if (isTimeToAnswer == true){
        e = e || window.event;

        if (e.keyCode == '37') {
            isTimeToAnswer = false;
            // left arrow
            calculateAnswerTime();
            document.getElementById("isCorrect").value = 1;
            document.forms["answerForm"].submit();
        }
        else if (e.keyCode == '38') {
            isTimeToAnswer = false;
            // up arrow
            calculateAnswerTime();
            document.getElementById("isCorrect").value = 0;
            document.forms["answerForm"].submit();
        }
        else if (e.keyCode == '39') {
            isTimeToAnswer = false;
            // right arrow
            calculateAnswerTime();
            document.getElementById("isCorrect").value = 0;
            document.forms["answerForm"].submit();
        }
        else if (e.keyCode == '40') {
            isTimeToAnswer = false;
            // down arrow
            calculateAnswerTime();
            document.getElementById("isCorrect").value = 0;
            document.forms["answerForm"].submit();
        }
    }
}

function calculateTimeLeft(){
    count=count-1;
    document.getElementById("word").innerHTML= " โจทย์จะเริ่มในอีก " + (count-1) +" วินาที " ;
}
