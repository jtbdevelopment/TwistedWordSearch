'use strict';

/**
 * @ngdoc function
 * @name twsUI.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the twsUI
 */
angular.module('twsUI').controller('MenuCtrl',
    ['$scope', 'jtbPlayerService',
        function ($scope, jtbPlayerService) {
            this.menuIsCollapsed = false;

            $scope.$on('playerLoaded', function () {
            });
        }
    ]
);
