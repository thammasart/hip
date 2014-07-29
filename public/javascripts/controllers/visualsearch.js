angular.module('VisualSearchApp',[])
    .controller('VisualSearchController', ['$scope','$http', function($scope, $http){
        $scope.sharps = []
        $scope.colorToString = function(color){
            var colorText = '';
            switch (color){
                case 'red' : colorText='#A51B00'; break;
                case 'blue': colorText='#57BAC9';break;
                case 'green': colorText='#2ED2AE'; break;
            }
            return colorText;
        }



        $scope.init = function(trialId){
            console.log('init');
            $http({method:'GET',url:'init',params:{trialId:trialId}}).success(function(result){
                $scope.trial = result.trial;
                console.log($scope.trial);
                console.log($scope.sharps);
                $scope.sharps = angular.fromJson($scope.trial.quiz.question.sharps);
                $scope.target = generateTarget();
                console.log($scope.sharps);
                
            }).error(function(result){
                console.log('error:' + result);
            });
        }

        function generateTarget(){
            var obj = {};
            obj.top = $scope.trial.quiz.positionYofTarget;
            obj.left = $scope.trial.quiz.positionXofTarget;
            if($scope.trial.quiz.target == 'CIRCLE_RED'){
                obj.color = 'red';
                obj.sharp = 'circle';
            }
            else if($scope.trial.quiz.target == 'CIRCLE_GREEN'){
                obj.color = 'green';
                obj.sharp = 'circle';
            }
            else if($scope.trial.quiz.target == 'SQAURE_BLUE'){
                obj.color = 'blue';
                obj.sharp = 'square';
            }
            else if($scope.trial.quiz.target == 'SQAURE_RED'){
                obj.color = 'red';
                obj.sharp = 'square';
            }
            else if($scope.trial.quiz.target == 'SQAURE_GREEN'){
                obj.color = 'green';
                obj.sharp = 'square';
            }
            else if($scope.trial.quiz.target == 'CIRCLE_BLUE'){
                obj.color = 'blue';
                obj.sharp = 'circle';
            }
            return obj;
        }
    }]);