angular.module('ExperimentCreator', ['ui.bootstrap'])
    .controller('ExController', function($scope){
        $scope.word = /^[a-zA-Z0-9ก-๙_ \-]*$/;
        $scope.value = 3;

        $scope.getInputStatus = function(input){
            if(input.$pristine)
                return 2;
            if(input.$valid)
                return 0;
            return 1;
        };


        $scope.today = function() {
            $scope.dt = new Date();
            $scope.startDate = new Date();
            $scope.expireDate = new Date();
        };
        $scope.today();

        $scope.stringToDate = function(value){
            return new Date(value);
        };

        $scope.showWeeks = true;
        $scope.toggleWeeks = function () {
            $scope.showWeeks = ! $scope.showWeeks;
        };

        $scope.clear = function () {
            $scope.dt = null;
        };

        // Disable weekend selection
        $scope.disabled = function(date, mode) {
            return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
        };

        $scope.toggleMin = function() {
            $scope.minDate = ( $scope.minDate ) ? null : new Date();
        };
        $scope.toggleMin();

        $scope.open = function($event, target) {
            $event.preventDefault();
            $event.stopPropagation();

            if(target === 0)
                $scope.startDateOpened = true;
            else if(target === 1)
                $scope.expireDateOpened = true;
        };

        $scope.dateOptions = {
            'year-format': "'yy'",
            'starting-day': 1
        };

        $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'shortDate', 'dd-MM-yyyy'];
        $scope.format = $scope.formats[3];

        $scope.checkDateValid = function(input){
            if($scope.startDate <= $scope.expireDate){
                input.startDate.$setValidity('startDate', true);
                input.expireDate.$setValidity('expireDate', true);
                return true;
            }

            input.startDate.$setValidity('startDate', false);
            input.expireDate.$setValidity('expireDate', false);
            return false;
        }
    })
    .controller('BrownPetersonCtrl', function($scope){
        $scope.initCountdowns = [];
        $scope.flashTimes = [];
        $scope.trigramTypes = [];
        $scope.trigramLanguages = [];

    })
    .controller('AttentionBlinkCtrl', function($scope){
        $scope.word = /^[0-9]*\.?[0-9]+$/;
    })
    .controller('SignalDetectionCtrl', function($scope){
        $scope.single = /^[a-zA-Z0-9ก-ฮ]{1}$/;
        $scope.floatPattern = /^[0-9]*\.?[0-9]+$/;
    })
    .controller('StroofEffectCtrl', function($scope){
    })
    .controller('PositionErrorCtrl', function($scope){
        $scope.word = /^[0-9]*\.?[0-9]+$/;
        $scope.memorySet = [    {length: 3},
                                {length: 5},
                                {length: 7},
                                {length: 10},
                                {length: 12}];
    })
    .controller('SternbergSearchCtrl', function($scope){
        $scope.word = /^[0-9]*\.?[0-9]+$/;
    })
    .controller('MagicNumber7Ctrl', function($scope){
        $scope.word = /^[0-9]*\.?[0-9]+$/;
    })
    .controller('SimonEffectCtrl', function($scope){
        $scope.trials = [];
        $scope.floatPattern = /^[0-1]*\.?[0-9]+$/;
    })
    .controller('VisualSearchCtrl', function($scope, $modal, $http){

        $scope.inProcess = false;
        $scope.overNoise = false;

        $scope.trials = [];

        $scope.init = function(expId){
            $scope.inProcess = true;
            $http({method:'GET',url:'init',params:{exp_id:expId}}).success(function(result){
                $scope.trials = result.trials;
                $scope.inProcess = false;
                console.log($scope.trials);
            }).error(function(result){
                console.log('error:' + result);
                $scope.inProcess = false;
            });
        }

        $scope.save = function(trial){
            $scope.inProcess = true;
            $http({method:'POST',url:'saveVisualSearch',data:trial.quiz.question.sharps,
                params:{trialId:trial.id,
                circleGreen:trial.quiz.circleGreen, circleRed:trial.quiz.circleRed,
                circleBlue: trial.quiz.circleBlue,
                squareBlue:trial.quiz.squareBlue, squareRed:trial.quiz.squareRed,
                squareGreen:trial.quiz.squareGreen,
                positionXofTarget: trial.quiz.positionXofTarget, 
                positionYofTarget: trial.quiz.positionYofTarget,
                frameSize: trial.quiz.frameSize, target: trial.quiz.target}})
            .success(function(result){
                $scope.inProcess = false;
                console.log(result);
            }).error(function(result){
                console.log('error:' + result);
                $scope.inProcess = false;
            });
        }

        $scope.frameSizes = ['SMALLER', 'SMALL', 'MEDIUM', 'BIG', 'EXTRA'];
        $scope.targets = ['SQAURE_GREEN', 'SQAURE_BLUE', 'SQAURE_RED', 
        'CIRCLE_GREEN', 'CIRCLE_BLUE', 'CIRCLE_RED'];

        $scope.changeTargetValue = function(trial){
            if(trial.quiz.target == 'SQAURE_GREEN')
                trial.quiz.squareGreen = 1;
            else if(trial.quiz.target == 'SQAURE_BLUE')
                trial.quiz.squareBlue = 1;
            else if(trial.quiz.target == 'SQAURE_RED')
                trial.quiz.squareRed = 1;
            else if (trial.quiz.target == 'CIRCLE_RED')
                trial.quiz.circleRed = 1;
            else if (trial.quiz.target == 'CIRCLE_BLUE')
                trial.quiz.circleBlue = 1;
            else if (trial.quiz.target == 'CIRCLE_GREEN')
                trial.quiz.circleGreen = 1;
        }

        $scope.open = function(trial){
            var modalInstance = $modal.open({
                templateUrl: 'preview.html',
                controller: ModalInstanceCtrl,
                size: 'lg',
                resolve: {
                    sharps : function(){
                        return $scope.sharps;
                    },
                    width : function(){
                        return $scope.width(trial);
                    },
                    height : function(){
                        return $scope.height(trial);
                    },
                    trial : function(){
                        return trial;
                    }
                }

            });

            modalInstance.result.then(function (result){
               trial.quiz.positionYofTarget = result.top;
               trial.quiz.positionXofTarget = result.left;
            });
        };

        $scope.max = function(trial){
            var frameSize = trial.quiz.frameSize;
            if(frameSize == 'SMALLER')
                return 30;
            if(frameSize == 'SMALL')
                return 50;
            if(frameSize == 'MEDIUM')
                return 90;
            if(frameSize == 'BIG')
                return 140;
            if(frameSize == 'EXTRA')
                return 160;

            return 25;
        };

        $scope.totalNoise = function(trial){
            return trial.quiz.squareRed + trial.quiz.squareBlue + trial.quiz.squareGreen + trial.quiz.circleGreen + trial.quiz.circleRed + trial.quiz.circleGreen;
        }



        $scope.width = function(trial){
            var frameSize = trial.quiz.frameSize;
            if(frameSize == 'SMALLER')
                return 300;
            if(frameSize == 'SMALL')
                return 500;
            if(frameSize == 'MEDIUM')
                return 600;
            if(frameSize == 'BIG')
                return 800;
            if(frameSize == 'EXTRA')
                return 1000;

            return 600;
        };
        $scope.height = function(trial){
            var frameSize = trial.quiz.frameSize;
            if(frameSize == 'SMALLER')
                return 200;
            if(frameSize == 'SMALL')
                return 300;
            if(frameSize == 'MEDIUM')
                return 400;
            if(frameSize == 'BIG')
                return 500;
            if(frameSize == 'EXTRA')
                return 500;

            return 400;
        };



    })
    .controller('MullerLayerCtrl', function($scope, $http, $modal){
        $scope.inProcess = false;
        $scope.floatPattern = /^[0-1]*\.?[0-9]+$/;
        $scope.trials = [];

        $scope.noOfChoices = [3,4,5];
        $scope.lengthTypes = ['SHORT', 'MEDIUM','LONG'];

        $scope.init = function(expId){
            $scope.inProcess = true;
            $http({method:'GET',url:'mullerInit',params:{expId:expId}}).success(function(result){
                $scope.trials = result.trials;
                $scope.inProcess = false;
                console.log($scope.trials);
            }).error(function(result){
                console.log('error:' + result);
                $scope.inProcess = false;
            });
        }

        $scope.open = function(quiz){
            var modalInstance = $modal.open({
                templateUrl: 'preview.html',
                controller: MullerModalInstanceCtrl,
                size: 'lg',
                resolve: {
                    quiz : function(){
                        return quiz;
                    }
                }

            });
        };

        $scope.openSummary = function(){
            var modalInstance = $modal.open({
                templateUrl: 'summary.html',
                controller: MullerSummaryInstanceCtrl,
                size: 'lg',
                resolve: {
                    trials : function(){
                        return $scope.trials;
                    }
                }

            });
        };

        $scope.save = function(){
            save(0);
        }

        function save(index){
            if(index < $scope.trials.length){
                $scope.inProcess = true;
                $http({method:'PUT',url:'saveMullerTrial',data:$scope.trials})
                .success(function(result){
                    $scope.inProcess = false;
                    console.log(result);
                    index++;
                    save(index);
                }).error(function(result){
                    console.log('error:' + result);
                    $scope.inProcess = false;
                    return;
                });
            }
            return;
        }

        $scope.calculateDifferChoice = function(quiz){
            if(quiz.differChoice > quiz.noOfChoice)
                quiz.differChoice = Math.floor((Math.random() * quiz.noOfChoice) + 1);

        }
    });

