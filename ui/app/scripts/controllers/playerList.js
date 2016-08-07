'use strict';

//  TODO - update of game state
angular.module('twsUI').controller('PlayerListCtrl',
    ['$scope', '$routeParams', 'jtbGameCache', 'jtbBootstrapGameActions', 'jtbPlayerService',
        function ($scope, $routeParams, jtbGameCache, jtbBootstrapGameActions, jtbPlayerService) {
            var controller = this;

            var currentPlayer = jtbPlayerService.currentPlayer();
            controller.groupGlyphicons = {
                Pending: 'question-sign',
                Accepted: 'thumbs-up',
                Declined: 'thumbs-down',
                Quit: 'flag'
            };
            controller.groups = ['Pending', 'Accepted', 'Declined', 'Quit'];
            controller.actions = jtbBootstrapGameActions;
            controller.groupCollapsed = {};
            controller.players = {};
            angular.forEach(controller.groups, function (group) {
                controller.players[group] = [];
                controller.groupCollapsed[group] = false;
            });
            function updateControllerFromGame() {
                controller.game = jtbGameCache.getGameForID($routeParams.gameID);
                angular.forEach(controller.players, function (value, key) {
                    controller.players[key] = [];
                });
                angular.forEach(controller.game.players, function (name, md5) {
                    var player = {
                        id: md5,
                        current: md5 === currentPlayer.md5,
                        displayName: name,
                        playerImage: controller.game.playerImages[md5],
                        playerProfile: controller.game.playerProfiles[md5]
                    };
                    controller.players[controller.game.playerStates[md5]].push(player);
                });

                controller.showAccept = controller.game.gamePhase === 'Challenged' &&
                    controller.game.playerStates[currentPlayer.md5] === 'Pending';
                controller.showReject = controller.game.gamePhase === 'Challenged';
                controller.showQuit = controller.game.gamePhase === 'Setup' || controller.game.gamePhase === 'Playing';
                controller.showRematch = controller.game.gamePhase === 'RoundOver';
            }

            updateControllerFromGame();
            $scope.$on('gameUpdated', function (message, oldGame) {
                if (oldGame.id === controller.game.id) {
                    updateControllerFromGame();
                }
            });
        }
    ]
);
