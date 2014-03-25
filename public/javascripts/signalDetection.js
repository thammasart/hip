var startTimer = setInterval(function() { showInstruction()},2000);
var instructionTime = 5000;
var count = 5;
var showInstructionTime = 0;
var showTimerTime = 0;
var attentionWordTime = 0;
var questionTime = 0;
var questionTime2 = 0;
var question = "XXXXXYXXXXXYYXXXXXXXXXXXXXXXXXXXXXXXXXYYYXXXXXX";

function showInstruction (){
    clearInterval(startTimer);
    document.getElementById("word").innerHTML=" เป้าหมายคือ Y ";
    showInstructionTime = setInterval(function(){showTimer()},5000);
}
function showTimer(){
    clearInterval(showInstructionTime);
    showTimerTime =   setInterval(calculateTimeLeft,1000);
    attentionWordTime = setInterval(function(){showSignalWord()},5000);
}
function showSignalWord(){
    clearInterval(attentionWordTime);
    clearInterval(showTimerTime);
    document.getElementById("word").style.fontSize = "30px";
    document.getElementById("word").innerHTML= question;
    questionTime2 = setInterval(function(){showQuestionWord()},5000);
}
function showQuestionList(){
    document.getElementById("word").innerHTML= question;
}
function showQuestionWord(){
    clearInterval(questionTime2);
    document.getElementById("word").style.fontSize = "60px";
    document.getElementById("word").innerHTML = " มี Y อยู่หรือไม่ " ;
}
function calculateTimeLeft(){
    count=count-1;
    document.getElementById("word").innerHTML= " โจทย์จะเริ่มในอีก " + (count-1) +" วินาที " ;
}
