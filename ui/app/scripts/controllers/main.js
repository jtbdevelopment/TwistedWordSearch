'use strict';

/**
 * @ngdoc function
 * @name twsUI.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the twsUI
 */
var CURRENT_VERSION = 1.3;
var RELEASE_NOTES = 'Added hints.';
angular.module('twsUI').controller('MainCtrl',
    ['jtbAppLongName', '$rootScope', 'jtbPlayerService', 'jtbBootstrapVersionNotesService',
        function (jtbAppLongName, $rootScope, jtbPlayerService, jtbBootstrapVersionNotesService) {
            var controller = this;

            controller.showHelp = false;
            controller.showAdmin = false;
            controller.appName = jtbAppLongName;

            function fullSizeBody() {
                controller.mainBodySize = 'col-xs-12 col-sm-12 col-md-12';
            }

            function partialSizeBody() {
                controller.mainBodySize = 'col-xs-8 col-sm-9 col-md-10';
            }

            function setEmptySideBar() {
                controller.hideGames = false;
                controller.forceShowGames = false;
                controller.showAdmin = false;
                controller.showLogout = false;
                controller.player = {};
                controller.adTemplate = 'views/ads/empty.html';
                controller.sideBarTemplate = 'views/sidebar/empty.html';
                controller.sideBarSize = 'hidden';
                fullSizeBody();
            }

            function setButtonSideBar() {
                controller.adTemplate = 'views/ads/ad-holder.html';
                controller.sideBarTemplate = 'views/sidebar/games.html';
                controller.sideBarSize = 'col-xs-4 col-sm-3 col-md-2';
                partialSizeBody();
            }

            setEmptySideBar();

            controller.logout = function () {
                setEmptySideBar();
                jtbPlayerService.signOutAndRedirect();
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

            controller.toggleHelp = function () {
                controller.showHelp = !controller.showHelp;
            };

            $rootScope.$on('playerLoaded', function () {
                setButtonSideBar();
                angular.copy(jtbPlayerService.currentPlayer(), controller.player);
                controller.showLogout = controller.player.source === 'MANUAL';
                controller.showAdmin =
                    controller.player.adminUser ||
                    controller.showAdmin;  //  Once an admin always an admin for ui
                jtbBootstrapVersionNotesService.displayVersionNotesIfAppropriate(CURRENT_VERSION, RELEASE_NOTES);
            });
        }
    ]
);
