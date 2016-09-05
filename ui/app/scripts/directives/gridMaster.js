'use strict';

angular.module('twsUI').directive('gridMaster',
    ['$interval', function ($interval) {
    function link(scope, element) {
        var height = element[0].offsetHeight;
        var width = element[0].offsetWidth;
        var promise = $interval(function () {
            height = element[0].offsetHeight;
            width = element[0].offsetWidth;
        }, 500);
        scope.$watch(function () {
            scope.gridCanvasStyle = {
                top: 0,
                left: 0,
                height: height,
                width: width
            };
        });
        scope.$on('$destroy', function () {
            $interval.cancel(promise);
        });
    }

    return {
        restrict: 'AE',
        link: link
    };
    }]);
