angular.module('ExperimentCreator', ['ui.bootstrap','uiSlider'])
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
        $scope.word = /^[0-1]{1}\.[0-9]+$/;
    })
    .controller('SignalDetectionCtrl', function($scope){
        $scope.single = /^[a-zA-Z0-9ก-ฮ]{1}$/;
        $scope.floatPattern = /^[0-1]{1}\.[0-9]+$/;
    })
    .controller('StroofEffectCtrl', function($scope){
    })
    .controller('PositionErrorCtrl', function($scope){
        $scope.word = /^[0-1]{1}\.[0-9]+$/;
        $scope.memorySet = [    {length: 3},
                                {length: 5},
                                {length: 7},
                                {length: 10},
                                {length: 12}];
    })
    .controller('SternbergSearchCtrl', function($scope){
        $scope.word = /^[0-1]{1}\.[0-9]+$/;
    })
    .controller('MagicNumber7Ctrl', function($scope){
        $scope.word = /^[1-9]{1}\.[0-9]+$/;
    })
    .controller('SimonEffectCtrl', function($scope){
        $scope.trials = [];
        $scope.floatPattern = /^[0-1]{1}\.[0-9]+$/;
    }).controller('VisualSearchCtrl', function($scope, $modal, $http){

        $scope.inProcess = false;

        $scope.trials = [];



        $scope.generateSharp = function(trial){
            $scope.sharps = [];
            for(var i = 0; i < trial.quiz.circleRed;i++){
                var obj = Sharp('red','circle');
                $scope.sharps.push(obj);
            }
            for(var i = 0; i < trial.quiz.circleGreen;i++){
                var obj = Sharp('green','circle');
                $scope.sharps.push(obj);
            }
            for(var i = 0; i < trial.quiz.squareBlue;i++){
                var obj = Sharp('blue','square');
                $scope.sharps.push(obj);
            }
            for(var i = 0; i < trial.quiz.squareRed;i++){
                var obj = Sharp('red','square');
                $scope.sharps.push(obj);
            }
            for(var i = 0; i < trial.quiz.squareGreen;i++){
                var obj = Sharp('green','square');
                $scope.sharps.push(obj);
            }
            console.log($scope.sharps);
        }

        var Sharp = function(color,sharp){
            var sharp = {
                top: Math.floor((Math.random() * 85) + 10),
                left: Math.floor((Math.random() * 85) + 10),
                color: color,
                sharp: sharp
            }
            return sharp;
        }

        $scope.init = function(expId){
            $scope.inProcess = true;
            $http({method:'GET',url:'init',params:{exp_id:expId}}).success(function(result){
                $scope.trials = result.trials;
                calculateSharps();
                $scope.inProcess = false;
            }).error(function(result){
                console.log('error:' + result);
                $scope.inProcess = false;
            });
        }

        var calculateSharps = function(){
            for(var i=0;i < $scope.trials.length;i++){
                var text =$scope.trials[i].quiz.question.sharps;
                var obj = angular.fromJson(text);
            }
        }

        $scope.frameSizes = ['SMALLER', 'SMALL', 'MEDIUM', 'BIG', 'EXTRA'];
        $scope.open = function(trial){
            $modal.open({
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
        };

        $scope.max = function(trial){
            var frameSize = trial.quiz.frameSize;
            if(frameSize == 'SMALLER')
                return 15;
            if(frameSize == 'SMALL')
                return 20;
            if(frameSize == 'MEDIUM')
                return 25;
            if(frameSize == 'BIG')
                return 30;
            if(frameSize == 'EXTRA')
                return 35;

            return 25;
        };

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



    });

var ModalInstanceCtrl = function ($scope, $modalInstance, sharps, width, height, trial) {

    $scope.sharps = sharps;
    $scope.width = width;
    $scope.height = height;

    var x_offset;
    var y_offset;


    $scope.ok = function () {
        generateSharps(trial);

        //$modalInstance.close();
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

    var generateSharps = function(trial){
        $scope.sharps = [];
        for(var i = 0; i < trial.quiz.circleRed;i++){
            var obj = createSharp('red','circle');
            if(obj != null)
                $scope.sharps.push(obj);
        }
        for(var i = 0; i < trial.quiz.circleGreen;i++){
            var obj = createSharp('green','circle');
            if(obj != null)
                $scope.sharps.push(obj);
        }
        for(var i = 0; i < trial.quiz.squareBlue;i++){
            var obj = createSharp('blue','square');
            if(obj != null)
                $scope.sharps.push(obj);
        }
        for(var i = 0; i < trial.quiz.squareRed;i++){
            var obj = createSharp('red','square');
            if(obj != null)
                $scope.sharps.push(obj);
        }
        for(var i = 0; i < trial.quiz.squareGreen;i++){
            var obj = createSharp('green','square');
            if(obj != null)
                $scope.sharps.push(obj);
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
    


    $scope.init = function(){
        var row = Math.floor($scope.height / 30);
        var column = Math.floor($scope.width / 30);
        x_offset = Math.floor(100/column);
        y_offset = Math.floor(100/row);

        generateSharps(trial);

    }


    var Sharp = function(top,left,color,sharp){
        var sharp = {
            top: top,
            left: left,
            color: color,
            sharp: sharp
        }
        return sharp;
    }

    $scope.showSharp = function(sharp){
        console.log(sharp);
    }
};