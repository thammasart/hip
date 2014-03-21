var startTimer = setInterval(function() { showInstruction()},2000);
var instructionTime = 5000;
var count = 5;
var showInstructionTime = 0;
var showTimerTime = 0;
var attentionWordTime = 0;
var questionTime = 0;
var questionTime2 = 0;
var i =0;
var question = new Array();
    question[0] = "D";        
    question[1] = "A";
    question[2] = "R";
    question[3] = "K";

function showInstruction (){
    clearInterval(startTimer);
    document.getElementById("word").innerHTML=" ค้นหา A และ B ที่อยู่ติดกัน ";
    showInstructionTime = setInterval(function(){showTimer()},5000);
}
function showTimer(){
    clearInterval(showInstructionTime);
    showTimerTime =   setInterval(calculateTimeLeft,1000);
    attentionWordTime = setInterval(function(){showAttentionWord()},5000);
}
function showAttentionWord(){
    clearInterval(attentionWordTime);
    clearInterval(showTimerTime);
    questionTime = setInterval(showQuestionList,500);
    questionTime2 = setInterval(function(){showQuestionWord()},2500);
}
function showQuestionList(){
    document.getElementById("word").innerHTML= question[i];
    i++;
}
function showQuestionWord(){
    clearInterval(questionTime2);
    clearInterval(questionTime);
    document.getElementById("word").innerHTML = " มี A และ B ติดกันหรือไม่ " ;
}
function calculateTimeLeft()
{
  count=count-1;
        document.getElementById("word").innerHTML= " โจทย์จะเริ่มในอีก " + (count-1) +" วินาท่ี " ;
}
