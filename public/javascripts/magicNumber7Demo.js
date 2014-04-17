var startTimer = setInterval(function() { showInstruction()},2000);
var instructionTime = 5000;
var count = 5;
var showInstructionTime = 0;
var showTimerTime = 0;
var magicWordTime = 0;
var delayTime = 0;
var questionTime = 0;
var questionTime2 = 0;
var i =0;
var expDuration;
var ansZone;
var question = "ABOPQAF";

function showInstruction (){
    ansZone = document.querySelectorAll('#choice .inputMagic');
    clearInterval(startTimer);
    document.getElementById("word").innerHTML=" จำตัวอักษรและลำดับทั้งหมด";
    showInstructionTime = setInterval(function(){showTimer()},5000);
}
function showTimer(){
    clearInterval(showInstructionTime);
    showTimerTime =   setInterval(calculateTimeLeft,1000);
    delayTime = setInterval(function(){showBlank()},5000);
}
function showBlank(){
    clearInterval(showTimerTime);
    clearInterval(delayTime);
    document.getElementById("word").innerHTML = "";
    magicWordTime = setInterval(function(){showMagicWord()},500);
}
function showMagicWord(){
    clearInterval(magicWordTime);
    var word = question[0];
    for (var j=1;j<question.length;j++){
        word += " " + question.charAt(j);
    }
    document.getElementById("word").innerHTML = word;
    questionTime = setInterval(showQuestionWord,1000);
}
function showQuestionWord(){
    clearInterval(questionTime2);
    clearInterval(questionTime);
    d = new Date();
    expDuration = d.getTime();
    for (var j=0;j<ansZone.length;j++){
        ansZone[j].style.visibility = "visible";
    }
    document.getElementById("confirm").style.visibility = "visible";
    document.getElementById("word").innerHTML = " เติมตัวอักษรให้ถูกต้องตามลำดับ" ;
}
function done(){
    d = new Date();
    expDuration =  (d.getTime()-expDuration)/1000;
    var score = 0;
    for (var j=0;j<ansZone.length;j++){
        if (ansZone[j].value == question.charAt(j)){
            score += 1;
        }
    }
    document.getElementById("time").value = expDuration;
    document.getElementById("score").value = score ;
}
function calculateTimeLeft()
{
        count=count-1;
        document.getElementById("word").innerHTML= " โจทย์จะเริ่มในอีก " + (count-1) +" วินาที " ;
}
