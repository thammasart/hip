var startTimer = setInterval(function() { showInstruction()},2000);
var instructionTime = 5000;
var count = 5;
var showInstructionTime = 0;
var showTimerTime = 0;
var positionWordTime = 0;
var questionTime = 0;
var delayTime = 0;

var charTime = 0;
var gapTime = 0;

var i =0;
var trial_flashSpeed;
var trial_delayTime;
var expDuration;
var dragElements;
var dropZone;
var dropZoneOrigin;
var question = new Array();

function onLoad(){

    dragElements = document.querySelectorAll('#dragElements div');
    dropZone = document.querySelectorAll('#dropZone .dropChar');
    dropZonePadding = document.querySelectorAll('.dropZone');
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
            else{
                var temp = this.innerHTML;
                this.innerHTML = e.dataTransfer.getData('text');
                currentDragElement.innerHTML = temp;
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

    for (var j = 0; j < dragElements.length; j++) {
        question[j] = dragElements[j].innerHTML;
    }
    randomDragElementOrder();
}

function randomDragElementOrder(){
    var textLength = dragElements.length;
    var isSame = true;
    do{
        for (var j = 0; j < textLength; j++) {
            var randIndex = Math.floor(Math.random()*textLength);

            var temp = dragElements[j].innerHTML;
            dragElements[j].innerHTML = dragElements[randIndex].innerHTML;
            dragElements[randIndex].innerHTML = temp;

        }

        for (var j = 0; j < textLength; j++) {
            if (question[j] != dragElements[j].innerHTML){
                isSame = false;
                break;
            }
            isSame = true;
        }

    }while(isSame);
}

function showInstruction (){
    clearInterval(startTimer);
    trial_flashSpeed = document.getElementById("flashSpeed").innerHTML;
    trial_delayTime = document.getElementById("delayTime").innerHTML;

    question_correctAnswer = true;
    genQuestion();
    document.getElementById("word").innerHTML=" จำลำดับตัวอักษรที่ปรากฎ";
    showInstructionTime = setInterval(function(){showTimer()},5000);
}

function showTimer(){
    clearInterval(showInstructionTime);
    showTimerTime =   setInterval(calculateTimeLeft,1000);
    delayTime =setInterval(function(){showBlankBefore()},5000);
}

function showBlankBefore(){
    clearInterval(delayTime);
    clearInterval(showTimerTime);
    document.getElementById("word").innerHTML= "";
    positionWordTime = setInterval(function(){showWordSeries()},500);
}

function showWordSeries(){
    clearInterval(positionWordTime);
    showCharacter();
}

function showCharacter(){
    clearInterval(gapTime);
        
    if (i < question.length){
        document.getElementById("word").innerHTML= question[i];
        i++;
        charTime = setInterval(showBlank,trial_flashSpeed*1000);
    }
    else
        showQuestionWord();
}

function showBlank(){
    clearInterval(charTime);

    document.getElementById("word").innerHTML = "";
    gapTime = setInterval(showCharacter,trial_delayTime*1000);
}

function showQuestionWord(){
    clearInterval(charTime);
    for (var x = 0; x < dropZonePadding.length; x++) {
        dropZonePadding[x].style.paddingTop = "8%";
    }
    document.querySelector('.positionWord').style.top = "5%";
    document.getElementById("dragElements").style.visibility = "visible";
    var dropWidth = 1+(6*dragElements.length);
    var dropMargin = (100.0-dropWidth)/2.0 ;
    var boxWidth = (100.0 - dragElements.length)/dragElements.length;
    document.getElementById("dropZone").style.width = dropWidth+"%" ;
    document.getElementById("dropZone").style.marginLeft= dropMargin+"%" ;
    var boxSet = document.getElementsByName("box");

    for(var k = 0; k < boxSet.length;k++){
        boxSet[k].style.width = boxWidth+"%";
    }
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
    document.getElementById("isCorrect").checked = true;
    var word = "";
    for (var j=0;j<dropZone.length;j++){
        word += dropZone[j].innerHTML;
        if (dropZone[j].innerHTML != question[j]){
            document.getElementById("isCorrect").checked = false;
        }
     }
     document.getElementById("answer").value = word;
}

function calculateTimeLeft(){
    count=count-1;
    document.getElementById("word").innerHTML= " โจทย์จะเริ่มในอีก " + (count-1) +" วินาที" ;
}
