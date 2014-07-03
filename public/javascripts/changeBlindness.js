var startTimer = setInterval(function() { showInstruction()},2000);
var instructionTime = 5000;
var count = 5;
var showInstructionTime = 0;
var showTimerTime = 0;
var blankTime = 0;
var questionTime = 0;
var delayTime = 0;

var charTime = 0;
var gapTime = 0;

var i =0;
var expDuration;

var imgState = 2;

var quiz_displayTime;
var question_answerAreaWidth;
var question_answerAreaHeight;
var question_positionOfChangeX;
var question_positionOfChangeY;

function showInstruction (){
    clearInterval(startTimer);
    quiz_displayTime = document.getElementById("quiz_displayTime").innerHTML;
    question_answerAreaWidth = document.getElementById("question_answerAreaWidth").innerHTML;
    question_answerAreaHeight = document.getElementById("question_answerAreaHeight").innerHTML;
    question_positionOfChangeX = document.getElementById("question_positionOfChangeX").innerHTML;
    question_positionOfChangeY = document.getElementById("question_positionOfChangeY").innerHTML;
    document.getElementById("word").innerHTML=" ท่านมีเวลาในการมองหาจุดต่างในภาพโจทย์ " + quiz_displayTime + " วินาที";

    document.getElementById("ansDiv").style.width = question_answerAreaWidth+"%";
    document.getElementById("ansDiv").style.height = question_answerAreaHeight+"%";
    document.getElementById("ansDiv").style.top = question_positionOfChangeY+"%";
    document.getElementById("ansDiv").style.left = question_positionOfChangeX+"%";

    var imgHeight = document.getElementById("img1").naturalHeight;

    var multiplier = 400/imgHeight;

    var imgWidth = document.getElementById("img1").naturalWidth * multiplier;

    var rect = document.getElementById("changeBlindnessExp").getBoundingClientRect();

    var mainWidth = rect.right - rect.left;

    if (imgWidth > 1000){
        document.getElementById("imgDiv").style.width = "1000px";
        document.getElementById("img1").style.width = "1000px";
        document.getElementById("img2").style.width = "1000px";
        document.getElementById("imgGray").style.width = "1000px";
    }
    else{
        document.getElementById("imgDiv").style.width = imgWidth + "px";
        document.getElementById("img1").style.width = imgWidth + "px";
        document.getElementById("img2").style.width = imgWidth + "px";
        document.getElementById("imgGray").style.width = imgWidth + "px";
    }

    showInstructionTime = setInterval(function(){showTimer()},5000);
}

function showTimer(){
    clearInterval(showInstructionTime);
    document.getElementById("word").innerHTML="";
    document.getElementById("word").style.fontSize = "60px";
    showTimerTime =   setInterval(calculateTimeLeft,1000);
    delayTime =setInterval(function(){showBlankBefore()},5000);
}

function showBlankBefore(){
    clearInterval(delayTime);
    clearInterval(showTimerTime);
    document.getElementById("word").innerHTML= "";
    blankTime = setInterval(function(){showBlank()},500);
}

function showBlank(){
    clearInterval(blankTime);
    count = parseInt(quiz_displayTime);
    count = count+1;
    gapTime = setInterval(showImg,50);
}

function showImg(){
    clearInterval(gapTime);
    document.getElementById("timerWord").innerHTML= " เหลือเวลาอีก " + (count-1) +" วินาที";
    document.getElementById("imgDiv").style.visibility = "visible";
    document.getElementById("img1").style.visibility = "visible";
    document.getElementById("stopB").style.visibility = "visible";
    showTimerTime = setInterval(calculateTimeLeft2,1000);
}

function stopImg(){
    if (imgState <= 2){
        document.getElementById("img1").style.visibility = "visible";
        document.getElementById("img2").style.visibility = "hidden";
    }
    else{
        document.getElementById("img1").style.visibility = "hidden";
        document.getElementById("img2").style.visibility = "visible";
    }

    document.getElementById("imgGray").style.visibility = "hidden";
    timeToAnswer();
}

function timeToAnswer(){
    clearInterval(showTimerTime);
    document.getElementById("stopB").style.visibility = "hidden";
    document.getElementById("ansDiv").style.visibility = "visible";
    document.getElementById("timerWord").innerHTML= " คลิกบนภาพ ตรงจุดที่ท่านเห็นว่ามีการเปลี่ยนแปลงเกิดขึ้น";
    count = -1;
    d = new Date();
    expDuration = d.getTime();
}

function done(subEvent){

    if (count <= 0){
        d = new Date();
        expDuration = d.getTime()-expDuration;
        document.getElementById("usedTime").value = expDuration/1000;

        var mainEvent = subEvent ? subEvent : window.event;

        var rect = document.getElementById("imgDiv").getBoundingClientRect();
        var x = mainEvent.clientX - rect.left;
        var y = mainEvent.clientY - rect.top;

        var width = rect.right - rect.left;
        var height = rect.bottom - rect.top;

        var percent_Of_x = (x/width) * 100;
        var percent_Of_y = (y/height) * 100;

        document.getElementById("positionOfChangeX").value = percent_Of_x;
        document.getElementById("positionOfChangeY").value = percent_Of_y;

        document.getElementById("isCorrect").checked = false;
        document.forms["answerForm"].submit();
    }
}

function doneCorrect(subEvent){

    if (count <= 0){
        d = new Date();
        expDuration = d.getTime()-expDuration;
        document.getElementById("usedTime").value = expDuration/1000;

        var mainEvent = subEvent ? subEvent : window.event;

        var rect = document.getElementById("imgDiv").getBoundingClientRect();
        var x = mainEvent.clientX - rect.left;
        var y = mainEvent.clientY - rect.top;

        var width = rect.right - rect.left;
        var height = rect.bottom - rect.top;

        var percent_Of_x = (x/width) * 100;
        var percent_Of_y = (y/height) * 100;

        document.getElementById("positionOfChangeX").value = percent_Of_x;
        document.getElementById("positionOfChangeY").value = percent_Of_y;

        document.getElementById("isCorrect").checked = true;
        document.forms["answerForm"].submit();
    }
}

function calculateTimeLeft(){
    count=count-1;
    document.getElementById("word").innerHTML= " โจทย์จะเริ่มในอีก " + (count-1) +" วินาที" ;
}

function calculateTimeLeft2(){
    count=count-1;
    if (count -1 > 0){
        document.getElementById("timerWord").innerHTML= " เหลือเวลาอีก " + (count-1) +" วินาที";
        if (imgState == 1){
            document.getElementById("img1").style.visibility = "visible";
            document.getElementById("img2").style.visibility = "hidden";
            document.getElementById("imgGray").style.visibility = "hidden";
            imgState++;
        }
        else if (imgState == 2 || imgState == 4){
            document.getElementById("img1").style.visibility = "hidden";
            document.getElementById("img2").style.visibility = "hidden";
            document.getElementById("imgGray").style.visibility = "visible";
            if (imgState == 2)
                imgState = 3;
            else
                imgState = 1;
        }
        else if (imgState == 3){
            document.getElementById("img1").style.visibility = "hidden";
            document.getElementById("img2").style.visibility = "visible";
            document.getElementById("imgGray").style.visibility = "hidden";
            imgState++;
        }
    }
    else{
        if (imgState <= 2){
            document.getElementById("img1").style.visibility = "visible";
            document.getElementById("img2").style.visibility = "hidden";
        }
        else{
            document.getElementById("img1").style.visibility = "hidden";
            document.getElementById("img2").style.visibility = "visible";
        }
        document.getElementById("imgGray").style.visibility = "hidden";
        timeToAnswer();
    }
}
