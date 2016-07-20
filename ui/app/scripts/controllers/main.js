'use strict';

/**
 * @ngdoc function
 * @name twsUI.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the twsUI
 */
angular.module('twsUI').controller('MainCtrl',
    ['$scope', 'jtbPlayerService',
        function ($scope, jtbPlayerService) {
            $scope.menuIsCollapsed = false;
            $scope.sideBarTemplate = 'views/sidebar/empty.html';

            $scope.$on('playerLoaded', function () {
                $scope.sideBarTemplate = 'views/sidebar/games.html';
            })
        }
    ]
);
