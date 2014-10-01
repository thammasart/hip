/**
 * Created by ohmini on 5/8/2557.
 */
    ExpApp.controller('BrownPetersonQuestionCtrl', function($scope, $http, $modal, toaster){
        $scope.inProcess = false;
        $scope.questions = [];


        $scope.trigramTypes = [ { name:'--all--', value:''},
                                {name:'word', value: 'word'},
                                {name:'nonsense', value:'nonsense'}];
        $scope.trigramType = $scope.trigramTypes[0];
        $scope.trigramLanguages = [ { name:'--all--', value:''},
                                    {name:'thai', value: 'thai'},
                                    {name:'english', value:'english'}];
        $scope.trigramLanguage = $scope.trigramLanguages[0];

        $scope.init = function(){
            $scope.inProcess = true;
            $http({method: 'GET', url: 'brownPetersonQuestion'}).success(function (result) {
                $scope.questions = result.questions;
                initQuestions();
                calculateShowQuestions();
                $scope.inProcess = false;
            }).error(function (result) {
                console.log('error:' + result);
                $scope.inProcess = false;
            });
        }

        $scope.openDeleteDialog = function(question, $index) {
            var modalInstance = $modal.open({
                templateUrl: 'deleteDialog.html',
                controller: ModalInstanceCtrl,
                size: 'md',
                resolve: {
                    question: function () {
                        return question;
                    },
                    index: function(){
                        return $index;
                    }
                }
            });

            modalInstance.result.then(function (question, index) {
                deleteQuestion(question, index);
            });
        }

        $scope.openDeleteSelectedDialog = function() {
            var modalInstance = $modal.open({
                templateUrl: 'deleteQuestions.html',
                controller: DeleteModalInstanceCtrl,
                size: 'md',
                resolve: {
                    questions: function () {
                        return $scope.questions;
                    }
                }
            });

            modalInstance.result.then(function (questions) {
                if(questions.length > 0){
                    $http({method:'POST', url: 'brownPetersonDeleteQuestions', data:questions})
                        .success(function(result){
                            $scope.questions = result.questions;
                            initQuestions();
                            toaster.pop('success', 'ลบคำถามสำเร็จ!', '', 5000);
                        }).error(function(result){
                            toaster.pop('danger', 'ลบคำถามล้มเหลว!', '', 5000);
                        });
                }else{
                    toaster.pop('warning', 'No records selected !!', '', 5000);
                }
            });
        }

        function initQuestions(){
            for(var i=0; i<$scope.questions.length; i++){
                $scope.questions[i].checked = false;
            }
        }

        function deleteQuestion(question, index){
            $http({method:'DELETE', url: 'brownPetersonDeleteQuestion', params:{id:question.id}})
                .success(function(result){
                    $scope.questions.splice(index, 1);
                    toaster.pop('success', 'ลบคำถามสำเร็จ!', '', 5000);
                }).error(function(result){
                    toaster.pop('warning', 'ลบคำถามล้มเหลว!', '', 5000);
                });
        }

        $scope.refreshPage =function(){
            $scope.currentPage = 1;
            calculateShowQuestions();
        }

        /*-- pagination set up */
          $scope.currentPage = 1;
          $scope.itemsPerPage = 15;
          $scope.showQuestions = [];

          $scope.numPages = function() {
              return Math.ceil($scope.questions.length / $scope.itemsPerPage);
          };
          function calculateShowQuestions() {
              var begin = (($scope.currentPage - 1) * $scope.itemsPerPage);
              var end = begin + $scope.itemsPerPage;

              $scope.showQuestions = $scope.questions.slice(begin, end);
              console.log($scope.showQuestions);
          };
          $scope.showPage = function(page) {
              $scope.currentPage = page;
              calculateShowQuestions();
          }

        $scope.nextPage = function() {
            if($scope.currentPage < $scope.numPages()) {
                $scope.currentPage++;
                calculateShowQuestions();
            }
        }

        $scope.prevPage = function() {
            if($scope.currentPage > 1 ) {
                $scope.currentPage--;
                calculateShowQuestions();
            }
        }
    });

var ModalInstanceCtrl = function ($scope,$modalInstance, question, index) {

    $scope.question = question;

    $scope.ok = function () {
        $modalInstance.close(question, index);
    };

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
};
var DeleteModalInstanceCtrl = function ($scope,$modalInstance, questions) {

    deleteQuestions = [];
    $scope.deleteText = 'คุณต้องการที่จะลบคำถาม ';

    $scope.init = function(){
        var text = '';
        for(var i=0; i<questions.length; i++){
            if(questions[i].checked){
                deleteQuestions.push(questions[i].id);
                text += '#' + questions[i].id + ' ';
            }
        }
        if(deleteQuestions.length == 0){
            $scope.deleteText = 'ไม่มีคำถามที่จะลบ โปรดลองใหม่อีกครั้ง';
        }else{
            $scope.deleteText += text + ' หรือไม่?';
        }
    }

    $scope.ok = function () {
        $modalInstance.close(deleteQuestions);
    };

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
};