'use strict';

/**
 * @ngdoc function
 * @name twsUI.controller:MenuCtrl
 * @description
 * # MenuCtrl
 * Controller of the twsUI
 */
angular.module('twsUI').controller('MenuCtrl',
    ['$scope', 'jtbGameCache', 'jtbGamePhaseService',
        function ($scope, jtbGameCache, jtbGamePhaseService) {
            var controller = this;
            controller.menuIsCollapsed = false;

            controller.phases = [];
            controller.phaseLabels = {};
            controller.phaseDescriptions = {};
            controller.phaseCollapsed = {};
            controller.games = {};
            jtbGamePhaseService.phases().then(
                function (phases) {
                    controller.phases = [];
                    angular.forEach(phases, function (value, key) {
                        controller.phases.push(key);
                        controller.phaseLabels[key] = value[1];
                        controller.phaseDescriptions[key] = value[0];
                        controller.games[key] = [];
                        controller.phaseCollapsed[key] = false;
                    });
                },
                function () {
                    //  TODO
                });

            function updateGames() {
                angular.forEach(controller.phases, function (phase) {
                    controller.games[phase] = jtbGameCache.getGamesForPhase(phase);
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
