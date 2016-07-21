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
            var controller = this;
            controller.sideBarTemplate = 'views/sidebar/empty.html';
            controller.mainBodySize = 'col-xs-12 col-md-12';

            $scope.$on('playerLoaded', function () {
                controller.sideBarTemplate = 'views/sidebar/games.html';
                controller.mainBodySize = 'col-xs-10 col-md-8';
            });
        }
    ]
);
