angular.module('VisualSearchApp',[])
    .controller('VisualSearchController', ['$scope', function($scope){
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
    }]);