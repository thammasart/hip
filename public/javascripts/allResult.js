var expType;
 function getExpType() { 
    return expType;
 } 
 function overlayTrial() { 
    document.getElementById("overLay").style.visibility = "visible";  
 } 
 function closeOverlay() { 
    document.getElementById("overLay").style.visibility = "hidden";  
 } 

 function setGraphScore(gNumber,score,maxScore,time) { 
    if(gNumber==1){ 
        document.getElementById("sBeam1").style.height = (score/maxScore)*100+"%";
    }else if(gNumber==2){ 
        document.getElementById("sBeam2").style.height = (score/maxScore)*100+"%";
    }else if(gNumber==3){ 
        document.getElementById("sBeam3").style.height = (score/maxScore)*100+"%";
    }else{ 
        document.getElementById("sBeam4").style.height = (score/maxScore)*100+"%";
    }
 } 

 function setGraphTime(b1,b2,b3,b4) { 
    var max =-99;
    var time = [b1,b2,b3,b4];

    for(var iTime = 0; iTime < time.length;iTime++){ 
        if(time[iTime]>max)
            max = time[iTime];
    }
        document.getElementById("tBeam1").style.height = (b1/max)*100+"%";
        document.getElementById("tBeam2").style.height = (b2/max)*100+"%";
        document.getElementById("tBeam3").style.height = (b3/max)*100+"%";
        document.getElementById("tBeam4").style.height = (b4/max)*100+"%";

        document.getElementById("line2").innerHTML = max*0.2+"sec";
        document.getElementById("line3").innerHTML = max*0.4+"sec";
        document.getElementById("line4").innerHTML = max*0.6+"sec";
        document.getElementById("line5").innerHTML = max*0.8+"sec";
        document.getElementById("line6").innerHTML = max+"sec";
 } 
 function selectExperiment(expName) { 
    expType = expName;
    document.getElementById("expName").innerHTML = expName +' <span class="caret"></span>';
    document.getElementById("overlayHeader").innerHTML = expName;
    
    for (var i=0;i<document.getElementsByName('gearButton').length;i++){
        document.getElementsByName('gearButton')[i].disabled=false;
        document.getElementsByName('gearButton')[i].style.backgroundColor = "white";
    }
        if(expName=="Attentional Blink"){ 
            document.getElementById("tab1").style.display = "block";
            document.getElementById("tab2").style.display = "block";
            document.getElementById("tab3").style.display = "none";
            document.getElementById("tab4").style.display = "none";

            document.getElementById("tab1").innerHTML = "Thai";
            document.getElementById("tab2").innerHTML = "English";
        }
        else if(expName=="Simon Effect"){ 
            document.getElementById("tab1").style.display = "block";
            document.getElementById("tab2").style.display = "block";
            document.getElementById("tab3").style.display = "none";
            document.getElementById("tab4").style.display = "none";

            document.getElementById("tab1").innerHTML = "1-Feature";
            document.getElementById("tab2").innerHTML = "2-Feature";
        }
        else if(expName=="Stroop Effect"){ 
            document.getElementById("tab1").style.display = "block";
            document.getElementById("tab2").style.display = "block";
            document.getElementById("tab3").style.display = "block";
            document.getElementById("tab4").style.display = "block";

            document.getElementById("tab1").innerHTML = "Thai Match";
            document.getElementById("tab2").innerHTML = "English Match";
            document.getElementById("tab3").innerHTML = "Thai not Match";
            document.getElementById("tab4").innerHTML = "English not Match";
        }
        else if(expName=="Visual Search"){ 
            document.getElementById("tab1").style.display = "block";
            document.getElementById("tab2").style.display = "block";
            document.getElementById("tab3").style.display = "none";
            document.getElementById("tab4").style.display = "none";

            document.getElementById("tab1").innerHTML = "Experiment 1";
            document.getElementById("tab2").innerHTML = "Experiment 2";
        }
        else if(expName=="Brown-Peterson Task"){ 
            document.getElementById("tab1").style.display = "block";
            document.getElementById("tab2").style.display = "block";
            document.getElementById("tab3").style.display = "block";
            document.getElementById("tab4").style.display = "block";

            document.getElementById("tab1").innerHTML = "Thai Non-Sense";
            document.getElementById("tab2").innerHTML = "English Non-Sense";
            document.getElementById("tab3").innerHTML = "Thai Word";
            document.getElementById("tab4").innerHTML = "English Word";
        }
        else if(expName=="Magic Number 7" || expName=="Position Error"|| expName=="signal Detection"
              || expName=="Sternberg Search (Exhaustive)"|| expName=="Sternberg Search (Parallel)"){ 
            document.getElementById("tab1").style.display = "block";
            document.getElementById("tab2").style.display = "block";
            document.getElementById("tab3").style.display = "block";
            document.getElementById("tab4").style.display = "none";

            document.getElementById("tab1").innerHTML = "Thai";
            document.getElementById("tab2").innerHTML = "English";
            document.getElementById("tab3").innerHTML = "Number";
        }
        else if(expName=="garner Interference"){ 
            document.getElementById("tab1").style.display = "block";
            document.getElementById("tab2").style.display = "block";
            document.getElementById("tab3").style.display = "none";
            document.getElementById("tab4").style.display = "none";

            document.getElementById("tab1").innerHTML = "Unidimension";
            document.getElementById("tab2").innerHTML = "Bidimension";
        }
        else if(expName=="Muller-Lyer-Illusion"){ 
            document.getElementById("tab1").style.display = "block";
            document.getElementById("tab2").style.display = "block";
            document.getElementById("tab3").style.display = "block";
            document.getElementById("tab4").style.display = "none";

            document.getElementById("tab1").innerHTML = "Short Lines";
            document.getElementById("tab2").innerHTML = "Medium Lines";
            document.getElementById("tab3").innerHTML = "Long Lines";
        }
 } 
$('#myTab a').click(function (e) {
  e.preventDefault()
  $(this).tab('show')
})