var MullerSummaryInstanceCtrl = function($scope, $modalInstance, trials){

    $scope.trials = trials;

    $scope.ok = function () {
        $modalInstance.close();
    };

    $scope.cancel = function () {
        $modalInstance.dismiss();
    };

    $scope.calculateBackground = function(type, show){

        if(!show)
            return ''

        if(type == 'NONE')
            return 'active'
        if(type == 'LEFT')
            return 'success'
        if(type == 'RIGHT')
            return 'info'
        if(type == 'IN')
            return 'warning'
        if(type == 'OUT')
            return 'danger'

        return '';
    }
}

var MullerModalInstanceCtrl = function($scope, $modalInstance, quiz){

    var canvas = {};

    $scope.ok = function () {
        $modalInstance.close();
    };

    $scope.cancel = function () {
        $modalInstance.dismiss();
    };

    $scope.init = function(){

        canvas = document.getElementById("arrowPreview");
        var width = 800;
        var height = 400;
        var length = calculateLength(quiz);
        var x = (800/2) - (length/2);
        var y = 30;

        var question = quiz.question;
        drawLine(calculateStartX(1, x), y, calculateDifferenceLength(1,length), question.line1, 1);
        y += 70;
        drawLine(calculateStartX(2, x), y, calculateDifferenceLength(2,length), question.line2, 2);
        y += 70;
        drawLine(calculateStartX(3, x), y, calculateDifferenceLength(3,length), question.line3, 3);
        y += 70;
        if(quiz.noOfChoice > 3) {
            drawLine(calculateStartX(4, x), y, calculateDifferenceLength(4,length), question.line4, 4);
            y += 70;
        }
        if(quiz.noOfChoice > 4) {
            drawLine(calculateStartX(5, x), y, calculateDifferenceLength(5,length), question.line5, 5);
        }


    }

    $scope.shuffle = function(){
        var question = quiz.question;
        var array = [question.line1, question.line2, question.line3,
                    question.line4, question.line5];
        array = shuffle(array);
        question.line1 = array[0];
        question.line2 = array[1];
        question.line3 = array[2];
        question.line4 = array[3];
        question.line5 = array[4];

        quiz.differChoice = Math.floor((Math.random() * quiz.noOfChoice) + 1);

        canvas.width = canvas.width;
        $scope.init();
    }

    function shuffle(array) {
      var currentIndex = array.length
        , temporaryValue
        , randomIndex
        ;

      // While there remain elements to shuffle...
      while (0 !== currentIndex) {

        // Pick a remaining element...
        randomIndex = Math.floor(Math.random() * currentIndex);
        currentIndex -= 1;

        // And swap it with the current element.
        temporaryValue = array[currentIndex];
        array[currentIndex] = array[randomIndex];
        array[randomIndex] = temporaryValue;
      }

      return array;
    }

    function calculateDifferenceLength(no, length){
        if(!quiz.isPositive)
            return no == quiz.differChoice ? length - Math.floor((quiz.differLength * 38)) : length;
        else
            return no == quiz.differChoice ? length + Math.floor((quiz.differLength * 38)) : length;
    }
    function calculateStartX(no, x){
        if(!quiz.isPositive)
            return no == quiz.differChoice ? x + Math.floor((quiz.differLength * 38 / 2)) : x;
        else
            return no == quiz.differChoice ? x - Math.floor((quiz.differLength * 38 / 2)) : x;
    }

    function calculateLength(quiz){
        if(quiz.lengthType == 'SHORT'){
            return 300;
        }else if(quiz.lengthType == 'MEDIUM'){
            return 400;
        }else if(quiz.lengthType == 'LONG'){
            return 500;
        }
        return 300;
    }

    function drawLine(x, y, length, type, choice){
        var context = canvas.getContext("2d");
        context.beginPath();
        context.moveTo(x, y);
        drawLeftArrow(x, y, type, context);
        context.lineTo(x + length, y);
        drawRightArrow(x + length, y, type, context);
        context.closePath();
        var color = choice == quiz.differChoice ? 'blue' : 'black';
        context.strokeStyle = color;
        context.lineWidth=2;
        context.stroke();
    }

    function drawLeftArrow(x, y, type, context){
        if(type == 'LEFT' || type == 'IN'){
            context.lineTo(x + 30, y - 30);
            context.moveTo(x, y);
            context.lineTo(x + 30, y + 30);
            context.moveTo(x, y);
        }
        if(type == 'OUT'){
            context.lineTo(x - 30, y - 30);
            context.moveTo(x, y);
            context.lineTo(x - 30, y + 30);
            context.moveTo(x, y);
        }
    }
    function drawRightArrow(x, y, type, context){
        if(type == 'RIGHT' || type == 'IN'){
            context.lineTo(x - 30, y - 30);
            context.moveTo(x, y);
            context.lineTo(x - 30, y + 30);
        }
        if(type == 'OUT'){
            context.lineTo(x + 30, y - 30);
            context.moveTo(x, y);
            context.lineTo(x + 30, y + 30);
        }
    }
}

