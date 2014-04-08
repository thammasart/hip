var startTimer = setInterval(function() { showInstruction()},2000);
var instructionTime = 5000;
var count = 5;
var showInstructionTime = 0;
var showTimerTime = 0;
var attentionWordTime = 0;
var questionTime = 0;
var questionTime2 = 0;
var i =0;
var expDuration;
var score = 0;
var questionCount = 0;
var question = new Array();
    question[0] = "Q";
    question[1] = "W";
    question[2] = "E";
    question[3] = "R";
    question[4] = "T";
    question[5] = "Y";
    question[6] = "U";
    question[7] = "I";
    question[8] = "O";
    question[9] = "P";
    question[10] = "A";
    question[11] = "S";
    question[12] = "D";
    question[13] = "F";

function showInstruction (){
    clearInterval(startTimer);
    document.getElementById("word").innerHTML=" ดูชุดตัวอักษรแล้วตอบคำถาม";
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
    questionTime2 = setInterval(function(){showQuestionWord()},(question.length*500)+500);
}
function showQuestionList(){
    document.getElementById("word").innerHTML= question[i];
    i++;
}
function showQuestionWord(){
    clearInterval(questionTime2);
    clearInterval(questionTime);
    d = new Date();
    expDuration = d.getTime();
    document.getElementById("me").style.visibility = "visible";
    document.getElementById("maiMe").style.visibility = "visible";
    document.getElementById("word").innerHTML = " มี Q อยู่หรือไม่" ;
}
function done(name){
    if(name == "me"){
        score = score+1;
    }
    if (questionCount > 0){
        d = new Date();
        expDuration =  (d.getTime()-expDuration)/1000;
        document.getElementById("time").value = expDuration;
        document.getElementById("score").value = score;
        document.forms["answerForm"].submit();
    }
    else{
        document.getElementById("word").innerHTML = " มี A อยู่หรือไม่" ;
        questionCount++;
    }
}
function calculateTimeLeft()
{
  count=count-1;
        document.getElementById("word").innerHTML= " โจทย์จะเริ่มในอีก " + (count-1) +" วินาที " ;
}
