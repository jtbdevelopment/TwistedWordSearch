'use strict';

angular.module('twsUI').directive('gridMaster', function () {
    function link(scope, element, attrs) { //scope we are in, element we are bound to, attrs of that element
        scope.$watch(function () { //watch any changes to our element
            scope.gridCanvasStyle = { //scope variable style, shared with our controller
                top: element[0].offsetTop + 'px',
                left: element[0].offsetLeft + 'px',
                height: element[0].offsetHeight + 'px', //set the height in style to our elements height
                width: element[0].offsetWidth + 'px' //same with width
            };
        });
    }

    return {
        restrict: 'AE', //describes how we can assign an element to our directive in this case like <div master></div
        link: link // the function to link to our element
    };
});