var ModalInstanceCtrl = function ($scope, $modalInstance, sharps, width, height, trial) {

    $scope.sharps = sharps;
    $scope.width = width;
    $scope.height = height;
    $scope.editSharp = {};
    $scope.floatPattern = /^[0-9]*\.?[0-9]+$/;
    $scope.sequence = 1;
    $scope.target = {};
    $scope.editSharp = {};
    $scope.target = null;


    var x_offset;
    var y_offset;


    $scope.generate = function () {
        generateSharps(trial);
    };
    $scope.ok = function () {
        trial.quiz.question.sharps = angular.toJson($scope.sharps);
        $modalInstance.close($scope.target);
    };

    $scope.cancel = function () {
        $modalInstance.dismiss();
    };
    $scope.colorToString = function(color){
        var colorText = '';
        switch (color){
            case 'red' : colorText='#A51B00'; break;
            case 'blue': colorText='#57BAC9';break;
            case 'green': colorText='#2ED2AE'; break;
            case 'error' : colorText="#FF0000";break;
        }
        return colorText;
    }

    $scope.init = function(){
        var row = Math.floor($scope.height / 30);
        var column = Math.floor($scope.width / 30);
        x_offset = Math.floor(100/column);
        y_offset = Math.floor(100/row);

        if(trial.quiz.target == 'CIRCLE_RED'){
            $scope.target = Sharp(trial.quiz.positionYofTarget, trial.quiz.positionXofTarget, 'red', 'circle');
        }
        else if(trial.quiz.target == 'CIRCLE_GREEN'){
            $scope.target = Sharp(trial.quiz.positionYofTarget, trial.quiz.positionXofTarget, 'green', 'circle');
        }
        else if(trial.quiz.target == 'SQAURE_BLUE'){
            $scope.target = Sharp(trial.quiz.positionYofTarget, trial.quiz.positionXofTarget, 'blue', 'square');
        }
        else if(trial.quiz.target == 'SQAURE_RED'){
            $scope.target = Sharp(trial.quiz.positionYofTarget, trial.quiz.positionXofTarget, 'red', 'square');
        }
        else if(trial.quiz.target == 'SQAURE_GREEN'){
            $scope.target = Sharp(trial.quiz.positionYofTarget, trial.quiz.positionXofTarget, 'green', 'square');
        }
        else if(trial.quiz.target == 'CIRCLE_BLUE'){
            $scope.target = Sharp(trial.quiz.positionYofTarget, trial.quiz.positionXofTarget, 'blue', 'circle');
        }
        $scope.sharps = angular.fromJson(trial.quiz.question.sharps);
    }

    var generateSharps = function(trial){
        $scope.sharps = [];
        if(trial.quiz.target != 'CIRCLE_RED'){
            for(var i = 0; i < trial.quiz.circleRed;i++){
                var obj = createSharp('red','circle');
                if(obj != null)
                    $scope.sharps.push(obj);
            }
        }
        if(trial.quiz.target != 'CIRCLE_GREEN'){
            for(var i = 0; i < trial.quiz.circleGreen;i++){
                var obj = createSharp('green','circle');
                if(obj != null)
                    $scope.sharps.push(obj);
            }
        }
        if(trial.quiz.target != 'SQAURE_BLUE'){
            for(var i = 0; i < trial.quiz.squareBlue;i++){
                var obj = createSharp('blue','square');
                if(obj != null)
                    $scope.sharps.push(obj);
            }
        }
        if(trial.quiz.target != 'SQAURE_RED'){
            for(var i = 0; i < trial.quiz.squareRed;i++){

                var obj = createSharp('red','square');
                if(obj != null)
                    $scope.sharps.push(obj);
            }
        }
        if(trial.quiz.target == 'SQAURE_GREEN'){
            for(var i = 0; i < trial.quiz.squareGreen;i++){

                var obj = createSharp('green','square');
                if(obj != null)
                    $scope.sharps.push(obj);
            }
        }
        if(trial.quiz.target == 'CIRCLE_BLUE'){
            for(var i = 0; i < trial.quiz.circleBlue; i++){

                var obj = createSharp('blue', 'circle');
                if(obj != null)
                    $scope.sharps.push(obj);
            }
        }
    }

    function createSharp(color, circle){
        var top = 0;
        var left = 0;

        for(var i=0; i<200; i++){
            top = Math.random() * (100 - y_offset);
            left = Math.random() * (100 - x_offset);
            if(!detectOtherCollission(top,left)){
                return Sharp(top,left,color,circle);
            }
        }

        return null;
    }

    function detectOtherCollission(top1, left1){
        var right1 = left1 + x_offset;
        var bottom1 = top1 + y_offset;
        for(var i=0; i < $scope.sharps.length; i++){
            if($scope.sharps[i] != null) {
                var top2 = $scope.sharps[i].top;
                var bottom2 = $scope.sharps[i].top + y_offset;
                var left2 = $scope.sharps[i].left;
                var right2 = $scope.sharps[i].left + x_offset;
                if (((top1 < top2 && top2 < bottom1) ||
                    (top1 < bottom2 && bottom2 < bottom1)) &&
                    ((left1 < left2 && left2 < right1) ||
                      (left1 < right2 && right2 < right1))){
                    return true;
                }
            }
        }
        return false;
    }
    


    


    var Sharp = function(top,left,color,sharp){
        var sharp = {
            id: $scope.sequence++,
            top: top,
            left: left,
            color: color,
            sharp: sharp
        }
        return sharp;
    }

    $scope.showSharp = function(sharp){
        $scope.editSharp = sharp;
        console.log($scope.editSharp);
    }
};
