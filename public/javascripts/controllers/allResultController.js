var gNumber = 0;
angular.module('AllResultApp', ['ui.bootstrap'])
    .controller('AllResultController',function($scope,$http,$location,$rootScope,$modal){
        
        $scope.trials = [];
        $scope.questionTypes = [];
        $rootScope.$watch(function(){return $location.path()},function(path){
            //console.log($location.path());
            
            if(path != ''){ 
                findExperiment(path);
            }
        });
        function findExperiment(path){
            $http({method : 'GET',url : 'findExperiment' ,params : { exp_type:path}})
            .success(function(result){
                $scope.trials = result.trials;
                $scope.questionTypes = result.questionTypes;
                console.log($scope.trials);
            });
        }
        $scope.open = function(num){
            gNumber = num;
            var modalInstance = $modal.open({
                templateUrl: 'preview.html',
                controller: resultModalInstanceCtrl,
                size: 'lg',
                resolve: {
                    trials : function(){
                        return $scope.trials;
                    },
                    questionTypes : function(){
                        return $scope.questionTypes;
                    }
                }

            });
        };
    }
);
    var resultModalInstanceCtrl= function($scope, $modalInstance,trials,questionTypes){
        
        $scope.trials =[];
        $scope.questionTypes =[];
        $scope.init = function(){
            $scope.trials = trials;
            $scope.questionTypes = questionTypes;
            $scope.title = trials[0].schedule.experimentType;
        };
        $scope.ok = function () {
            $modalInstance.close();
        };

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.sel = function (totalScore,totalUsedTime,totalUser) {

            if(gNumber == 1){
                document.getElementById("graphUser1").innerHTML = totalUser;
                document.getElementById("graphTime1").innerHTML = totalUsedTime;
                document.getElementById("graphScore1").innerHTML = totalScore;
            }else
            if(gNumber == 2){
                document.getElementById("graphUser2").innerHTML = totalUser;
                document.getElementById("graphTime2").innerHTML = totalUsedTime;
                document.getElementById("graphScore2").innerHTML = totalScore;
            }else
            if(gNumber == 3){
                document.getElementById("graphUser3").innerHTML = totalUser;
                document.getElementById("graphTime3").innerHTML = totalUsedTime;
                document.getElementById("graphScore3").innerHTML = totalScore;
            }else
            if(gNumber == 4){
                document.getElementById("graphUser4").innerHTML = totalUser;
                document.getElementById("graphTime4").innerHTML = totalUsedTime;
                document.getElementById("graphScore4").innerHTML = totalScore;
            }
            $modalInstance.close();
        };
    }


