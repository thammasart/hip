var startTimer = setInterval(function() { showInstruction()},2000);
var instructionTime = 5000;
var count = 5;
var showInstructionTime = 0;
var showTimerTime = 0;
var attentionWordTime = 0;
var delayTime = 0;
var questionTime = 0;
var i =0;
var question_set;
var question_letter;
var question_correctAnswer;
var trial_blinkTime;
var expDuration;
var question = new Array();

function genQuestion(){
    for (var j=0;j<question_set.length;j++){
        question[j] = question_set.charAt(j);
    }
}

function showInstruction (){
    clearInterval(startTimer);
    question_set = document.getElementById("questionSet").innerHTML;
    question_letter = document.getElementById("questionLetter").innerHTML;
    question_correctAnswer = document.getElementById("correctAnswer").innerHTML;
    trial_blinkTime = document.getElementById("blinkTime").innerHTML;
    genQuestion();
    if (question_letter.length == 1)
        document.getElementById("word").innerHTML=" เห็น "+ question_letter.charAt(0) + " หรือไม่";
    else if (question_letter.length == 2)
        document.getElementById("word").innerHTML=" เห็น "+ question_letter.charAt(0) +" ปรากฏหลัง " + question_letter.charAt(1) + " หรือไม่";
    else if (question_letter.length == 3)
        document.getElementById("word").innerHTML=" เห็น "+ question_letter.charAt(0) +", " + question_letter.charAt(1)+ " หลัง " + question_letter.charAt(2) +" หรือไม่";
    showInstructionTime = setInterval(function(){showTimer()},5000);
}
function showTimer(){
    clearInterval(showInstructionTime);
    showTimerTime =   setInterval(calculateTimeLeft,1000);
    delayTime = setInterval(function(){showBlank()},5000);
}
function showBlank(){
    clearInterval(delayTime);
    clearInterval(showTimerTime);
    document.getElementById("word").innerHTML= "";
    attentionWordTime = setInterval(function(){showAttentionWord()},500);
}

function showAttentionWord(){
    clearInterval(attentionWordTime);
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
    if (question_letter.length == 1)
        document.getElementById("word").innerHTML=" มี "+ question_letter.charAt(0) + " อยู่หรือไม่";
    else if (question_letter.length == 2)
        document.getElementById("word").innerHTML=" มี "+ question_letter.charAt(0) +" และ " + question_letter.charAt(1) + " ติดกันหรือไม่";
    else if (question_letter.length == 3)
        document.getElementById("word").innerHTML=" มี "+ question_letter.charAt(0) +", " + question_letter.charAt(1) + "และ" + question_letter.charAt(2) +" ติดกันหรือไม่";

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
     if ( (question_correctAnswer=="true" && clicked_name == "me") || (question_correctAnswer=="false" && clicked_name == "maiMe") )
        document.getElementById("isCorrect").checked = true;
     else
        document.getElementById("isCorrect").checked = false;

}

function calculateTimeLeft()
{
  count=count-1;
        document.getElementById("word").innerHTML= " โจทย์จะเริ่มในอีก " + (count-1) +" วินาที" ;
}
