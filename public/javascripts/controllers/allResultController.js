var gNumer = 0;
var gTime = new Array(0,0,0,0);
var gScore = new Array(0,0,0,0);
angular.module('AllResultApp', ['ui.bootstrap'])
    .controller('AllResultController',function($scope,$http,$location,$rootScope,$modal){
        
        $scope.trials = [];
        $scope.trials2 = [];
        $scope.trials3 = [];
        $scope.trials4 = [];
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
                $scope.trials2 = result.trials2;
                $scope.trials3 = result.trials3;
                $scope.trials4 = result.trials4;
                //cut here Questiontype
                $scope.questionTypes = result.questionTypes;
                console.log($scope.trials);
                clearGraph();
            });
        }
        
         function clearGraph() { 
            document.getElementById("tBeam1").style.height = 0;
            document.getElementById("tBeam2").style.height = 0;
            document.getElementById("tBeam3").style.height = 0;
            document.getElementById("tBeam4").style.height = 0;

            document.getElementById("sBeam1").style.height = 0;
            document.getElementById("sBeam2").style.height = 0;
            document.getElementById("sBeam3").style.height = 0;
            document.getElementById("sBeam4").style.height = 0;

            document.getElementById("graphUser1").innerHTML = "n/a";
            document.getElementById("graphUser2").innerHTML = "n/a";
            document.getElementById("graphUser3").innerHTML = "n/a";
            document.getElementById("graphUser4").innerHTML = "n/a";

            document.getElementById("graphScore1").innerHTML = "n/a";
            document.getElementById("graphScore2").innerHTML = "n/a";
            document.getElementById("graphScore3").innerHTML = "n/a";
            document.getElementById("graphScore4").innerHTML = "n/a";

            document.getElementById("graphTime1").innerHTML = "n/a";
            document.getElementById("graphTime2").innerHTML = "n/a";
            document.getElementById("graphTime3").innerHTML = "n/a";
            document.getElementById("graphTime4").innerHTML = "n/a";
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
                    trials2 : function(){
                        return $scope.trials2;
                    },
                    trials3 : function(){
                        return $scope.trials3;
                    },
                    trials4 : function(){
                        return $scope.trials4;
                    },
                    questionTypes : function(){
                        return $scope.questionTypes;
                    }
                }

            });
        };
    }
);
    var resultModalInstanceCtrl= function($scope, $modalInstance,trials,trials2,trials3,trials4,questionTypes){
        
        $scope.trials =[];
        $scope.trials2 =[];
        $scope.trials3 =[];
        $scope.trials4 =[];
        $scope.questionTypes =[];

        $scope.init = function(){
            $scope.trials = trials;
            $scope.trials2 = trials2;
            $scope.trials3 = trials3;
            $scope.trials4 = trials4;
            $scope.questionTypes = questionTypes;
            $scope.title = trials[0].schedule.experimentType;
        };
        $scope.ok = function () {
            $modalInstance.close();
        };

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        Object.size = function(obj) {
            var size = 0, key;
            for (key in obj) {
                if (obj.hasOwnProperty(key)) size++;
            }
            return size;
        };

    // Get the size of an object

        $scope.sel = function (totalScore,totalUsedTime,totalUser,trialIndex,qTypeNumber,tName,tId) {
            if(qTypeNumber == 1){
                var size = Object.size(trials[trialIndex].quizzes);
            }else if(qTypeNumber ==2){
                var size = Object.size(trials2[trialIndex].quizzes);
            }else if(qTypeNumber ==3){
                var size = Object.size(trials3[trialIndex].quizzes);
            }else if(qTypeNumber ==4){
                var size = Object.size(trials4[trialIndex].quizzes);
            }


            console.log(size);
            console.log(questionTypes);
            if(gNumber == 1){
                document.getElementById("t1Name").value = tName + ":" + tId;
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

                    document.getElementById("sBeam1").style.height = (gScore[0]/size)*100+"%";
                    document.getElementById("sBeam2").style.height = (gScore[1]/size)*100+"%";
                    document.getElementById("sBeam3").style.height = (gScore[2]/size)*100+"%";
                    document.getElementById("sBeam4").style.height = (gScore[3]/size)*100+"%";

                    document.getElementById("line2").innerHTML = max*0.2+"sec";
                    document.getElementById("line3").innerHTML = max*0.4+"sec";
                    document.getElementById("line4").innerHTML = max*0.6+"sec";
                    document.getElementById("line5").innerHTML = max*0.8+"sec";
                    document.getElementById("line6").innerHTML = max+"sec";
                   $modalInstance.close();
        };
    }


