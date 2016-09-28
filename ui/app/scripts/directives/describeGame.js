'use strict';

angular.module('twsUI').directive('describeGame',
    [
        function () {
            return {
                scope: {
                    features: '=features'
                },
                templateUrl: 'views/describeGame.html'
            }
        }
    ]
);
