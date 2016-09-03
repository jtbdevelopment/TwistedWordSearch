'use strict';

angular.module('twsUI').directive('gridMaster', function () {
    function link(scope, element) {
        scope.$watch(function () {
            scope.gridCanvasStyle = {
                top: 0,
                left: 0,
                height: element[0].offsetHeight,
                width: element[0].offsetWidth
            };
        });
    }

    return {
        restrict: 'AE',
        link: link
    };
});
