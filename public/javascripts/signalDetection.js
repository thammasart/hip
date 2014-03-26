var startTimer = setInterval(function() { showInstruction()},2000);
var instructionTime = 5000;
var count = 5;
var showInstructionTime = 0;
var showTimerTime = 0;
var attentionWordTime = 0;
var questionTime = 0;
var questionTime2 = 0;
var expDuration = 0;
var question = "";
var target;
var noise;
var textLength;
var noOfTarget;

function showInstruction (){

    target = document.getElementById("target").innerHTML;
    noise = document.getElementById("noise").innerHTML;
    textLength = document.getElementById("length").innerHTML;
    noOfTarget = document.getElementById("noOfTarget").innerHTML;
    clearInterval(startTimer);
    generateQuestion();
    document.getElementById("word").innerHTML=" เป้าหมายคือ " + target;
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
    document.getElementById("word").innerHTML = " มี "+ target +" อยู่หรือไม่ " ;
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
     if ( (noOfTarget > 0 && clicked_name == "true") || (noOfTarget == 0 && clicked_name == "false") )
        document.getElementById("isCorrect").checked = true;
     else
        document.getElementById("isCorrect").checked = false;
}
function calculateTimeLeft(){
    count=count-1;
    document.getElementById("word").innerHTML= " โจทย์จะเริ่มในอีก " + (count-1) +" วินาที " ;
}
function generateQuestion(){
    var targetChar = new Array();

    for (var i = 0;i<noOfTarget;i++){
        var isUnique = false;
        var randIndex =1;
        do{
            isUnique = true;
            randIndex = Math.floor(Math.random()*textLength);
            for (var j = 0;j<targetChar.length;j++)
                if (randIndex == targetChar[j])
                    isUnique = false;
        }while(!isUnique);
        targetChar[i] = randIndex;
    }

    for (var i = 0;i<textLength;i++){
        var isTarget = false;
        for (var j = 0;j<noOfTarget;j++)
            if (targetChar[j] == i)
                isTarget = true;
        if (isTarget)
            question += target;
        else
            question += noise;
    }
}