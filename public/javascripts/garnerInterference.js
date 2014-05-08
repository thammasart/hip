var startTimer = setInterval(function() {showSquare()},2000);
var showTimerTime = 0;
var showTimerTime2 = 0;
var count = 5;
var delayTime = 0;
var delayTime2 = 0;
var expDuration = 0;
var d = 0;
var score = 0;

function showSquare(){
    clearInterval(startTimer);
    document.getElementById("mc").style.visibility = "visible";  
    document.getElementById("ic").style.visibility = "visible";  
    document.getElementById("ic2").style.visibility = "visible";  
}

function showTimer(){
    document.getElementById("mc").style.visibility = "hidden";
    document.getElementById("mc").style.display= "none";
    document.getElementById("ic").style.visibility = "hidden";
    document.getElementById("ic2").style.visibility = "hidden";
    showTimerTime =   setInterval(calculateTimeLeft,1000);
    delayTime = setInterval(function(){delay()},5000);
}

function calculateTimeLeft()
{
    count=count-1;
    document.getElementById("word").innerHTML= " โจทย์จะเริ่มในอีก " + (count-1) +" วินาที" ;
}
function delay(){
    clearInterval(delayTime);
    clearInterval(showTimerTime);
    document.getElementById("word").innerHTML="";
    document.getElementById("mc").style.display= "table";
    delayTime2 = setInterval(function(){showQuestion()},1000);
}
function showQuestion(){
    clearInterval(delayTime2);
    d = new Date();
    expDuration = d.getTime();
    document.getElementById("ic").style.visibility = "visible";
    document.getElementById("yes").style.visibility = "visible";
    document.getElementById("no").style.visibility = "visible";
    document.getElementById("sq").style.marginLeft = "-5%";
    document.getElementById("sq").style.visibility = "visible";
    document.getElementById("sqw").innerHTML = "";
    document.getElementById("q").innerHTML = " รูปนี้ใช่สี่เหลี่ยมสีเข้มขนาดเล็กหรือไม่ " ;
}

function done(name){
    d = new Date();
    expDuration = (d.getTime()-expDuration)/1000;

    if(name == "yes"){
        score = score+1;
    }
    document.getElementById("time").value = expDuration;
    document.getElementById("score").value = score;

}
