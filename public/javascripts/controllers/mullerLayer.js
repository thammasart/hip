
/**
 * Created by ohmini on 31/7/2557.
 */
angular.module('MullerLayerApp',[])
    .controller('MullerLayerController', ['$scope','$http', function($scope, $http){
        var canvas = {};
        $scope.quiz = {};

        $scope.init = function(trialId, questionNo){
            $scope.quiz = {};
            $http({method:'GET',url:'findQuestion',params:{trialId:trialId, questionNo: questionNo}}).success(function(result){
                $scope.quiz = result.quiz;
                document.getElementById("displayTime").value = result.displayTime;
                drawCanvas()
            }).error(function(result){
                console.log('error:' + result);
            });
        }

        function drawCanvas() {
            canvas = document.getElementById("arrowPreview");
            var width = 800;
            var height = 400;
            var length = calculateLength($scope.quiz);
            var x = (800/2) - (length/2);
            var y = 30;
            var question = $scope.quiz.question;
            drawLine(calculateStartX(1, x), y, calculateDifferenceLength(1,length), question.line1);
            y += 70;
            drawLine(calculateStartX(2, x), y, calculateDifferenceLength(2,length), question.line2);
            y += 70;
            drawLine(calculateStartX(3, x), y, calculateDifferenceLength(3,length), question.line3);
            y += 70;
            if($scope.quiz.noOfChoice > 3) {
                drawLine(calculateStartX(4, x), y, calculateDifferenceLength(4,length), question.line4);
                y += 70;
            }
            if($scope.quiz.noOfChoice > 4) {
                drawLine(calculateStartX(5, x), y, calculateDifferenceLength(5,length), question.line5);
            }
        }

        function drawLine(x, y, length, type){
            var context = canvas.getContext("2d");
            context.beginPath();
            context.moveTo(x, y);
            drawLeftArrow(x, y, type, context);
            context.lineTo(x + length, y);
            drawRightArrow(x + length, y, type, context);
            context.closePath();
            context.strokeStyle = 'black';
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

        function calculateDifferenceLength(no, length){
            if(!$scope.quiz.isPositive)
                return no == $scope.quiz.differChoice ? length - Math.floor(($scope.quiz.differLength * 38)) : length;
            else
                return no == $scope.quiz.differChoice ? length + Math.floor(($scope.quiz.differLength * 38)) : length;
        }
        function calculateStartX(no, x){
            if(!$scope.quiz.isPositive)
                return no == $scope.quiz.differChoice ? x + Math.floor(($scope.quiz.differLength * 38 / 2)) : x;
            else
                return no == $scope.quiz.differChoice ? x - Math.floor(($scope.quiz.differLength * 38 / 2)) : x;
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

        $scope.calculateOffset = function(){
            if($scope.quiz.noOfChoice == 4){
                return 'col-md-offset-2';
            }
            if($scope.quiz.noOfChoice == 5)
                return 'col-md-offset-1';
            return 'col-md-offset-3';
        }

    }]);
