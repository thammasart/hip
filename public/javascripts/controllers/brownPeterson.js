/**
 * Created by ohmini on 5/8/2557.
 */
angular.module('BrownPetersonApp', [])
    .controller('BrownPetersonQuestionCtrl', function($scope, $http){
        $scope.inProcess = false;
        $scope.questions = [];
        $scope.trigramType = '';
        $scope.trigramLanguage = '';
        $scope.trigramTypes = ['','word','nonsense'];
        $scope.trigramLanguages = ['', 'thai', 'english'];

        $scope.init = function(){
            $scope.inProcess = true;
            $http({method: 'GET', url: 'brownPetersonQuestion'}).success(function (result) {
                $scope.questions = result.questions;
                $scope.inProcess = false;
                console.log($scope.trials);
            }).error(function (result) {
                console.log('error:' + result);
                $scope.inProcess = false;
            });
        }

        $scope.delete = function(question, $index){
            $http({method:'DELETE', url: 'brownPetersonDeleteQuestion', params:{id:question.id}})
                .success(function(result){
                    console.log('success');
                    $scope.questions.splice($index, 1);
                }).error(function(result){
                    console.log('fail');
                });
        }
    });