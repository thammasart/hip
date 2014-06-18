var startTimer = setInterval(function() {showSquare()},2000);
var showTimerTime = 0;
var showTimerTime2 = 0;
var count = 5;
var delayTime = 0;
var delayTime2 = 0;
var expDuration = 0;
var d = 0;
var score = 0;

 function getQuestionType() { 
    return document.getElementById("questionType").innerHTML.toString();
 } 
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

    delayTime2 = setInterval(function(){showQuestion()},5000);
}
function showQuestion(){
    clearInterval(delayTime2);

    var qColor =getQcolor();
    var qSize=getQsize() ;    
    d = new Date();
    expDuration = d.getTime();
    document.getElementById("ansBox").style.visibility = "hidden";
    document.getElementById("ansBox").style.display= "none";
    document.getElementById("ic").style.visibility = "visible";
    document.getElementById("ic").style.paddingTop= "3%";
    document.getElementById("yes").style.visibility = "visible";
    document.getElementById("no").style.visibility = "visible";

    if(getQuestionType()=="BOTH"){
        document.getElementById("ques").innerHTML = "ใช้สี่เหลี่ยม"+qColor+qSize+"หรือไม่";
    } else if (getQuestionType()=="COLOR"){
        document.getElementById("ques").innerHTML = "ใช้สี่เหลี่ยม"+qColor+"หรือไม่";
    } else if (getQuestionType()=="SIZE"){
        document.getElementById("ques").innerHTML = "ใช้สี่เหลี่ยม"+qSize+"หรือไม่";
    }
}

 function getQcolor() { 
    if(document.getElementById("colorQuestion").innerHTML=="light"){
        return "สีอ่อน";
    }else  return "สีเข้ม";
 } 
 function getQsize() { 
    if(document.getElementById("sizeQuestion").innerHTML=="big"){ 
        return "ขนาดใหญ่";
    } else return "ขนาดเล็ก"; 
 }
function done(name){
    d = new Date();
    expDuration = (d.getTime()-expDuration)/1000;
    document.getElementById("usedTime").value = expDuration;

    var colorM = document.getElementById("colorMatch").innerHTML;
    var sizeM = document.getElementById("sizeMatch").innerHTML;

    if(getQuestionType() == "COlOR"){
        if((colorM == "true" && name == "yes") || (colorM == "false" && name == "no")){
            document.getElementById("isCorrect").checked = true;
        } else document.getElementById("isCorrect").checked = false;

    }else if(getQuestionType() == "SIZE"){
        if((sizeM== "true" && name == "yes") || (sizeM== "false" && name == "no")){
            document.getElementById("isCorrect").checked = true;
        } else document.getElementById("isCorrect").checked = false;

    } else if (getQuestionType() == "BOTH"){
        if((name == "yes" && sizeM =="true" && colorM == "true") || (name == "no" && (sizeM == "false"||colorM == "false"))){
            document.getElementById("isCorrect").checked = true;
        } else document.getElementById("isCorrect").checked = false;
    } // end big if
    document.getElementById("score").value = score;
}
