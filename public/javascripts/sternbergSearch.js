var startTimer = setInterval(function() { showInstruction()},10);
var instructionTime = 5000;
var count = 5;
var blankTime = 0;
var showInstructionTime = 0;
var showTimerTime = 0;
var attentionWordTime = 0;
var questionTime = 0;
var i =0;
var question_memorySet;
var quiz_questionChar;
var quiz_isTrue;
var trial_blinkTime;
var isShowQuestion;
var expDuration;
var question = new Array();

function genQuestion(){
    for (var j=0;j<question_memorySet.length;j++){
        question[j] = question_memorySet.charAt(j);
    }
}

function showInstruction (){
    clearInterval(startTimer);
    question_memorySet = document.getElementById("memorySet").innerHTML;
    quiz_questionChar = document.getElementById("questionChar").innerHTML;
    quiz_isTrue = document.getElementById("isTrue").innerHTML;
    trial_blinkTime = document.getElementById("blinkTime").innerHTML;
    isShowQuestion = document.getElementById("isShowQuestion").innerHTML;
    if (isShowQuestion == "false"){
        genQuestion();
        document.getElementById("word").innerHTML="ดูชุดตัวอักษรแล้วตอบคำถาม";
        showInstructionTime = setInterval(function(){showTimer()},5000);
    }
    else{
        showQuestionWord();
    }
}
function showTimer(){
    clearInterval(showInstructionTime);
    showTimerTime =   setInterval(calculateTimeLeft,1000);
    attentionWordTime = setInterval(function(){showAttentionWord()},5000);
}
function showAttentionWord(){
    clearInterval(attentionWordTime);
    clearInterval(showTimerTime);
    document.getElementById("word").innerHTML = "";
    blankTime = setInterval(showWord,500);
}
function showWord(){
    questionTime = setInterval(showQuestionList,trial_blinkTime*1000);
}
function showQuestionList(){
    if (i < question.length){
        document.getElementById("word").innerHTML= question[i];
        i++;
    }
    else{
        showQuestionWord();
    }
}
function showQuestionWord(){
    clearInterval(questionTime);
    document.getElementById("me").style.visibility = "visible";
    document.getElementById("maiMe").style.visibility = "visible";
    document.getElementById("word").innerHTML = "มี " + quiz_questionChar.charAt(0);
    for (var j = 1;j < quiz_questionChar.length;j++)
        document.getElementById("word").innerHTML += " และ " + quiz_questionChar.charAt(j);
    if (quiz_questionChar.length > 1)
        document.getElementById("word").innerHTML += " ติดกันหรือไม่";
    else
        document.getElementById("word").innerHTML += " หรือไม่";
    d = new Date();
    expDuration = d.getTime();
}

function submitButtonClick(clicked_name){
     d = new Date();
     expDuration = d.getTime()-expDuration;
     document.getElementById("usedTime").value = expDuration/1000;
     if (clicked_name == "me")
        document.getElementById("answer").checked= true;
     else
        document.getElementById("answer").checked = false;
     if ( (quiz_isTrue=="true" && clicked_name == "me") || (quiz_isTrue=="false" && clicked_name == "maiMe") )
        document.getElementById("isCorrect").checked = true;
     else
        document.getElementById("isCorrect").checked = false;
}

function calculateTimeLeft()
{
  count=count-1;
        document.getElementById("word").innerHTML= " โจทย์จะเริ่มในอีก " + (count-1) +" วินาที" ;
}
