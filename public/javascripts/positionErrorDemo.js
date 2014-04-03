var startTimer = setInterval(function() { showInstruction()},2000);
var instructionTime = 5000;
var count = 5;
var showInstructionTime = 0;
var showTimerTime = 0;
var positionWordTime = 0;
var questionTime = 0;
var i =0;
var question_set;
var question_letter;
var question_correctAnswer;
var trial_blinkTime;
var expDuration;
var dragElements;
var dropZone;
var dropZoneOrigin;
var question = new Array();

function onLoad(){

    dragElements = document.querySelectorAll('#dragElements div');
    dropZone = document.querySelectorAll('#dropZone .dropChar');
    dropZoneOrigin = document.querySelector('#dragElements');
    for (var j = 0; j < dragElements.length; j++) {
        // Event Listener for when the drag interaction starts.
        dragElements[j].addEventListener('dragstart', function(e) {
            e.dataTransfer.setData('text', this.innerHTML);
            currentDragElement = this;
        });

        // Event Listener for when the drag interaction finishes.
        dragElements[j].addEventListener('dragend', function(e) {
            currentDragElement = null;
        });
    }

    for (var j = 0; j < dropZone.length; j++) {

        dropZone[j].addEventListener('dragstart', function(e) {
            e.dataTransfer.setData('text', this.innerHTML);
            currentDragElement = this;
        });

        // Event Listener for when the drag interaction finishes.
        dropZone[j].addEventListener('dragend', function(e) {
            currentDragElement = null;
        });

        dropZone[j].addEventListener('dragover', function(e) {
          if (e.preventDefault) {
            e.preventDefault();
          }
          return false;
        });

        // Event Listener for when the dragged element enters the drop zone.
        dropZone[j].addEventListener('dragenter', function(e) {
            //TODO
        });

        // Event Listener for when the dragged element leaves the drop zone.
        dropZone[j].addEventListener('dragleave', function(e) {
            //TODO
        });

        // Event Listener for when the dragged element dropped in the drop zone.
        dropZone[j].addEventListener('drop', function(e) {
            if (e.preventDefault) e.preventDefault();
            if (e.stopPropagation) e.stopPropagation();
            if (this.innerHTML == ""){
                this.innerHTML = e.dataTransfer.getData('text');
                currentDragElement.innerHTML = "";
            }

            return false;
        });
    }
    dropZoneOrigin.addEventListener('dragover', function(e) {
        if (e.preventDefault) {
            e.preventDefault();
        }
        return false;
    });

    // Event Listener for when the dragged element enters the drop zone.
    dropZoneOrigin.addEventListener('dragenter', function(e) {
        //TODO
    });

    // Event Listener for when the dragged element leaves the drop zone.
    dropZoneOrigin.addEventListener('dragleave', function(e) {
        //TODO
    });

    dropZoneOrigin.addEventListener('drop', function(e) {
        if (e.preventDefault) e.preventDefault();
        if (e.stopPropagation) e.stopPropagation();
        if (currentDragElement.innerHTML != ""){
            for (var j = 0; j < dragElements.length; j++){
                if (dragElements[j].innerHTML == ""){
                    currentDragElement.innerHTML = "";
                    dragElements[j].innerHTML = e.dataTransfer.getData('text');
                    break;
                }
            }
        }
        return false;
    });
}

function genQuestion(){
/*
    for (var j=0;j<question_set.length;j++){
        question[j] = question_set.charAt(j);
    }
    */
    question[0] = 'A';
    question[1] = 'B';
    question[2] = 'C';
    question[3] = 'D';
    question[4] = 'E';
    question[5] = 'F';
    question[6] = 'G';
    question[7] = 'H';
    question[8] = 'I';
    question[9] = 'J';

}

function showInstruction (){
    clearInterval(startTimer);
    //question_set = document.getElementById("questionSet").innerHTML;
    //question_letter = document.getElementById("questionLetter").innerHTML;
    //question_correctAnswer = document.getElementById("correctAnswer").innerHTML;
    //trial_blinkTime = document.getElementById("blinkTime").innerHTML;
    question_correctAnswer = true;
    trial_blinkTime = 0.5;
    genQuestion();
    document.getElementById("word").innerHTML=" จำตัวอักษรที่ปรากฎ";
    showInstructionTime = setInterval(function(){showTimer()},5000);
}
function showTimer(){
    clearInterval(showInstructionTime);
    showTimerTime =   setInterval(calculateTimeLeft,1000);
    positionWordTime = setInterval(function(){showWordSeries()},5000);
}
function showWordSeries(){
    clearInterval(positionWordTime);
    clearInterval(showTimerTime);
    questionTime = setInterval(showQuestionList,trial_blinkTime*1000);
}
function showQuestionList(){
    if (i < question.length){
        document.getElementById("word").innerHTML= question[i];
        i++;
    }
    else{
        showQuestionWord();
    }
}
function showQuestionWord(){
    clearInterval(questionTime);
    document.querySelector('.positionWord').style.top = "10%";
    document.getElementById("dragElements").style.visibility = "visible";
    document.getElementById("dropZone").style.visibility = "visible";
    document.getElementById("confirm").style.visibility = "visible";
    document.getElementById("word").innerHTML= "Drag & Drop ตามลำดับที่ปรากฎก่อนหน้านี้";

    d = new Date();
    expDuration = d.getTime();
}

function done(){
     d = new Date();
     expDuration = d.getTime()-expDuration;
     document.getElementById("usedTime").value = expDuration/1000;
     var word = "";
     for (var j=0;j<dropZone.length;j++){
        if (dropZone[j].innerHTML != "")
            word += dropZone[j].innerHTML;
        else{
            word += "?";
        }
     }
     document.getElementById("answer").value = word;
}

function calculateTimeLeft()
{
  count=count-1;
        document.getElementById("word").innerHTML= " โจทย์จะเริ่มในอีก " + (count-1) +" วินาที" ;
}
