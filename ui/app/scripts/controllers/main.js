'use strict';

/**
 * @ngdoc function
 * @name twsUI.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the twsUI
 */
angular.module('twsUI').controller('MainCtrl',
    ['$scope', '$rootScope', 'jtbPlayerService', '$location',
        function ($scope, $rootScope, jtbPlayerService, $location) {
            var controller = this;

            function setEmptySideBar() {
                controller.showAdmin = false;
                controller.showLogout = false;
                controller.player = {};
                controller.sideBarTemplate = 'views/sidebar/empty.html';
                controller.mainBodySize = 'col-xs-12 col-md-12';
            }

            function setButtonSideBar() {
                controller.sideBarTemplate = 'views/sidebar/games.html';
                controller.mainBodySize = 'col-xs-8 col-md-10';
            }

            setEmptySideBar();

            controller.logout = function () {
                setEmptySideBar();
                jtbPlayerService.signOutAndRedirect();
            };

            controller.newGame = function () {
                $location.path('/create');
                //  TODO - hide menu?
            };

            controller.refreshGames = function () {
                $rootScope.$broadcast('refreshGames');
            };

            $scope.$on('playerLoaded', function () {
                setButtonSideBar();
                angular.copy(jtbPlayerService.currentPlayer(), controller.player);
                controller.showLogout = controller.player.source === 'MANUAL';
                controller.showAdmin = controller.player.adminUser || controller.showLogout;
            });
        }
    ]
);
