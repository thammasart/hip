var startTimer = setInterval(function() {showSquare()},2000);
var showTimerTime = 0;
var showTimerTime2 = 0;
var count = 5;
var delayTime = 0;
var delayTime2 = 0;
var expDuration = 0;
var d = 0;
var score = 0;

function setup(){

    var bigSQ = document.getElementsByClassName('bigSquare');
    var smallSQ = document.getElementsByClassName('smallSquare');
    var darkSQ = document.getElementsByClassName('dark');
    var lightSQ= document.getElementsByClassName('light');
    for(var k =0;k<bigSQ.length;k++){
        bigSQ[k].style['width'] = document.getElementById("lengthBigSquare").innerHTML+ "cm";
    }
    for(var j =0;j<smallSQ.length;j++){
        smallSQ[j].style['width'] = document.getElementById("lengthSmallSquare").innerHTML+ "cm";
    }
    for(var l = 0; l < darkSQ.length;l++){ 
        darkSQ[l].style['background-color'] = "#"+document.getElementById("colorDark").innerHTML;
    }    
    for(var m = 0; m < lightSQ.length;m++){ 
        lightSQ[m].style['background-color'] = "#"+document.getElementById("colorLight").innerHTML;
    } 
}

function showSquare(){
    clearInterval(startTimer);
    document.getElementById("mc").style.visibility = "visible";  
    document.getElementById("mc").style.display = "inherit";  
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
    document.getElementById("word").innerHTML= "";
    
    if(document.getElementById("sizePic").innerHTML == "small"){ 
        document.getElementById("ansSquare").style.width= document.getElementById("lengthSmallSquare").innerHTML+"cm";
        document.getElementById("ansBox").style.width= document.getElementById("lengthSmallSquare").innerHTML+"cm";
        document.getElementById("ansBox").style.height= document.getElementById("lengthSmallSquare").innerHTML+"cm";
    }   else{ 
        document.getElementById("ansSquare").style.width= document.getElementById("lengthBigSquare").innerHTML+"cm";
        document.getElementById("ansBox").style.width= document.getElementById("lengthBigSquare").innerHTML+"cm";
        document.getElementById("ansBox").style.height= document.getElementById("lengthBigSquare").innerHTML+"cm";
    }

    if(document.getElementById("colorPic").innerHTML == "dark"){ 
        document.getElementById("ansSquare").style.color= "#"+document.getElementById("colorDark").innerHTML; 
    }   else{ 
        document.getElementById("ansSquare").style.color= "#"+document.getElementById("colorLight").innerHTML; 
    }
    document.getElementById("ansSquare").style.display= "table";

    //delayTime2 = setInterval(function(){showQuestion()},5000);
}
function showQuestion(){
    clearInterval(delayTime2);
    d = new Date();
    expDuration = d.getTime();
    document.getElementById("ic").style.visibility = "visible";
    document.getElementById("yes").style.visibility = "visible";
    document.getElementById("no").style.visibility = "visible";
    document.getElementById("q").style.marginTop = "10%";
    document.getElementById("q").innerHTML = " ใช่สี่เหลี่ยมสีเข้มขนาดเล็กหรือไม่ " ;
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
