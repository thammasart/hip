function validateNumberKey(evt) {

  var theEvent = evt || window.event;
  var key = theEvent.keyCode || theEvent.which;

    if(key == 37 || key == 38 || key == 39 || key == 40 || key == 8 || key == 46) { // Left / Up / Right / Down Arrow, Backspace, Delete keys
         return;
     }
    key = String.fromCharCode( key );

  var regex = /[0-9]\b/;
  if( !regex.test(key) ) {
    theEvent.returnValue = false;
    if(theEvent.preventDefault) theEvent.preventDefault();
  }
}
function goBack() {
    window.history.back()
}

function selParaName(name) { 
   document.getElementById("paraName").innerHTML = name;
} 

function selEx(expName) { 
   document.getElementById("expName").innerHTML = expName;
    document.getElementById("paraName").disabled = false;

        if(expName=="Attentional Blink"){ 
            document.getElementById("para1").style.display = "block";
            document.getElementById("para2").style.display = "block";
            document.getElementById("para3").style.display = "none";
            document.getElementById("para4").style.display = "none";

            document.getElementById("para1").innerHTML = "Thai";
            document.getElementById("para2").innerHTML = "English";
        }
        else if(expName=="Simon Effect"){ 
            document.getElementById("para1").style.display = "block";
            document.getElementById("para2").style.display = "block";
            document.getElementById("para3").style.display = "none";
            document.getElementById("para4").style.display = "none";

            document.getElementById("para1").innerHTML = "1-Feature";
            document.getElementById("para2").innerHTML = "2-Feature";
        }
        else if(expName=="Stroop Effect"){ 
            document.getElementById("para1").style.display = "block";
            document.getElementById("para2").style.display = "block";
            document.getElementById("para3").style.display = "block";
            document.getElementById("para4").style.display = "block";

            document.getElementById("para1").innerHTML = "Thai Match";
            document.getElementById("para2").innerHTML = "English Match";
            document.getElementById("para3").innerHTML = "Thai not Match";
            document.getElementById("para4").innerHTML = "English not Match";
        }
        else if(expName=="Visual Search"){ 
            document.getElementById("para1").style.display = "block";
            document.getElementById("para2").style.display = "block";
            document.getElementById("para3").style.display = "none";
            document.getElementById("para4").style.display = "none";

            document.getElementById("para1").innerHTML = "Experiment 1";
            document.getElementById("para2").innerHTML = "Experiment 2";
        }
        else if(expName=="Brown-Peterson Task"){ 
            document.getElementById("para1").style.display = "block";
            document.getElementById("para2").style.display = "block";
            document.getElementById("para3").style.display = "block";
            document.getElementById("para4").style.display = "block";

            document.getElementById("para1").innerHTML = "Thai Non-Sense";
            document.getElementById("para2").innerHTML = "English Non-Sense";
            document.getElementById("para3").innerHTML = "Thai Word";
            document.getElementById("para4").innerHTML = "English Word";
        }
        else if(expName=="Magic Number 7" || expName=="Position Error"|| expName=="signal Detection"
              || expName=="Sternberg Search (Exhaustive)"|| expName=="Sternberg Search (Parallel)"){ 
            document.getElementById("para1").style.display = "block";
            document.getElementById("para2").style.display = "block";
            document.getElementById("para3").style.display = "block";
            document.getElementById("para4").style.display = "none";

            document.getElementById("para1").innerHTML = "Thai";
            document.getElementById("para2").innerHTML = "English";
            document.getElementById("para3").innerHTML = "Number";
        }
        else if(expName=="garner Interference"){ 
            document.getElementById("para1").style.display = "block";
            document.getElementById("para2").style.display = "block";
            document.getElementById("para3").style.display = "none";
            document.getElementById("para4").style.display = "none";

            document.getElementById("para1").innerHTML = "Unidimension";
            document.getElementById("para2").innerHTML = "Bidimension";
        }
        else if(expName=="Muller-Lyer-Illusion"){ 
            document.getElementById("para1").style.display = "block";
            document.getElementById("para2").style.display = "block";
            document.getElementById("para3").style.display = "block";
            document.getElementById("para4").style.display = "none";

            document.getElementById("para1").innerHTML = "Short Lines";
            document.getElementById("para2").innerHTML = "Medium Lines";
            document.getElementById("para3").innerHTML = "Long Lines";
        }
} 
