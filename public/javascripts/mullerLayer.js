var startTimer = setInterval(function() {showTimer()},2000);
var showTimerTime = 0;
var count = 5;
var delayTime = 0;
var delayTime2 = 0;
var expDuration = 0;
var d = 0;
var differentChoice ; 
var lenghtType ; 
var differentLenght ; 
var lineLength = "5%";
var score = 0 ;
var displayTime = 10;
var isDemo ;

function showTimer(){
    isDemo = document.getElementById("demo").value;
    clearInterval(startTimer);
    showTimerTime =   setInterval(calculateTimeLeft,1000);
    delayTime = setInterval(function(){delay()},5000);
}

function calculateTimeLeft()
{
    count=count-1;
    document.getElementById("word").innerHTML= " โจทย์จะเริ่มในอีก " + (count-1) +" วินาที" ;
}
function delay(){
    displayTime = parseInt(document.getElementById("displayTime").value)  ;
    clearInterval(delayTime);
    clearInterval(showTimerTime);
    document.getElementById("word").innerHTML= " มีเวลาทำการทดลอง "+displayTime+" วินาที" ;
    delayTime2 = setInterval(function(){showQuestion()},1000);
}

function showExpTime() { 
    displayTime--;
    document.getElementById("word").innerHTML= " เหลือเวลาอีก " + displayTime +" วินาที" ;
    if(displayTime == 0){
        if(isDemo =="real"){
            done(99);
        }else{
            doneDemo("f");
        }
        document.getElementById("answerForm").submit();
    }
} 

function showQuestion(){
    clearInterval(delayTime2);
    d = new Date();
    expDuration = d.getTime();
    expTime = setInterval(function(){showExpTime()},1000);
    document.getElementById("mc").style.visibility = "visible";  
    document.getElementById("ic").style.visibility = "visible";  
}
var expTime;
function done(choice){
    clearInterval(showExpTime);
    d = new Date();
    expDuration = (d.getTime()-expDuration)/1000;
    document.getElementById("usedTime").value = expDuration;
    document.getElementById("choice").value = choice;
}
function doneDemo(name){
    d = new Date();
    expDuration = (d.getTime()-expDuration)/1000;

    if(name == "t"){
        score = score+1;
    }
    document.getElementById("time").value = expDuration;
    document.getElementById("score").value = score;

}
//function setUp(differLenght,differChoice){
//    lenghtType = document.getElementById("lengthType").innerHTML.toString();
//   
//    if(lenghtType == 1){
//        lineLength = 30;
//    }
//    else if(lenghtType == 2){
//        lineLength = 50;        
//    }
//    else if(lenghtType == 3){
//        lineLength = 80;
//    }
//    var line = document.getElementsByClassName('arrowButton');
//    for(var k =0;k<line.length;k++){
//        line[k].style['width'] = lineLength + "%";
//    }
//    document.getElementById(differChoice-1).style.width = lineLength - differLenght+"%";
//
//    var ar = document.getElementsByClassName('lineArrow');
//    var arrowRight = document.getElementsByClassName('arrowPos');
//    for(var z =0;z<arrowRight.length;z++){
//        arrowRight[z].style.marginLeft =  ar[z].offsetWidth +"px";
//        if(hasClass(arrowRight[z],"aps")) {
//           arrowRight[z].style.marginLeft = ar[2].offsetWidth + 15 +"px";
//        }
//    }
//}
function hasClass(element, cls) {
    return (' ' + element.className + ' ').indexOf(' ' + cls + ' ') > -1;
}
