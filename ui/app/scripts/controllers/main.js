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

            function fullSizeBody() {
                controller.mainBodySize = 'col-xs-12 col-md-12';
            }

            function partialSizeBody() {
                controller.mainBodySize = 'col-xs-8 col-md-10';
            }

            function setEmptySideBar() {
                controller.hideGames = false;
                controller.forceShowGames = false;
                controller.showAdmin = false;
                controller.showLogout = false;
                controller.player = {};
                controller.sideBarTemplate = 'views/sidebar/empty.html';
                controller.sideBarSize = 'hidden';
                fullSizeBody();
            }

            function setButtonSideBar() {
                controller.sideBarTemplate = 'views/sidebar/games.html';
                controller.sideBarSize = 'col-xs-4 col-md-2';
                partialSizeBody();
            }

            setEmptySideBar();

            controller.logout = function () {
                setEmptySideBar();
                jtbPlayerService.signOutAndRedirect();
            };

            controller.go = function (where) {
                $location.path(where);
                //  TODO - hide menu?
            };

            controller.refreshGames = function () {
                $rootScope.$broadcast('refreshGames');
            };

            controller.toggleMenu = function () {
                controller.stopHoverMenu();
                controller.hideGames = !controller.hideGames;
                controller.forceShowGames = false;
                if (controller.hideGames) {
                    fullSizeBody();
                } else {
                    partialSizeBody();
                }
            };

            controller.hoverMenu = function () {
                if (controller.hideGames) {
                    controller.hideGames = false;
                    controller.forceShowGames = true;
                    partialSizeBody();
                }
            };

            controller.stopHoverMenu = function () {
                if (controller.forceShowGames) {
                    controller.hideGames = true;
                    controller.forceShowGames = false;
                    fullSizeBody();
                }
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
