var gNumber = 0;
var gTime = new Array(0,0,0,0);
var gScore = new Array(0,0,0,0);
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
                gTime[0]=totalUsedTime;
                gScore[0]=totalScore;
            }else
            if(gNumber == 2){
                document.getElementById("graphUser2").innerHTML = totalUser;
                document.getElementById("graphTime2").innerHTML = totalUsedTime;
                document.getElementById("graphScore2").innerHTML = totalScore;
                gTime[1]=totalUsedTime;
                gScore[1]=totalScore;
            }else
            if(gNumber == 3){
                document.getElementById("graphUser3").innerHTML = totalUser;
                document.getElementById("graphTime3").innerHTML = totalUsedTime;
                document.getElementById("graphScore3").innerHTML = totalScore;
                gTime[2]=totalUsedTime;
                gScore[2]=totalScore;
            }else
            if(gNumber == 4){
                document.getElementById("graphUser4").innerHTML = totalUser;
                document.getElementById("graphTime4").innerHTML = totalUsedTime;
                document.getElementById("graphScore4").innerHTML = totalScore;
                gTime[3]=totalUsedTime;
                gScore[3]=totalScore;
            }
                var max =-99;

                for(var iTime = 0; iTime <gTime.length;iTime++){ 
                    if(gTime[iTime]>max)
                        max =gTime[iTime];
                }
                    document.getElementById("tBeam1").style.height = (gTime[0]/max)*100+"%";
                    document.getElementById("tBeam2").style.height = (gTime[1]/max)*100+"%";
                    document.getElementById("tBeam3").style.height = (gTime[2]/max)*100+"%";
                    document.getElementById("tBeam4").style.height = (gTime[3]/max)*100+"%";

                    document.getElementById("sBeam1").style.height = (gScore[0]/max)*100+"%";
                    document.getElementById("sBeam2").style.height = (gScore[1]/max)*100+"%";
                    document.getElementById("sBeam3").style.height = (gScore[2]/max)*100+"%";
                    document.getElementById("sBeam4").style.height = (gScore[3]/max)*100+"%";

                    document.getElementById("line2").innerHTML = max*0.2+"sec";
                    document.getElementById("line3").innerHTML = max*0.4+"sec";
                    document.getElementById("line4").innerHTML = max*0.6+"sec";
                    document.getElementById("line5").innerHTML = max*0.8+"sec";
                    document.getElementById("line6").innerHTML = max+"sec";
                   $modalInstance.close();
        };
    }


