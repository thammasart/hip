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

    var row,column;


    $scope.ok = function () {
        clearGrid();
        generateSharp(trial);

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

    var generateSharp = function(trial){
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
    }
    


    $scope.init = function(){
        row = Math.floor($scope.height / 30);
        column = Math.floor($scope.width / 30);
        $scope.grids = new Array(row);
        for(var i = 0; i < row; i++){
            $scope.grids[i] = new Array(column);
        }
        console.log($scope.grids);
        var top = 0;
        var left = 0;
        var x_offset = 100/column;
        var y_offset = 100/row;
        for(var i = 0; i < row; i++){
            for(var j =0; j < column; j++){
                $scope.grids[i][j] = Grid(top, left);
                left += x_offset;
            }
            top += y_offset;
            left = 0;
        }
        generateSharp(trial);
    }

    var clearGrid = function(){
        for(var i = 0; i < row; i++){
            for(var j =0; j < column; j++){
                $scope.grids[i][j].visit = false;
                $scope.grids[i][j].sharp = {};
            }
        }
    }

    var randomGrid = function(){
        var i = 0;
        var j = 0;
        var count = 0;
        while(count < 200){
           i = Math.floor((Math.random() * row));
           j = Math.floor((Math.random() * column));
           if(!$scope.grids[i][j].visit){
               return $scope.grids[i][j];
           }
           count++;
        }
    }

    var Sharp = function(color,sharp){
        var grid = randomGrid();
        var sharp = {
            top: grid.top,
            left: grid.left,
            color: color,
            sharp: sharp
        }
        grid.visit = true;
        grid.sharp = sharp;
        return sharp;
    }

    var Grid = function(top,left){
        var grid = {
            top:top,
            left:left,
            sharp:{},
            visit:false
        }
        return grid;
    }
};