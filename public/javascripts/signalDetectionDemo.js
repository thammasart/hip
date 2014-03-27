var startTimer = setInterval(function() { showInstruction()},2000);
var instructionTime = 5000;
var count = 5;
var showInstructionTime = 0;
var showTimerTime = 0;
var signalWordTime = 0;
var questionTime = 0;
var expDuration = 0;
var question = "XXXXXYXXXXXYXXXYXXXXXXXXX";

function showInstruction (){
    clearInterval(startTimer);
    document.getElementById("word").innerHTML=" เป้าหมายคือ Y ";
    showInstructionTime = setInterval(function(){showTimer()},5000);
}
function showTimer(){
    clearInterval(showInstructionTime);
    showTimerTime =   setInterval(calculateTimeLeft,1000);
    signalWordTime = setInterval(function(){showSignalWord()},5000);
}
function showSignalWord(){
    clearInterval(signalWordTime);
    clearInterval(showTimerTime);
    document.getElementById("word").style.fontSize = "30px";
    document.getElementById("word").innerHTML= question;
    questionTime = setInterval(function(){showQuestionWord()},5000);
}
function showQuestionList(){
    document.getElementById("word").innerHTML= question;
}
function showQuestionWord(){
    clearInterval(questionTime);
    document.getElementById("word").style.fontSize = "60px";
    document.getElementById("word").innerHTML = " มี Y อยู่หรือไม่ " ;
    document.getElementById("true").style.visibility = "visible";
    document.getElementById("false").style.visibility = "visible";
    d = new Date();
    expDuration = d.getTime();
}
function submitButtonClick(clicked_name){
     d = new Date();
     expDuration = d.getTime()-expDuration;
     document.getElementById("usedTime").value = expDuration/1000;
     if (clicked_name == "true")
        document.getElementById("answer").checked= true;
     else
        document.getElementById("answer").checked = false;
     if (clicked_name == "true")
        document.getElementById("isCorrect").checked = true;
     else
        document.getElementById("isCorrect").checked = false;
}
function calculateTimeLeft(){
    count=count-1;
    document.getElementById("word").innerHTML= " โจทย์จะเริ่มในอีก " + (count-1) +" วินาที " ;
}