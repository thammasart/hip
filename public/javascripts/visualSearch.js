var startTimer = setInterval(function() { showInstruction()},2000);
var instructionTime = 5000;
var count = 5;
var showInstructionTime = 0;
var showTimerTime = 0;
var visualImageTime = 0;
var delayTime = 0;
var questionTime = 0;
var i =0;
var question_set;
var question_letter;
var question_correctAnswer;
var trial_blinkTime;
var expDuration = 0;
var question = new Array();

function showInstruction (){
    clearInterval(startTimer);
    document.getElementById("word").innerHTML = "เป้าหมายคือ";
    document.getElementById("circleEx").style.visibility = "visible";
    showInstructionTime = setInterval(function(){showTimer()},5000);
}
function showTimer(){
    clearInterval(showInstructionTime);
    showTimerTime = setInterval(calculateTimeLeft,1000);
    delayTime = setInterval(function(){showBlank()},5000);
}
function showBlank(){
    clearInterval(delayTime);
    clearInterval(showTimerTime);
    document.getElementById("word").innerHTML= "";
    visualImageTime = setInterval(function(){showVisualImage()},500);
}

function showVisualImage(){
    clearInterval(questionTime);
    clearInterval(visualImageTime);
    document.getElementById("visualArea").style.visibility = "visible";
    d = new Date();
    expDuration = d.getTime();
}

function shapeClick(shapeClick){
    d = new Date();
    expDuration = d.getTime()-expDuration;
    document.getElementById("usedTime").value = expDuration/1000;

    document.getElementById("positionX").value = parseInt(shapeClick.style.left);
    document.getElementById("positionY").value = parseInt(shapeClick.style.top);

    if (shapeClick.name == "target")
        document.getElementById("isCorrect").checked = true;
    else
       document.getElementById("isCorrect").checked = false;

}

function calculateTimeLeft()
{
    document.getElementById("circleEx").style.visibility = "hidden";
    document.getElementById("word").style.width = "100%";
    document.getElementById("word").style.left = "-17%";
    count=count-1;
    document.getElementById("word").innerHTML= " โจทย์จะเริ่มในอีก " + (count-1) +" วินาที" ;
}
