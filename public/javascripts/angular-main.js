angular.module('ExperimentCreator', ['ui.bootstrap'])
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
    });