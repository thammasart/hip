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
var question_color;
var question_character;
var question_direction;
var quiz_position;
var trial_blinkTime;
var isTimeToAnswer = false;

function showInstruction (){
    clearInterval(startTimer);
    question_color = document.getElementById("question_color").innerHTML;
    question_character = document.getElementById("question_character").innerHTML;
    question_direction = document.getElementById("question_direction").innerHTML;
    quiz_position = document.getElementById("quiz_position").innerHTML;
    trial_blinkTime = document.getElementById("trial_blinkTime").innerHTML;
    showInstructionTime = setInterval(function(){showTimer()},1000);
}
function showTimer(){
    clearInterval(showInstructionTime);
    showTimerTime = setInterval(calculateTimeLeft,1000);
    delayTime = setInterval(function(){showBlank()},5000);
}

function showBlank(){
    clearInterval(delayTime);
    clearInterval(showTimerTime);
    document.getElementById("word").innerHTML = "&nbsp";
    document.getElementById("center").style.visibility = "visible";
    signalWordTime = setInterval(function(){showSignalWord()},1000);
}

function showSignalWord(){
    clearInterval(signalWordTime);
    clearInterval(showTimerTime);
    document.getElementById(quiz_position).innerHTML = question_character;
    document.getElementById(quiz_position).style.color = question_color;
    document.getElementById(quiz_position).style.visibility = "visible";
    questionTime = setInterval(function(){showQuestionWord()},trial_blinkTime*1000);
}
function showQuestionWord(){
    clearInterval(questionTime);
    document.getElementById(quiz_position).style.visibility = "hidden";
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
        document.getElementById("answer").checked = false;
        if (e.keyCode == '37') {
            isTimeToAnswer = false;
            // left arrow
            calculateAnswerTime();
            if (question_direction == "left" && expDuration/1000 < 0.8)
                document.getElementById("isCorrect").checked = true;
            document.forms["answerForm"].submit();
        }
        else if (e.keyCode == '38') {
            isTimeToAnswer = false;
            // up arrow
            calculateAnswerTime();
            if (question_direction == "up" && expDuration/1000 < 0.8)
                document.getElementById("isCorrect").checked = true;
            document.forms["answerForm"].submit();
        }
        else if (e.keyCode == '39') {
            isTimeToAnswer = false;
            // right arrow
            calculateAnswerTime();
            if (question_direction == "right" && expDuration/1000 < 0.8)
                document.getElementById("isCorrect").checked = true;
            document.forms["answerForm"].submit();
        }
        else if (e.keyCode == '40') {
            isTimeToAnswer = false;
            // down arrow
            calculateAnswerTime();
            if (question_direction == "down" && expDuration/1000 < 0.8)
                document.getElementById("isCorrect").checked = true;
            document.forms["answerForm"].submit();
        }
    }
}

function calculateTimeLeft(){
    count=count-1;
    document.getElementById("word").innerHTML= " โจทย์จะเริ่มในอีก " + (count-1) +" วินาที " ;
}
