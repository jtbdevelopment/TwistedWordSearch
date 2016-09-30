'use strict';

angular.module('twsUI.services').factory('gameAnimations',
    [
        '$animate', 'jtbPlayerService',
        function ($animate, jtbPlayerService) {
            var md5 = jtbPlayerService.currentPlayer().md5;
            return {
                animateGameUpdate: function (controller, oldGame, newGame) {
                    angular.forEach();
                    if (oldGame.scores[md5] !== newGame.scores[md5]) {
                        controller.scoreChange = newGame.scores[md5] - oldGame.scores[md5];
                        var alert = angular.element('#score-alert');
                        if (angular.isDefined(alert)) {
                            $animate.addClass(alert, 'in').then(function () {
                                $animate.removeClass(alert, 'in');
                            });
                        }
                    }
                }
            }
        }
    ]
);
