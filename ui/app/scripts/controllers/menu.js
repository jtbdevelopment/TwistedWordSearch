'use strict';

angular.module('twsUI').controller('MenuCtrl',
    ['$scope', 'jtbGameCache', 'jtbGamePhaseService',
        function ($scope, jtbGameCache, jtbGamePhaseService) {
            var controller = this;

            controller.phases = [];
            controller.phaseLabels = {};
            controller.phaseDescriptions = {};
            controller.phaseCollapsed = {};
            controller.games = {};
            controller.describeGame = function () {
                return '';
            };

            //  TODO - customize if using custom classifier
            controller.phaseGlyphicons = {
                Playing: 'play',
                Setup: 'comment',
                Challenged: 'inbox',
                RoundOver: 'repeat',
                Declined: 'remove',
                NextRoundStarted: 'ok-sign',
                Quit: 'flag'
            };

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
