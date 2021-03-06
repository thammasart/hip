var ExpApp = angular.module('ExperimentCreator', ['ui.bootstrap','toaster']);
    ExpApp.controller('changeController', function($scope, $rootScope){
        $rootScope.isChange = false;
        $rootScope.isInEditParameter = false;

        $rootScope.changingData = function(){
            $rootScope.isChange = true;
        }
        $scope.click = function(path){
            if($rootScope.isChange && $rootScope.isInEditParameter){
                var confirm = window.confirm("ข้อมูลที่คุณแก้ไขจะไม่ได้รับการบันทึก คุณต้องการออกจากหน้านี้หรือไม่?");
                if(confirm)
                    window.location.assign(path);
            }else if(!$rootScope.isInEditParameter){
                window.location.assign(path);
            }else{
                window.location.assign(path);
            }
        }
    })
    .controller('ExController', function($scope, $rootScope, $http){
        $scope.value = 3;
        $scope.isEditName = false;
        $scope.isEditDate = false;
        $scope.startDateOpened = false;
        $scope.expireDateOpened = false;
        $scope.inProcess = false;
        $scope.today = new Date();
        $scope.dl = new Date();
        $rootScope.exp = {}
        $scope.status = 'CLOSE';
        $rootScope.enableStatusButton = true;

        $scope.init = function() {
            $rootScope.$watch('exp', function () {
                if($rootScope.exp.expireDate) {
                    $scope.expireDate = new Date($rootScope.exp.expireDate);
                }
                if($rootScope.exp.startDate) {
                    $scope.startDate = new Date($rootScope.exp.startDate);
                }
                if($rootScope.exp){
                    $scope.status = $rootScope.exp.status;
                }
            });
            $rootScope.isInEditParameter = true;

        }

        $scope.getInputStatus = function(input){
            if(input.$pristine)
                return 2;
            if(input.$valid)
                return 0;
            return 1;
        };
        $scope.changeStatusComfirm = function(){
            var r = confirm("Are You Comfirm To Change Experiment Status ?");
            if(r == true){
                $scope.changeStatus();
            }
        }
        $scope.changeStatus = function(){
            if($scope.status == 'CLOSE'){
                $scope.status = 'OPEN';
            }else if($scope.status == 'OPEN'){
                $scope.status = 'DISABLED';
            }
            var startDate = new Date($rootScope.exp.startDate).getTime();
            var expireDate = new Date($rootScope.exp.expireDate).getTime();
            $http({method:'PUT',url:'saveExperiment',params:{id:$rootScope.exp.id,name:$rootScope.exp.name, startDate:startDate, expireDate:expireDate, status: $scope.status}})
                .success(function(result){
                    $scope.inProcess = false;
                    $rootScope.exp.status = $scope.status;
                }).error(function(result){
                    $scope.inProcess = false;
                });
        }

        $scope.checkDisabled = function(disabled){
            return disabled ? 'disabled' : '';
        }

        $scope.editName = function(){
            if($scope.isEditName){
                $scope.save('name');
            }

            $scope.isEditName = !$scope.isEditName;

        }

        $scope.textEdit = function(editable){
            return editable ? 'save' : 'edit';
        }

        $scope.save =function(field){
            var name = '', startDate = -1, expireDate = -1;
            if(field == 'name')
                name = $rootScope.exp.name;
            else if(field == 'date') {
                $scope.isEditDate = false;
                startDate = new Date($scope.startDate).getTime();
                expireDate = new Date($scope.expireDate).getTime();
                $rootScope.exp.startDate = $scope.startDate;
                $rootScope.exp.expireDate = $scope.expireDate;
            }

            $http({method:'PUT',url:'saveExperiment',
                params:{id:$rootScope.exp.id,
                    name:name,
                    startDate:startDate,
                    expireDate:expireDate,
                    status:$scope.status}})
                .success(function(result){
                    $scope.inProcess = false;
                }).error(function(result){
                    $scope.inProcess = false;
                });
        }

        $rootScope.exp = {};

        $scope.showWeeks = true;
        $scope.toggleWeeks = function () {
            $scope.showWeeks = ! $scope.showWeeks;
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

            if(target === 0 ) {
                $scope.startDateOpened = true;
                $scope.isEditDate = true;
            }else if(target === 1) {
                $scope.expireDateOpened = true;
                $scope.isEditDate = true;
            }
        };

        $scope.dateOptions = {
            formatYear: 'yy',
            startingDay: 1
        };

        $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'shortDate', 'dd-MM-yyyy'];
        $scope.format = $scope.formats[3];

    })
    .controller('BrownPetersonCtrl', function($scope, $rootScope, $http, toaster, $modal){
        $scope.regIneger = /^(0|[1-9][0-9]*)$/;
        $scope.trigramTypes = ['word', 'nonsense'];
        $scope.trigramLanguages = ['english','thai'];
        $scope.trials = [];
        $scope.nonsenseThai = [];
        $scope.nonsenseEnglish = [];
        $scope.wordThai = [];
        $scope.wordEnglish = [];
        $scope.inProcess = false;

        $rootScope.isChange = false;

        $scope.init = function(expId) {
            $scope.inProcess = true;
            $http({method: 'GET', url: 'brownPetersonInit', params: {expId: expId}}).success(function (result) {
                $scope.trials = result.trials;
                $rootScope.exp = angular.copy($scope.trials[0].schedule);
                $scope.nonsenseThai = result.nonsenseThai;
                $scope.nonsenseEnglish = result.nonsenseEng;
                $scope.wordThai = result.wordThai;
                $scope.wordEnglish = result.wordEnglish;
                $scope.inProcess = false;
            }).error(function (result) {
                $scope.inProcess = false;
            });
        }

        $scope.click = function(path){
            if($rootScope.isChange){
                var confirm = window.confirm("ข้อมูลที่คุณแก้ไขจะไม่ได้รับการบันทึก คุณต้องการออกจากหน้านี้หรือไม่?");
                if(confirm)
                    window.location.assign(path);
            }else{
                window.location.assign(path);
            }
        }

        $scope.refresh = function(trial){
            $rootScope.isChange = true;
            for(var i=0; i<trial.quizzes.length; i++){
                trial.quizzes[i].question = findQuestion(trial.trigramType, trial.trigramLanguage);
            }
        }
        $scope.randomQuestion = function(quiz, trigramType, trigramLanguage){
            $scope.change();
            quiz.question = findQuestion(trigramType, trigramLanguage);
        }

        $scope.change = function(){
            $rootScope.isChange = true;
        }

        $scope.saveAll = function(){
            $scope.inProcess = true;
            $http({method:'PUT',url:'saveBrownPetersonTrials',data:$scope.trials})
                .success(function(result){
                    $scope.inProcess = false;
                    $rootScope.isChange = false;
                    toaster.pop('success', 'บันทึกข้อมูลสำเร็จ!', '', 5000);
                }).error(function(result){
                    $scope.inProcess = false;
                    toaster.pop('warning', 'บันทึกข้อมูลล้มเหลว!', '', 5000);
                });
        }
        function findQuestion(trigramType, trigramLanguage){
            if(trigramType == 'word'){
                if(trigramLanguage == 'english'){
                    return $scope.wordEnglish[Math.floor(Math.random() * $scope.wordEnglish.length)];
                }else{
                    return $scope.wordThai[Math.floor(Math.random() * $scope.wordThai.length)];
                }
            }else{
                if(trigramLanguage == 'english'){
                    return $scope.nonsenseEnglish[Math.floor(Math.random() * $scope.nonsenseEnglish.length)];
                }else{
                    return $scope.nonsenseThai[Math.floor(Math.random() * $scope.nonsenseThai.length)];
                }
            }
        }

        $scope.openTutorial = function(){
            $modal.open({
                templateUrl: 'tutorial.html',
                controller: TutorialModalInstanceCtrl,
            });
        };

    })
    .controller('AttentionBlinkCtrl', function($scope, $rootScope, $http, toaster, $modal){
        $scope.trials = [];
        $scope.formError = [];
            $scope.tempTrials = [];
        $scope.word = /^[0-9]*\.?[0-9]+$/;
            $scope.single = /^[a-zA-Z0-9ก-ฮ]{1}$/;
            $scope.regIneger = /^(0|[1-9][0-9]*)$/;
            $scope.floatPattern = /^(?:10(?:\.0+)?|[1-9](?:\.[0-9]+)?|0?\.[1-9]+|0?\.0[1-9]+)$/;
        $scope.inProcess = true;
        $rootScope.isChange = false;
        $scope.questionTypes = ['THAI','ENGLISH','NUMBER'];

        $scope.init = function(expId) {
            $http({method: 'GET', url: 'attentionBlinkInit', params: {expId: expId}}).success(function (result) {
                $scope.trials = result.trials;
                $scope.tempTrials = angular.copy(result.trials);
                initTempTrial();
                $rootScope.exp = $scope.trials[0].schedule;
                validateParameter();
                $scope.inProcess = false;
            }).error(function (result) {
                $scope.inProcess = false;
            });
        }

            function initTempTrial(){
                for(var i=0; i<$scope.tempTrials.length; i++){
                    $scope.formError.push(false);
                    for(var j=0; j<$scope.tempTrials[i].quizzes.length; j++){
                        $scope.tempTrials[i].quizzes[j] = initLetters($scope.tempTrials[i].quizzes[j]);
                    }
                }
            }
            function initLetters(quiz){
                quiz.letters = quiz.question.set.split("");
                quiz.targets = quiz.question.letter.split("");
                if(quiz.letters.length < 20){
                    for(var i=quiz.letters.length; i< 20; i++){
                        quiz.letters.push("");
                    }
                }
                if(quiz.targets.length < 3){
                    for(var i=quiz.targets.length; i< 3; i++){
                        quiz.letters.push("");
                    }
                }
                return quiz;
            }

        $scope.change = function(){
            $rootScope.isChange = true;
            validateParameter();
        }

        $scope.resetParameter = function(quiz){
            for(var i=0; i<quiz.letters.length; i++){
                quiz.letters[i] = '';
            }
            for(var i=0; i<quiz.targets.length; i++){
                quiz.targets[i] = '';
            }
            quiz.isCorrect = false;
            quiz.blinkTime = 0.1;
        }

        function validateParameter(){

            for(var i=0; i<$scope.tempTrials.length; i++){
                var validate = true;
                for(var j=0; j < $scope.tempTrials[i].quizzes.length ; j++){
                    var quiz = $scope.tempTrials[i].quizzes[j];
                    validate = validateLetterAndTarget(quiz);
                    if(!validate){
                        $scope.formError[i] = true;
                        $rootScope.enableStatusButton = false;
                        break;
                    }
                }
                if(validate){
                    $scope.formError[i] = false;
                }
            }

            var validate = true;
            for(var i=0; i<$scope.formError.length; i++){
                if($scope.formError[i]){
                    validate = false;
                }
            }
            if(validate){
                $rootScope.enableStatusButton = true;
            }

        }

        function validateLetterAndTarget(quiz){
            var validate = isLettersCorrect(quiz);
            if(!validate){
                return false;
            }else{
                if(!isTargetCorrect(quiz)){
                    return false;
                }else{
                    return true;
                }
            }
        }

        function isLettersCorrect(quiz){
            for(var i=0; i<6; i++){
                if(!quiz.letters[i] || quiz.letters[i] === ''){
                    return false;
                }
            }
            for(var i=6; i<19; i++){
                if(!quiz.letters[i] || quiz.letters[i] === ''){
                    for(var j=i+1;j<20;j++){
                        if(quiz.letters[j]){
                            return false;
                        }
                    }
                }
            }
            return true;
        }


        function isTargetCorrect(quiz){
            if(!quiz.targets[0] || quiz.targets[0] === ''){
                return false;
            }
            if(quiz.targets[0] && quiz.targets[2]){
                if(!quiz.targets[1]){
                    return false;
                }
            }
            return true;
        }

        $scope.generateQuestion = function(quiz, questionType){
            $scope.change();
            var ENGLISH_CASE = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
            var THAI_CASE = "กขคฆงจฉชฌญฎฐฒณดถทธนบผพภมยรลวศษสหฬอ";
            var NUMBER_CASE = '0123456789';
            if(questionType == 'THAI'){
                quiz.question.questionType = 'THAI';
                generateTextQuestion(quiz, THAI_CASE);
            }else if(questionType == 'ENGLISH'){
                quiz.question.questionType = 'ENGLISH';
                generateTextQuestion(quiz, ENGLISH_CASE);
            }else if(questionType == 'NUMBER'){
                quiz.question.questionType = 'NUMBER';
                generateTextQuestion(quiz, NUMBER_CASE);
            }
        }

        $scope.saveAll = function(){
            $scope.inProcess = true;
            generateTrials();
            $http({method:'PUT',url:'saveAttentionBlinkTrials',data:$scope.trials})
                .success(function(result){
                    $scope.inProcess = false;
                    $rootScope.isChange = false;
                    toaster.pop('success', 'บันทึกข้อมูลสำเร็จ!', '', 5000);
                }).error(function(result){
                    $scope.inProcess = false;
                    toaster.pop('warning', 'บันทึกข้อมูลล้มเหลว!', '', 5000);
                });


        }
        function generateTrials(){
            for(var i=0; i<$scope.tempTrials.length; i++){
                generateQuizzes($scope.trials[i].quizzes, $scope.tempTrials[i].quizzes);
            }
        }

        function generateQuizzes(quizzes, newQuizzes){
            for(var i=0; i<quizzes.length; i++){
                quizzes[i].blinkTime = newQuizzes[i].blinkTime;
                quizzes[i].isCorrect = newQuizzes[i].isCorrect;
                quizzes[i].length = calculateLettersLength(newQuizzes[i]);
                quizzes[i].numberOfTarget = calculateNumberOfTarget(newQuizzes[i]);
                generateQuestion(quizzes[i].question, newQuizzes[i]);
            }
        }

        function calculateLettersLength(quiz){
            for(var i=6; i<quiz.letters.length; i++){
                if(!quiz.letters[i] || quiz.letters[i] === ''){
                    return i;
                }
            }
            return 20;
        }
        function calculateNumberOfTarget(quiz){
            if(quiz.targets[2]){
                return 3;
            }else if(quiz.targets[1]){
                return 2;
            }
            return 1;
        }
        function generateQuestion(question, quiz){
            question.correctAnswer = quiz.isCorrect;
            console.log(question);
            console.log(quiz);
            var letter = quiz.targets[0];
            if(quiz.targets[1]){
                letter += quiz.targets[1];
            }
            if(quiz.targets[2]){
                letter += quiz.targets[2];
            }
            question.letter = letter;

            var set = "";
            for(var i=0; i<quiz.letters.length; i++){
                if(quiz.letters[i]){
                    set += quiz.letters[i];
                }
            }
            question.set = set;

        }

        function generateTextQuestion(quiz, CASE){

            var text = generateText(CASE,quiz.numberOfTarget);
            quiz.question.letter = text;
            if(quiz.isCorrect){
                text = generateText(CASE,quiz.length);
                var start = Math.floor(Math.random() * (quiz.length - quiz.numberOfTarget));
                var sub = text.substr(start, quiz.numberOfTarget);
                quiz.question.set = text.replace(sub, quiz.question.letter);
            }else{
                text = generateText(CASE,quiz.length);
                while(text.search(quiz.question.letter) != -1)
                    text = generateText(CASE,quiz.length);
                quiz.question.set = text;
            }
        }

        function generateText(CASE, length){
            var text = '';
            for(var i=0; i<length; i++){
                text += CASE.charAt(Math.floor(Math.random() * CASE.length));
            }
            return text;
        }
        $scope.openTutorial = function(){
            $modal.open({
                templateUrl: 'tutorial.html',
                controller: TutorialModalInstanceCtrl,
            });
        };
        $scope.openResetPopup = function(quiz){
            var modalInstance = $modal.open({
                templateUrl: 'resetAttentionBlinkValues.html',
                controller: ResetAttentionBlinkValuesModalInstanceCtrl,
            });

            modalInstance.result.then(function (result){
               $scope.resetParameter(quiz);
               $scope.change();
            });
        };
    })
    .controller('SignalDetectionCtrl', function($scope, $rootScope, $http, toaster, $modal){
        $scope.single = /^[a-zA-Z0-9ก-ฮ]{1}$/;
        $scope.regIneger = /^(0|[1-9][0-9]*)$/;
        $scope.floatPattern = /^(?!0*(\.0+)?$)(\d+|\d*\.\d+)$/;
        $rootScope.exp = {};
        $scope.trials = [];
        $scope.inProcess = false;
        $rootScope.isChange = false;

        $scope.init = function(expId) {
            $http({method: 'GET', url: 'signalDetectionInit', params: {expId: expId}}).success(function (result) {
                $scope.trials = result.trials;
                $rootScope.exp = $scope.trials[0].schedule;
                $scope.inProcess = false;
            }).error(function (result) {
                $scope.inProcess = false;
            });
        }

        $scope.change = function(){
            $rootScope.isChange = true;
        }

        $scope.saveAll = function(){
            $scope.inProcess = true;
            $http({method:'PUT',url:'saveSignalDetectionTrials',data:$scope.trials})
                .success(function(result){
                    $scope.inProcess = false;
                    $rootScope.isChange = false;
                    toaster.pop('success', 'บันทึกข้อมูลสำเร็จ!', '', 5000);
                }).error(function(result){
                    $scope.inProcess = false;
                    toaster.pop('warning', 'บันทึกข้อมูลล้มเหลว!', '', 5000);
                });
        }
        $scope.openTutorial = function(){
            $modal.open({
                templateUrl: 'tutorial.html',
                controller: TutorialModalInstanceCtrl,
            });
        };
    })
    .controller('StroofEffectCtrl', function($scope, $rootScope, $http, toaster, $modal){
        $scope.regIneger = /^(0|[1-9][0-9]*)$/;
        $scope.questionTypes = ['THAI', 'ENGLISH'];
        $scope.trials = [];
        $scope.inProcess = false;

        var matchEnglish = [];
        var matchThai = [];
        var notMatchEnglish = [];
        var notMatchThai = [];

        $scope.init = function(expId) {
            $scope.inProcess = true;
            $http({method: 'GET', url: 'stroofEffectInit', params: {expId: expId}}).success(function (result) {
                $scope.trials = result.trials;
                $rootScope.exp = $scope.trials[0].schedule;
                initQuestion(result.questions);
                $scope.inProcess = false;
            }).error(function (result) {
                $scope.inProcess = false;
            });
        }

        $scope.randomQuestion = function(quiz, questionType){
            if(questionType == 'ENGLISH'){
                if(quiz.question.match){
                    quiz.question = angular.copy(matchEnglish[Math.floor(Math.random() * matchEnglish.length)]);
                }else{
                    quiz.question = angular.copy(notMatchEnglish[Math.floor(Math.random() * matchEnglish.length)]);
                }
            }else{
                if(quiz.question.match){
                    quiz.question = angular.copy(matchThai[Math.floor(Math.random() * matchEnglish.length)]);
                }else{
                    quiz.question = angular.copy(notMatchThai[Math.floor(Math.random() * matchEnglish.length)]);
                }
            }
        }

        $scope.refresh = function(trial){
            for(var i=0; i<trial.quizzes.length; i++){
                $scope.randomQuestion(trial.quizzes[i], trial.questionType);
            }
        }

        $scope.saveAll = function(){
            $scope.inProcess = true;
            resetQuestion();
            $http({method:'PUT',url:'saveStroofEffectTrials',data:$scope.trials})
                .success(function(result){
                    $scope.inProcess = false;
                    $rootScope.isChange = false;
                    toaster.pop('success', 'บันทึกข้อมูลสำเร็จ!', '', 5000);

                }).error(function(result){
                    $scope.inProcess = false;
                    toaster.pop('warning', 'บันทึกข้อมูลล้มเหลว!', '', 5000);
                });
        }

        $scope.changeMatch = function(quiz, questionType){
            if(quiz.question.match){
               if(questionType == 'THAI'){
                    assignQuestion(quiz, matchThai);
               }else{
                    assignQuestion(quiz, matchEnglish);
               }
            }else{
               if(questionType == 'THAI'){
                   assignQuestion(quiz, notMatchThai);
              }else{
                   assignQuestion(quiz, notMatchEnglish);
              }
            }
        }

        function assignQuestion(quiz, questions){
            for(var i=0; i<questions.length; i++){
               if(quiz.question.colorWord == questions[i].colorWord){
                   quiz.question = questions[i];
               }
            }
        }

        function resetQuestion(){
            for(var i=0; i<$scope.trials.length; i++){
                for(var j=0; j<$scope.trials[i].quizzes.length; j++){
                    $scope.trials[i].quizzes[j].question = getNewQuestion($scope.trials[i].quizzes[j].question);
                }
            }
        }

        function getNewQuestion(question){
            return {
                id:question.id,
                colorWord:question.colorWord,
                inkColor:question.inkColor,
                questionType: question.questionType
            }
        }

        function initQuestion(questions){
            for(var i=0; i<questions.length; i++){
                if(questions[i].questionType == 'ENGLISH'){
                    if(questions[i].match){
                        matchEnglish.push(questions[i]);
                    }else{
                        notMatchEnglish.push(questions[i])
                    }
                }else{
                    if(questions[i].match){
                        matchThai.push(questions[i]);
                    }else{
                        notMatchThai.push(questions[i])
                    }
                }
            }

        }
        $scope.openTutorial = function(){
            $modal.open({
                templateUrl: 'tutorial.html',
                controller: TutorialModalInstanceCtrl,
            });
        };
    })
    .controller('PositionErrorCtrl', function($scope, $rootScope, $http, toaster, $modal){
        $scope.word = /^(?!0*(\.0+)?$)([0-5]|[0-4]\.\d+)$/;
        $scope.memorySet = [ 3, 5, 7, 10, 12];
        $scope.questionTypes = ['THAI', 'ENGLISH', 'NUMBER'];
        $scope.trials = [];
        $scope.inProcess = false;
        $scope.isEditable = [];

        $rootScope.isChange = false;

        $scope.init = function(expId) {
            $scope.inProcess = true;
            $http({method: 'GET', url: 'PositionErrorInit', params: {expId: expId}}).success(function (result) {
                $scope.trials = result.trials;
                $rootScope.exp = $scope.trials[0].schedule;
                initEditable();
                $scope.inProcess = false;
            }).error(function (result) {
                $scope.inProcess = false;
            });
        }

        $scope.change = function(){
            $rootScope.isChange = true;
        }

        function initEditable(){
            for(var i=0; i<$scope.trials.length; i++){
                $scope.isEditable.push([]);
                for(var j=0; j<$scope.trials[i].quizzes.length; j++){
                    $scope.isEditable[i].push(false);
                }
            }
        }

        $scope.edit = function(i, j){
            $scope.isEditable[i][j] = !$scope.isEditable[i][j];
        }

        $scope.editText = function(i, j){
            if($scope.isEditable[i][j]){
                return 'ok';
            }
            return 'edit';
        }

        $scope.generateQuestion = function(quiz, questionType){
            var ENGLISH_CASE = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
            var THAI_CASE = "กขคฆงจฉชฌญฎฐฒณดถทธนบผพภมยรลวศษสหฬอ";
            var NUMBER_CASE = '0123456789';

            if(questionType == 'THAI'){
                quiz.question.questionType = 'THAI';
                generateText(quiz, THAI_CASE);
            }else if(questionType == 'ENGLISH'){
                quiz.question.questionType = 'ENGLISH';
                generateText(quiz, ENGLISH_CASE);
            }else{
                quiz.question.questionType = 'NUMBER';
                generateText(quiz, NUMBER_CASE);
            }
        }

        $scope.saveAll = function(){
            $scope.inProcess = true;
            $http({method:'PUT',url:'savePositionErrorTrials',data:$scope.trials})
                .success(function(result){
                    $scope.inProcess = false;
                    $rootScope.isChange = false;
                    toaster.pop('success', 'บันทึกข้อมูลสำเร็จ!', '', 5000);
                }).error(function(result){
                    $scope.inProcess = false;
                    toaster.pop('warning', 'บันทึกข้อมูลล้มเหลว!', '', 5000);
                });
        }

        function generateText(quiz, CASE){
            var text = '';
            for(var i=0; i<quiz.length; i++){
                text += CASE.charAt(Math.floor(Math.random() * CASE.length));
            }
            quiz.question.memorySet = text;
        }

        $scope.refresh = function(trial){
            for(var i=0; i<trial.quizzes.length; i++){
                $scope.generateQuestion(trial.quizzes[i], trial.questionType);
            }
        }
        $scope.openTutorial = function(){
            $modal.open({
                templateUrl: 'tutorial.html',
                controller: TutorialModalInstanceCtrl,
            });
        };

    })
    .controller('SternbergSearchCtrl', function($scope, $rootScope, $http, toaster, $modal){
        $scope.word = /^(?!0*(\.0+)?$)(\d+|\d*\.\d+)$/;
        $scope.showQuiz = true;
        var ENGLISH_CASE = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
        var THAI_CASE = "กขคฆงจฉชฌญฎฐฒณดถทธนบผพภมยรลวศษสหฬอ";
        var NUMBER_CASE = '0123456789';
        $scope.questionTypes = ['THAI', 'ENGLISH', 'NUMBER'];
        $scope.trials = [];
        $scope.inProcess = false;
        $scope.regIneger = /^(0|[1-9][0-9]*)$/;
        $scope.isEditable = [];

        $scope.init = function(expId) {
            $scope.inProcess = true;
            $http({method: 'GET', url: 'sternbergInit', params: {expId: expId}}).success(function (result) {
                $scope.trials = result.trials;
                $rootScope.exp = $scope.trials[0].schedule;
                initEditable();
                $scope.inProcess = false;
            }).error(function (result) {
                $scope.inProcess = false;
            });
        }

        function initEditable(){
            for(var i=0; i<$scope.trials.length; i++){
                $scope.isEditable.push(false);
            }
        }

        $scope.edit = function(index, trial){
            if($scope.isEditable[index]){
                generateQuiz(trial);
            }
            $scope.isEditable[index] = !$scope.isEditable[index];
        }

        $scope.editText = function(index){
            return $scope.isEditable[index] ? 'ok' : 'edit';
        }

        $scope.generateQuestion = function(trial){

            if(trial.questionType == 'THAI'){
                trial.quizzes[0].question.questionType = 'THAI';
                generateText(trial, THAI_CASE);
            }else if(trial.questionType == 'ENGLISH'){
                trial.quizzes[0].question.questionType = 'ENGLISH';
                generateText(trial, ENGLISH_CASE);
            }else{
                trial.quizzes[0].question.questionType = 'NUMBER';
                generateText(trial, NUMBER_CASE);
            }

            generateQuiz(trial);
        }

        function generateText(trial, CASE){
            var charSet = generateCharSet(CASE);
            var temp = angular.copy(charSet);
            var text = '';
            for(var i=0; i<trial.length; i++){
                if(temp.length == 0){
                    temp = angular.copy(charSet);
                }
                var index = Math.floor(Math.random() * temp.length);
                text += temp[index];
                temp.splice(index, 1);
            }
            trial.quizzes[0].question.memorySet = text;
        }

        function generateQuiz(trial){
            var question = trial.quizzes[0].question;
            trial.quizzes = [];
            generateOneCharCorrectQuiz(trial, question);
            generateTwoCharCorrectQuiz(trial, question);
            if(trial.questionType == 'THAI'){
                generateOneCharInCorrectQuiz(trial, question, THAI_CASE);
                generateTwoCharInCorrectQuiz(trial, question, THAI_CASE);
            }else if(trial.questionType == 'ENGLISH'){
                generateOneCharInCorrectQuiz(trial, question, ENGLISH_CASE);
                generateTwoCharInCorrectQuiz(trial, question, ENGLISH_CASE);
            }else{
                generateOneCharInCorrectQuiz(trial, question, NUMBER_CASE);
                generateTwoCharInCorrectQuiz(trial, question, NUMBER_CASE);
            }

            trial.quizzes = shuffle(trial.quizzes);
        }

        function generateTwoCharInCorrectQuiz(trial, question, CASE){
            if(trial.length < 2)
                return;

            var quizSet = generateCharSetExceptMemorySet(CASE, question.memorySet);
            var temp = angular.copy(quizSet);
            for(var i=0; i<trial.twoCharIsInCorrect; i++){
                var text = question.memorySet.charAt(Math.floor(Math.random() * (question.memorySet.length - 1) ));
                var index = Math.floor(Math.random() * temp.length);
                text += temp[index];
                temp.splice(index, 1);
                trial.quizzes.push(Quiz(text, false, question));
            }
        }

        function generateCharSetExceptMemorySet(CASE, memorySet){
            var quizSet = [];
            for(var i=0; i<CASE.length; i++){
                var char = CASE.charAt(i);
                if(memorySet.search(char) == -1)
                    quizSet.push(CASE.charAt(i));
            }
            return quizSet;
        }

        function generateTwoCharCorrectQuiz(trial, question){
            if(trial.length < 2)
                return;
            var temp = [];
            for(var i=0; i<trial.twoCharIsCorrect; i++){
                var text = generateTwoChar(temp, question.memorySet);
                var quiz = Quiz(text, true, question);
                trial.quizzes.push(quiz);
            }
        }

        function generateTwoChar(array, memorySet){
            var index = Math.floor(Math.random() * (memorySet.length - 1));
            var text = '';
            text += memorySet.charAt(index);
            text += memorySet.charAt(index + 1);
            if(duplicateQuestion(array, text)){
                text = generateTwoChar(array, memorySet);
            }else{
                array.push(text);
            }
            return text;
        }
        function duplicateQuestion(array, text){
            for(var i=0; i<array.length; i++){
                if(array[i] == text)
                    return true;
            }
            return false;
        }

        function generateOneCharCorrectQuiz(trial, question){
            var quizSet = generateCharSet(question.memorySet);
            var temp = angular.copy(quizSet);
            for(var i=0; i<trial.oneCharIsCorrect; i++){
                if(temp.length == 0){
                    temp = angular.copy(quizSet);
                }
                var index = Math.floor(Math.random() * temp.length);
                var quiz = Quiz(temp[index], true, question);
                temp.splice(index, 1);
                trial.quizzes.push(quiz);
            }
        }

        function generateOneCharInCorrectQuiz(trial, question, CASE){
            var quizSet = generateCharSet(CASE);
            var temp = angular.copy(quizSet);
            for(var i=0; i<trial.oneCharIsInCorrect; i++){
                if(temp.length == 0){
                    temp = angular.copy(quizSet);
                }
                var questionChar = generateChar(temp, question.memorySet);
                var quiz = Quiz(questionChar, false, question);
                trial.quizzes.push(quiz);
            }

        }

        function generateChar(quizSet, memorySet){
            var index = Math.floor(Math.random() * quizSet.length);
            var questionChar = quizSet[index];
            if(memorySet.search(questionChar) > -1){
                questionChar = generateChar(quizSet, memorySet);
            }else{
                quizSet.splice(index, 1);
            }

            return questionChar;
        }

        function generateCharSet(CASE){
            var quizSet = [];
            for(var i=0; i<CASE.length; i++){
                quizSet.push(CASE.charAt(i));
            }
            return quizSet;
        }

        function Quiz(questionChar, isTrue, question){
            return {
                questionChar: questionChar,
                isTrue: isTrue,
                question: question
            }
        }

        $scope.getQuestion = function(questionChar){
            if(questionChar.length <= 1){
                return questionChar;
            }
            var text = questionChar.charAt(0);
            text += ' และ ';
            text += questionChar.charAt(1);
            return text;
        }
        $scope.saveAll = function(){
            $scope.inProcess = true;
            $http({method:'PUT',url:'saveSternBergTrials',data:$scope.trials})
                .success(function(result){
                    $scope.inProcess = false;
                    $rootScope.isChange = false;
                    toaster.pop('success', 'บันทึกข้อมูลสำเร็จ!', '', 5000);
                }).error(function(result){
                    $scope.inProcess = false;
                    toaster.pop('warning', 'บันทึกข้อมูลล้มเหลว!', '', 5000);
                });
        }

        function shuffle(array) {
            var currentIndex = array.length
                , temporaryValue
                , randomIndex
                ;

            // While there remain elements to shuffle...
            while (0 !== currentIndex) {

                // Pick a remaining element...
                randomIndex = Math.floor(Math.random() * currentIndex);
                currentIndex -= 1;

                // And swap it with the current element.
                temporaryValue = array[currentIndex];
                array[currentIndex] = array[randomIndex];
                array[randomIndex] = temporaryValue;
            }

            return array;
        }
        $scope.openTutorial = function(){
            $modal.open({
                templateUrl: 'tutorial.html',
                controller: TutorialModalInstanceCtrl,
            });
        };
    })
    .controller('MagicNumber7Ctrl', function($scope, $rootScope, $http, toaster, $modal){
        $scope.word = /^(?!0*(\.0+)?$)(\d|\d\.\d+|10)$/;
        $scope.questionTypes = ['THAI', 'ENGLISH', 'NUMBER'];
        $scope.trials = [];
        $scope.inProcess = false;
        $scope.regIneger = /^(0|[1-9][0-9]*)$/;
        $scope.isEditable = [];

        $scope.init = function(expId) {
            $scope.inProcess = true;
            $http({method: 'GET', url: 'MagicSevenInit', params: {expId: expId}}).success(function (result) {
                $scope.trials = result.trials;
                $rootScope.exp = $scope.trials[0].schedule;
                initEditable();
                $scope.inProcess = false;
            }).error(function (result) {
                $scope.inProcess = false;
            });
        }

        function initEditable(){
            for(var i=0; i<$scope.trials.length; i++){
                $scope.isEditable.push([]);
                for(var j=0; j<$scope.trials[i].quizzes.length; j++){
                    $scope.isEditable[i].push(false);
                }
            }
        }

        $scope.edit = function(i, j){
            $scope.isEditable[i][j] = !$scope.isEditable[i][j];
        }

        $scope.editText = function(i, j){
            if($scope.isEditable[i][j]){
                return 'ok';
            }
            return 'edit';
        }

        $scope.generateQuestion = function(quiz, questionType){
            var ENGLISH_CASE = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
            var THAI_CASE = "กขคฆงจฉชฌญฎฐฒณดถทธนบผพภมยรลวศษสหฬอ";
            var NUMBER_CASE = '0123456789';

            if(questionType == 'THAI'){
                quiz.question.questionType = 'THAI';
                generateText(quiz, THAI_CASE);
            }else if(questionType == 'ENGLISH'){
                quiz.question.questionType = 'ENGLISH';
                generateText(quiz, ENGLISH_CASE);
            }else{
                quiz.question.questionType = 'NUMBER';
                generateText(quiz, NUMBER_CASE);
            }
        }

        $scope.saveAll = function(){
            $scope.inProcess = true;
            $http({method:'PUT',url:'saveMagicSevenTrials',data:$scope.trials})
                .success(function(result){
                    $scope.inProcess = false;
                    $rootScope.isChange = false;
                    toaster.pop('success', 'บันทึกข้อมูลสำเร็จ!', '', 5000);
                }).error(function(result){
                    $scope.inProcess = false;
                    toaster.pop('warning', 'บันทึกข้อมูลล้มเหลว!', '', 5000);
                });
        }

        function generateText(quiz, CASE){
            var text = '';
            for(var i=0; i<quiz.length; i++){
                text += CASE.charAt(Math.floor(Math.random() * CASE.length));
            }
            quiz.question.memorySet = text;
        }

        $scope.refresh = function(trial){
            for(var i=0; i<trial.quizzes.length; i++){
                $scope.generateQuestion(trial.quizzes[i], trial.questionType);
            }
        }
        $scope.openTutorial = function(){
            $modal.open({
                templateUrl: 'tutorial.html',
                controller: TutorialModalInstanceCtrl,
            });
        };
    })
    .controller('SimonEffectCtrl', function($scope, $rootScope, $http, toaster, $modal){
        $scope.trials = [];
        $scope.floatPattern = /^[0-1]*\.?[0-9]+$/;

        $scope.questionTypes = ['ONEFEATURE','TWOFEATURE'];
        $scope.positions = ['up', 'down', 'right', 'left'];
        $scope.trials = [];
        var questions = [];
        var oneFeatureQuestion = [];

        $scope.inProcess = false;

        $scope.init = function(expId) {
            $scope.inProcess = true;
            $http({method: 'GET', url: 'SimonEffectInit', params: {expId: expId}}).success(function (result) {
                $scope.trials = result.trials;
                $rootScope.exp = $scope.trials[0].schedule;
                questions = result.questions;
                initQuestion();
                $scope.inProcess = false;
            }).error(function (result) {
                $scope.inProcess = false;
            });
        }

        $scope.randomQuestion = function(quiz, questionType){
            if(questionType == 'ONEFEATURE'){
                quiz.question = oneFeatureQuestion[Math.floor(Math.random() * oneFeatureQuestion.length)];
            }else{
                quiz.question = questions[Math.floor(Math.random() * questions.length)];
            }
            quiz.position = $scope.positions[Math.floor(Math.random() * $scope.positions.length)];
        }

        $scope.refresh = function(trial){
            for(var i=0; i<trial.quizzes.length; i++){
                $scope.randomQuestion(trial.quizzes[i], trial.questionType);
            }
        }

        $scope.saveAll = function(){
            $scope.inProcess = true;
            $http({method:'PUT',url:'saveSimonEffectTrials',data:$scope.trials})
                .success(function(result){
                    toaster.pop('success', 'บันทึกข้อมูลสำเร็จ!', '', 5000);
                    $rootScope.isChange = false;
                    $scope.inProcess = false;
                }).error(function(result){
                toaster.pop('warning', 'บันทึกข้อมูลล้มเหลว!', '', 5000);
                    $scope.inProcess = false;
                });
        }

        function initQuestion(){
            for(var i=0; i<questions.length; i++){
                if(questions[i].color == 'red' && questions[i].alphabet == 'X'||
                    questions[i].color == 'green' && questions[i].alphabet == 'O'){
                    oneFeatureQuestion.push(questions[i]);
                }
            }
        }
        $scope.openTutorial = function(){
            $modal.open({
                templateUrl: 'tutorial.html',
                controller: TutorialModalInstanceCtrl,
            });
        };
    })
    .controller('VisualSearchCtrl', function($scope, $rootScope, $modal, $http, toaster){

        $scope.inProcess = false;
        $scope.overNoise = false;
        $scope.regIneger = /^(0|[1-9][0-9]*)$/;
        $scope.trials = [];

        $scope.init = function(expId){
            $scope.inProcess = true;
            $http({method:'GET',url:'init',params:{exp_id:expId}}).success(function(result){
                $scope.trials = result.trials;
                $rootScope.exp = $scope.trials[0].schedule;
                $scope.inProcess = false;
            }).error(function(result){
                $scope.inProcess = false;
            });
        }

        var isSaveSuccess = true;
        function save(trial, index, length){
            $scope.inProcess = true;
            $http({method:'POST',url:'saveVisualSearch',data:trial.quiz.question.sharps,
                params:{trialId:trial.id,
                circleGreen:trial.quiz.circleGreen, circleRed:trial.quiz.circleRed,
                circleBlue: trial.quiz.circleBlue,
                squareBlue:trial.quiz.squareBlue, squareRed:trial.quiz.squareRed,
                squareGreen:trial.quiz.squareGreen,
                positionXofTarget: trial.quiz.positionXofTarget, 
                positionYofTarget: trial.quiz.positionYofTarget,
                frameSize: trial.quiz.frameSize, target: trial.quiz.target}})
            .success(function(result){
                if(isSaveSuccess && index == length - 1){
                    toaster.pop('success', 'บันทึกข้อมูลสำเร็จ!', '', 5000);
                    $scope.inProcess = false;
                    $rootScope.isChange = false;
                }
            }).error(function(result){
                isSaveSuccess = false;
                if(!isSaveSuccess && index == length - 1){
                    toaster.pop('warning', 'บันทึกข้อมูลล้มเหลว!', '', 5000);
                    isSaveSuccess = true;
                    $scope.inProcess = false;
                }
            });
        }

        $scope.saveAll = function(){
            for(var i=0; i<$scope.trials.length; i++){
                save($scope.trials[i], i, $scope.trials.length);
            }
        }

        $scope.frameSizes = ['SMALLER', 'SMALL', 'MEDIUM', 'BIG', 'EXTRA'];
        $scope.targets = ['SQAURE_GREEN', 'SQAURE_BLUE', 'SQAURE_RED', 
        'CIRCLE_GREEN', 'CIRCLE_BLUE', 'CIRCLE_RED'];

        $scope.changeTargetValue = function(trial){
            if(trial.quiz.target == 'SQAURE_GREEN')
                trial.quiz.squareGreen = 1;
            else if(trial.quiz.target == 'SQAURE_BLUE')
                trial.quiz.squareBlue = 1;
            else if(trial.quiz.target == 'SQAURE_RED')
                trial.quiz.squareRed = 1;
            else if (trial.quiz.target == 'CIRCLE_RED')
                trial.quiz.circleRed = 1;
            else if (trial.quiz.target == 'CIRCLE_BLUE')
                trial.quiz.circleBlue = 1;
            else if (trial.quiz.target == 'CIRCLE_GREEN')
                trial.quiz.circleGreen = 1;
        }

        $scope.open = function(trial){
            var modalInstance = $modal.open({
                templateUrl: 'preview.html',
                controller: VisualSearchModalInstanceCtrl,
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

            modalInstance.result.then(function (result){
               trial.quiz.positionYofTarget = result.top;
               trial.quiz.positionXofTarget = result.left;
               $rootScope.changingData();
            });

        };

        $scope.max = function(trial){
            var frameSize = trial.quiz.frameSize;
            if(frameSize == 'SMALLER')
                return 30;
            if(frameSize == 'SMALL')
                return 50;
            if(frameSize == 'MEDIUM')
                return 90;
            if(frameSize == 'BIG')
                return 140;
            if(frameSize == 'EXTRA')
                return 160;

            return 25;
        };

        $scope.totalNoise = function(trial){
            return trial.quiz.squareRed + trial.quiz.squareBlue + trial.quiz.squareGreen + trial.quiz.circleGreen + trial.quiz.circleRed + trial.quiz.circleGreen;
        }



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

        $scope.openTutorial = function(){
            $modal.open({
                templateUrl: 'tutorial.html',
                controller: TutorialModalInstanceCtrl,
            });
        };

    })
    .controller('MullerLayerCtrl', function($scope, $rootScope, $http, $modal, toaster){
        $scope.inProcess = false;
        $scope.floatPattern = /^(?!0*(\.0+)?$)([0-5]|[0-4]\.\d+)$/;
        $scope.trials = [];
        $scope.regIneger = /^(0|[1-9][0-9]*)$/;

        $scope.noOfChoices = [3,4,5];
        $scope.lengthTypes = ['SHORT', 'MEDIUM','LONG'];

        $scope.init = function(expId){
            $scope.inProcess = true;
            $http({method:'GET',url:'mullerInit',params:{expId:expId}}).success(function(result){
                $scope.trials = result.trials;
                $rootScope.exp = $scope.trials[0].schedule;
                $scope.inProcess = false;
            }).error(function(result){
                $scope.inProcess = false;
            });
        }

        $scope.open = function(quiz){
            var modalInstance = $modal.open({
                templateUrl: 'preview.html',
                controller: MullerModalInstanceCtrl,
                size: 'lg',
                resolve: {
                    quiz : function(){
                        return quiz;
                    }
                }

            });

            modalInstance.result.then(function (){
                $rootScope.changingData();
            });
        };

        $scope.openSummary = function(){
            var modalInstance = $modal.open({
                templateUrl: 'summary.html',
                controller: MullerSummaryInstanceCtrl,
                size: 'lg',
                resolve: {
                    trials : function(){
                        return $scope.trials;
                    }
                }

            });
        };

        $scope.save = function(){
            save();
        }

        function save(){
            $scope.inProcess = true;
            $http({method:'PUT',url:'saveMullerTrial',data:$scope.trials})
            .success(function(result){
                $scope.inProcess = false;
                $rootScope.isChange = false;
                toaster.pop('success', 'บันทึกข้อมูลสำเร็จ!', '', 5000);
            }).error(function(result){
                $scope.inProcess = false;
                toaster.pop('warning', 'บันทึกข้อมูลล้มเหลว!', '', 5000);
            });
        }

        $scope.calculateDifferChoice = function(quiz){
            if(quiz.differChoice > quiz.noOfChoice)
                quiz.differChoice = Math.floor((Math.random() * quiz.noOfChoice) + 1);

        }
        $scope.openTutorial = function(){
            $modal.open({
                templateUrl: 'tutorial.html',
                controller: TutorialModalInstanceCtrl
            });
        };
    })
    .controller('GarnerController', function($scope, $rootScope, $http, $modal, toaster){
        $scope.floatPattern = /^(?!0*(\.0+)?$)(\d|\d\.\d+|10)$/;
        $scope.inProcess = false;
        $scope.trials = [];
        $scope.regIneger = /^(0|[1-9][0-9]*)$/;

        $scope.colors = ['red', 'blue', 'yellow', 'green', 'grey'];

        $scope.DARK = 0;
        $scope.LIGHT = 1;

        $scope.allColors = [];
        $scope.showColors = [];
        $scope.targets = [];
        $scope.open = [];
        $scope.modes = [];
        $scope.generateSuccess = [];

        var SIZES = ["big", "small"];
        var COLORS = ["dark", "light"];

        $scope.init = function(expId){
            $scope.inProcess = true;
            initAllColors();

            $http({method:'GET',url:'garnerInit',params:{expId:expId}}).success(function(result){
                $scope.trials = result.trials;
                $rootScope.exp = $scope.trials[0].schedule;
                initOpen();
                calculateAllColors(result.colors);
                $scope.inProcess = false;
            }).error(function(result){
                $scope.inProcess = false;
            });
        }

        function initOpen(){
            for(var i=0; i < $scope.trials.length; i++){
                $scope.open.push(false);
                $scope.showColors.push({colors:[]});
                $scope.modes.push($scope.DARK);
                $scope.generateSuccess.push(false);
            }
        }

        $scope.show = function(color, colorObj, index, mode){
            if(!$scope.open[index]){
                for(var i=0; i<$scope.allColors.length; i++){
                    if(color == $scope.allColors[i].name){
                        $scope.showColors[index].colors = $scope.allColors[i].objs;
                        break;
                    }
                }
                $scope.modes[index] = mode;
                $scope.targets[index] = colorObj;
            }
            $scope.open[index] = !$scope.open[index];
        }
        $scope.changeColor = function(trial, color, index){
            if($scope.modes[index] == $scope.DARK)
                trial.colorDark = color;
            else
                trial.colorLight = color;
            $scope.open[index] = false;
        }

        $scope.saveAll = function(){
            $scope.inProcess = true;
            $http({method:'PUT',url:'saveGarnerTrials',data:$scope.trials})
                .success(function(result){
                    $scope.inProcess = false;
                    $rootScope.isChange = false;
                    toaster.pop('success', 'บันทึกข้อมูลสำเร็จ!', '', 5000);
                }).error(function(result){
                    toaster.pop('warning', 'บันทึกข้อมูลล้มเหลว!', '', 5000);
                    $scope.inProcess = false;
                });
        }

        function calculateAllColors(colors){
            for(var i=0; i<colors.length; i++){
                for(var j=0; j < $scope.allColors.length; j++){
                    if(colors[i].color == $scope.allColors[j].name){
                        $scope.allColors[j].objs.push(colors[i]);
                    }

                }
            }
        }

        function initAllColors() {
            for(var i=0; i<$scope.colors.length; i++){
                var obj = {
                    name: $scope.colors[i],
                    objs: []
                }
                $scope.allColors.push(obj);
            }
        }

        $scope.generateColor = function(trial, index){
            $scope.open[index] = false;
            for(var i=0; i<$scope.allColors.length; i++){
                if(trial.color == $scope.allColors[i].name){
                    trial.colorDark =  $scope.allColors[i].objs[Math.floor(Math.random() * $scope.allColors[i].objs.length)];
                    trial.colorLight = calculateColor(trial.colorDark, $scope.allColors[i].objs)
                    if(trial.colorDark.saturation < trial.colorLight.saturation){
                        var temp = trial.colorDark;
                        trial.colorDark = trial.colorLight;
                        trial.colorLight = temp;
                    }
                    break;
                }
            }
        }

        function calculateColor(colorDark, colors){
            var colorLight = colors[Math.floor(Math.random() * colors.length)];
            return colorLight.id != colorDark.id ? colorLight : calculateColor(colorDark, colors);
        }

        $scope.generateQuestion = function(trial, index){
            if( trial.noOfBiDimensionQuestion == 0 &&
                trial.noOfColorQuestion == 0 &&
                trial.noOfSizeQuestion == 0 &&
                trial.noOfFakeBiDimentsionQuestion == 0 &&
                trial.noOfFakeColorQuestion == 0 &&
                trial.noOfFakeSizeQuestion == 0){
                    toaster.pop('warning', 'ไม่มีจำนวนคำถาม โปรดตรวจสอบอีกครั้ง', '', 5000);
                    return;
                }

            trial.quizzes = [];
            for(var i=0; i < trial.noOfColorQuestion; i++) {
                trial.quizzes.push(Quiz('COLOR',true));
            }
            for(var i=0; i < trial.noOfFakeColorQuestion; i++) {
                trial.quizzes.push(Quiz('COLOR', false));
            }
            for(var i=0; i < trial.noOfSizeQuestion; i++) {
                trial.quizzes.push(Quiz('SIZE', true));
            }
            for(var i=0; i < trial.noOfFakeSizeQuestion; i++) {
                trial.quizzes.push(Quiz('SIZE', false));
            }
            for(var i=0; i < trial.noOfBiDimensionQuestion; i++) {
                trial.quizzes.push(Quiz('BOTH', true));
            }
            for(var i=0; i < trial.noOfFakeBiDimentsionQuestion; i++) {
                trial.quizzes.push(Quiz('BOTH', false));
            }
            trial.quizzes = shuffle(trial.quizzes);
            $scope.generateSuccess[index] = true;

        }

        function destroyAlert(index){
            $scope.generateSuccess[index] = false;
        }

        function Quiz(questionType, isNotFake){
            return {
              questionType : questionType,
              question : createQuestion(questionType, isNotFake)
            };
        }

        function createQuestion(questionType, isNotFake){
            if(questionType == 'COLOR')
                return createColorQuestion(isNotFake);
            else if(questionType == 'SIZE')
                return createSizeQuestion(isNotFake);
            else
                return createBiDimensionQuestion(isNotFake);

        }
        function Question(){
            return {
                colorPic:'',
                sizePic:'',
                colorQuestion:'',
                sizeQuestion:'',
                colorMatch:false,
                sizeMatch:false
            };
        }

        function createColorQuestion(isNotFake){
            var question = Question();
            question.colorPic = generateColorPic();
            question.colorQuestion = isNotFake ? question.colorPic : calculateColorFake(question.colorPic);
            question.colorMatch = isNotFake ? true : false;
            question.sizePic = generateSizePic();
            return question;
        }
        function createSizeQuestion(isNotFake){
            var question = Question();
            question.sizePic = generateSizePic();
            question.sizeQuestion = isNotFake ? question.sizePic : calculateSizeFake(question.sizePic);
            question.sizeMatch = isNotFake ? true : false;
            question.colorPic = generateColorPic();
            return question;
        }
        function createBiDimensionQuestion(isNotFake){
            var question = Question();
            question.colorPic = generateColorPic();
            question.colorQuestion = isNotFake ? question.colorPic : calculateColorFake(question.colorPic);
            question.colorMatch = isNotFake ? true : false;
            question.sizePic = generateSizePic();
            question.sizeQuestion = isNotFake ? question.sizePic : calculateSizeFake(question.sizePic);
            question.sizeMatch = isNotFake ? true : false;
            return question;
        }

        function generateColorPic(){
            return COLORS[Math.floor(Math.random() * COLORS.length)];
        }
        function calculateColorFake(colorPic){
            return colorPic == COLORS[0] ? COLORS[1] : COLORS[0];
        }
        function generateSizePic(){
            return SIZES[Math.floor(Math.random() * SIZES.length)];
        }
        function calculateSizeFake(sizePic){
            return sizePic == SIZES[0] ? SIZES[1] : SIZES[0];
        }

        $scope.openSummary = function(){
            var modalInstance = $modal.open({
                templateUrl: 'summary.html',
                controller: GarnerSummaryInstanceCtrl,
                size: 'lg',
                resolve: {
                    trials : function(){
                        return $scope.trials;
                    }
                }

            });
        };

        function shuffle(array) {
            var currentIndex = array.length
                , temporaryValue
                , randomIndex
                ;

            // While there remain elements to shuffle...
            while (0 !== currentIndex) {

                // Pick a remaining element...
                randomIndex = Math.floor(Math.random() * currentIndex);
                currentIndex -= 1;

                // And swap it with the current element.
                temporaryValue = array[currentIndex];
                array[currentIndex] = array[randomIndex];
                array[randomIndex] = temporaryValue;
            }

            return array;
        }
        $scope.openTutorial = function(){
            $modal.open({
                templateUrl: 'tutorial.html',
                controller: TutorialModalInstanceCtrl,
            });
        };
    })
    .controller('ChangeBlindnessController', function($scope, $rootScope, $http, $modal) {
        $scope.inProcess = false;
        $scope.trials = [];

        $scope.init = function(expId){
            $http({method:'GET',url:'changeBlindnessInit',params:{expId:expId}}).success(function(result){
                $rootScope.exp = result.trials[0].schedule;
                initTrials(result.trials,result.questions);
                $scope.inProcess = false;
            }).error(function(result){
                $scope.inProcess = false;
            });
        }

        $scope.saveAll = function(){
            var trials = [];
            for(var i=0; i<$scope.trials.length; i++){
                $scope.trials[i].trial.quizzes = [];
                for(var j=0; j<$scope.trials[i].pictures.length; j++){
                    if($scope.trials[i].pictures[j].selected){
                        $scope.trials[i].trial.quizzes.push({question:$scope.trials[i].pictures[j].question});
                    }
                }
                trials.push($scope.trials[i].trial);
            }
            $scope.inProcess = true;
            $http({method:'PUT',url:'saveChangeBlindnessTrials',data:trials})
                .success(function(result){
                    $scope.inProcess = false;
                    $rootScope.isChange = false;
                }).error(function(result){
                    $scope.inProcess = false;
                });
        }

        $scope.open = function(pic){
            var modalInstance = $modal.open({
                templateUrl: 'preview.html',
                controller: ChangeBlindnessInstanceCtrl,
                size: 'lg',
                resolve: {
                    pic : function(){
                        return pic;
                    }
                }

            });
            modalInstance.result.then(function (){
                $rootScope.changingData();
            });

        };
        function initTrials(trials, questions){
            for(var i=0; i<trials.length; i++){
                var obj = {
                    trial: trials[i],
                    pictures: []
                }
                $scope.trials.push(obj);
            }
            initPictures(questions);
        }

        function initPictures(questions){
            for(var i=0; i<$scope.trials.length; i++){
                for(var j=0; j<questions.length; j++){
                    var obj = {
                        question: questions[j],
                        selected: isCurrentQuestion($scope.trials[i], questions[j])
                    }
                    $scope.trials[i].pictures.push(obj);
                }
            }

        }

        function isCurrentQuestion(obj, question){
            for(var i=0; i<obj.trial.quizzes.length; i++){
                if(obj.trial.quizzes[i].question.id == question.id){
                    return true;
                }
            }
            return false;
        }
    });

var ChangeBlindnessInstanceCtrl = function($scope, $rootScope, $modalInstance, pic) {

    $scope.pic = pic;

    $scope.ok = function () {
        $modalInstance.close();
    };
}

var GarnerSummaryInstanceCtrl = function($scope, $rootScope, $modalInstance, trials){

    $scope.trials = trials;

    $scope.ok = function () {
        $modalInstance.close();
    };

    $scope.cancel = function () {
        $modalInstance.dismiss();
    };

    $scope.showTextQuestion = function(quiz){
        if(quiz.questionType == 'COLOR')
            return quiz.question.colorQuestion == 'dark' ? 'สีเข้ม' : 'สีอ่อน';
        if(quiz.questionType == 'SIZE')
            return quiz.question.sizeQuestion == 'big' ? 'ขนาดใหญ่' : 'ขนาดเล็ก';
        if(quiz.questionType == 'BOTH')
            var text = quiz.question.colorQuestion == 'dark' ? 'สีเข้ม' : 'สีอ่อน';
            text += quiz.question.sizeQuestion == 'big' ? 'ขนาดใหญ่' : 'ขนาดเล็ก';
            return  text ;
    }

    $scope.showTextPicDisplay = function(quiz){
        var text = quiz.question.colorQuestion == 'dark' ? 'สีเข้ม' : 'สีอ่อน';
        text += quiz.question.sizeQuestion == 'big' ? 'ขนาดใหญ่' : 'ขนาดเล็ก';
        return  text ;
    }
    $scope.showAnswer = function(quiz){
        if(quiz.questionType == 'COLOR')
            return quiz.question.colorMatch ? 'glyphicon-ok' : 'glyphicon glyphicon-remove';
        if(quiz.questionType == 'SIZE')
            return quiz.question.sizeMatch ? 'glyphicon-ok' : 'glyphicon glyphicon-remove';
        if(quiz.questionType == 'BOTH')
            return quiz.question.colorMatch && quiz.question.sizeMatch ? 'glyphicon-ok' : 'glyphicon glyphicon-remove';
    }
}

var MullerSummaryInstanceCtrl = function($scope, $rootScope, $modalInstance, trials){

    $scope.trials = trials;

    $scope.ok = function () {
        $modalInstance.close();
    };

    $scope.cancel = function () {
        $modalInstance.dismiss();
    };

    $scope.calculateBackground = function(type, show){

        if(!show)
            return ''

        if(type == 'NONE')
            return 'active'
        if(type == 'LEFT')
            return 'success'
        if(type == 'RIGHT')
            return 'info'
        if(type == 'IN')
            return 'warning'
        if(type == 'OUT')
            return 'danger'

        return '';
    }
}

var MullerModalInstanceCtrl = function($scope, $rootScope, $modalInstance, quiz){

    var canvas = {};

    $scope.ok = function () {
        $modalInstance.close();
    };

    $scope.cancel = function () {
        $modalInstance.dismiss();
    };

    $scope.init = function(){

        canvas = document.getElementById("arrowPreview");
        var width = 800;
        var height = 400;
        var length = calculateLength(quiz);
        var x = (800/2) - (length/2);
        var y = 30;

        var question = quiz.question;
        drawLine(calculateStartX(1, x), y, calculateDifferenceLength(1,length), question.line1, 1);
        y += 70;
        drawLine(calculateStartX(2, x), y, calculateDifferenceLength(2,length), question.line2, 2);
        y += 70;
        drawLine(calculateStartX(3, x), y, calculateDifferenceLength(3,length), question.line3, 3);
        y += 70;
        if(quiz.noOfChoice > 3) {
            drawLine(calculateStartX(4, x), y, calculateDifferenceLength(4,length), question.line4, 4);
            y += 70;
        }
        if(quiz.noOfChoice > 4) {
            drawLine(calculateStartX(5, x), y, calculateDifferenceLength(5,length), question.line5, 5);
        }


    }

    $scope.shuffle = function(){
        var question = quiz.question;
        var array = [question.line1, question.line2, question.line3,
                    question.line4, question.line5];
        array = shuffle(array);
        question.line1 = array[0];
        question.line2 = array[1];
        question.line3 = array[2];
        question.line4 = array[3];
        question.line5 = array[4];

        quiz.differChoice = Math.floor((Math.random() * quiz.noOfChoice) + 1);

        canvas.width = canvas.width;
        $scope.init();
    }

    function shuffle(array) {
      var currentIndex = array.length
        , temporaryValue
        , randomIndex
        ;

      // While there remain elements to shuffle...
      while (0 !== currentIndex) {

        // Pick a remaining element...
        randomIndex = Math.floor(Math.random() * currentIndex);
        currentIndex -= 1;

        // And swap it with the current element.
        temporaryValue = array[currentIndex];
        array[currentIndex] = array[randomIndex];
        array[randomIndex] = temporaryValue;
      }

      return array;
    }

    function calculateDifferenceLength(no, length){
        if(!quiz.isPositive)
            return no == quiz.differChoice ? length - Math.floor((quiz.differLength * 38)) : length;
        else
            return no == quiz.differChoice ? length + Math.floor((quiz.differLength * 38)) : length;
    }
    function calculateStartX(no, x){
        if(!quiz.isPositive)
            return no == quiz.differChoice ? x + Math.floor((quiz.differLength * 38 / 2)) : x;
        else
            return no == quiz.differChoice ? x - Math.floor((quiz.differLength * 38 / 2)) : x;
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

    function drawLine(x, y, length, type, choice){
        var context = canvas.getContext("2d");
        context.beginPath();
        context.moveTo(x, y);
        drawLeftArrow(x, y, type, context);
        context.lineTo(x + length, y);
        drawRightArrow(x + length, y, type, context);
        context.closePath();
        var color = choice == quiz.differChoice ? 'blue' : 'black';
        context.strokeStyle = color;
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
        if(type == 'OUT' || type == 'RIGHT'){
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
        if(type == 'OUT' || type == 'LEFT'){
            context.lineTo(x + 30, y - 30);
            context.moveTo(x, y);
            context.lineTo(x + 30, y + 30);
        }
    }
}

var VisualSearchModalInstanceCtrl = function ($scope, $modal, $modalInstance, sharps, width, height, trial) {

    $scope.sharps = sharps;
    $scope.width = width;
    $scope.height = height;
    $scope.editSharp = {};
    $scope.floatPattern = /^[0-9]*\.?[0-9]+$/;

    $scope.sequence = 1;
    $scope.target = {};
    $scope.editSharp = {};
    $scope.target = null;


    var x_offset;
    var y_offset;


    $scope.generate = function () {
        generateSharps(trial);
    };
    $scope.ok = function () {
        trial.quiz.question.sharps = angular.toJson($scope.sharps);
        $modalInstance.close($scope.target);
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

    $scope.init = function(){
        var row = Math.floor($scope.height / 30);
        var column = Math.floor($scope.width / 30);
        x_offset = Math.floor(100/column);
        y_offset = Math.floor(100/row);

        if(trial.quiz.target == 'CIRCLE_RED'){
            $scope.target = Sharp(trial.quiz.positionYofTarget, trial.quiz.positionXofTarget, 'red', 'circle');
        }
        else if(trial.quiz.target == 'CIRCLE_GREEN'){
            $scope.target = Sharp(trial.quiz.positionYofTarget, trial.quiz.positionXofTarget, 'green', 'circle');
        }
        else if(trial.quiz.target == 'SQAURE_BLUE'){
            $scope.target = Sharp(trial.quiz.positionYofTarget, trial.quiz.positionXofTarget, 'blue', 'square');
        }
        else if(trial.quiz.target == 'SQAURE_RED'){
            $scope.target = Sharp(trial.quiz.positionYofTarget, trial.quiz.positionXofTarget, 'red', 'square');
        }
        else if(trial.quiz.target == 'SQAURE_GREEN'){
            $scope.target = Sharp(trial.quiz.positionYofTarget, trial.quiz.positionXofTarget, 'green', 'square');
        }
        else if(trial.quiz.target == 'CIRCLE_BLUE'){
            $scope.target = Sharp(trial.quiz.positionYofTarget, trial.quiz.positionXofTarget, 'blue', 'circle');
        }
        $scope.sharps = angular.fromJson(trial.quiz.question.sharps);
    }

    $scope.openGenerateTutorial = function(){
        $modal.open({
            templateUrl: 'tutorialInGenerate.html',
            controller: TutorialModalInstanceCtrl
        });
    };

    var generateSharps = function(trial){
        $scope.sharps = [];
        if(trial.quiz.target != 'CIRCLE_RED'){
            for(var i = 0; i < trial.quiz.circleRed;i++){
                var obj = createSharp('red','circle');
                if(obj != null)
                    $scope.sharps.push(obj);
            }
        }
        if(trial.quiz.target != 'CIRCLE_GREEN'){
            for(var i = 0; i < trial.quiz.circleGreen;i++){
                var obj = createSharp('green','circle');
                if(obj != null)
                    $scope.sharps.push(obj);
            }
        }
        if(trial.quiz.target != 'SQAURE_BLUE'){
            for(var i = 0; i < trial.quiz.squareBlue;i++){
                var obj = createSharp('blue','square');
                if(obj != null)
                    $scope.sharps.push(obj);
            }
        }
        if(trial.quiz.target != 'SQAURE_RED'){
            for(var i = 0; i < trial.quiz.squareRed;i++){

                var obj = createSharp('red','square');
                if(obj != null)
                    $scope.sharps.push(obj);
            }
        }
        if(trial.quiz.target != 'SQAURE_GREEN'){
            for(var i = 0; i < trial.quiz.squareGreen;i++){

                var obj = createSharp('green','square');
                if(obj != null)
                    $scope.sharps.push(obj);
            }
        }
        if(trial.quiz.target != 'CIRCLE_BLUE'){
            for(var i = 0; i < trial.quiz.circleBlue; i++){

                var obj = createSharp('blue', 'circle');
                if(obj != null)
                    $scope.sharps.push(obj);
            }
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
    


    


    var Sharp = function(top,left,color,sharp){
        var sharp = {
            id: $scope.sequence++,
            top: top,
            left: left,
            color: color,
            sharp: sharp
        }
        return sharp;
    }

    $scope.showSharp = function(sharp){
        $scope.editSharp = sharp;
    }
};

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

    var deleteQuestions = [];
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


var TutorialModalInstanceCtrl = function($scope, $modalInstance){
    $scope.ok = function () {
        $modalInstance.close();
    };

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
}

var ResetAttentionBlinkValuesModalInstanceCtrl = function($scope, $modalInstance){
    $scope.ok = function () {
        $modalInstance.close();
    };

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
}
