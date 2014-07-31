angular.module('AllResultApp', ['ui.bootstrap'])
    .controller('AllResultController',function($scope,$http,$location,$rootScope,$modal){
        
        $scope.trials = [];
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
                console.log($scope.trials);
            });
        }
        $scope.open = function(){
            var modalInstance = $modal.open({
                templateUrl: 'preview.html',
                controller: resultModalInstanceCtrl,
                size: 'lg',
                resolve: {
                    trials : function(){
                        return $scope.trials;
                    }
                }

            });
        };
    }
);
    var resultModalInstanceCtrl= function($scope, $modalInstance,trials){
        
        $scope.trials =[];
        $scope.init = function(){
            $scope.trials = trials;
        };
        $scope.ok = function () {
            $modalInstance.close();
        };

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.sel = function () {
            alert("hello");
        };
    }


