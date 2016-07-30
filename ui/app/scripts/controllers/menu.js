'use strict';

/**
 * @ngdoc function
 * @name twsUI.controller:MenuCtrl
 * @description
 * # MainCtrl
 * Controller of the twsUI
 */
angular.module('twsUI').controller('MenuCtrl',
    ['$scope', 'jtbGameCache', 'jtbGamePhaseService',
        function ($scope, jtbGameCache, jtbGamePhaseService) {
            var controller = this;
            controller.menuIsCollapsed = false;

            controller.phases = [];
            controller.phaseCollapsed = {};
            controller.games = {};
            jtbGamePhaseService.phases().then(
                function (phases) {
                    controller.phases = angular.copy(phases);
                    angular.forEach(phases, function (phase) {
                        controller.games[phase[1]] = [];
                        controller.phaseCollapsed[phase] = false;
                    });
                },
                function () {
                    //  TODO
                });

            function updateGames() {
                angular.forEach(controller.phases, function (phase) {
                    controller.games[phase[1]] = angular.copy(jtbGameCache.getGamesForPhase(phase[1]));
                });
            }

            $scope.$on('gameCachesLoaded', function () {
                updateGames();
            });

            $scope.$on('gameRemoved', function () {
                updateGames();
            });

            $scope.$on('gameAdded', function () {
                updateGames();
            });

            $scope.$on('gameUpdated', function () {
                updateGames();
            });
        }
    ]
);
