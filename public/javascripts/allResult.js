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
    var expType = expName;
    document.getElementById("expName").innerHTML = expName +' <span class="caret"></span>';
    document.getElementById("expNameHeader").innerHTML = expName;
    //document.getElementById("overlayHeader1").innerHTML = expName;

 } 
$('#myTab a').click(function (e) {
  e.preventDefault()
  $(this).tab('show')
})
