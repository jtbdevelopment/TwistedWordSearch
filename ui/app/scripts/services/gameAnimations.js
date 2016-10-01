'use strict';

angular.module('twsUI.services').factory('gameAnimations',
    [
        '$animate', '$timeout', 'jtbPlayerService',
        function ($animate, $timeout, jtbPlayerService) {
            var md5 = jtbPlayerService.currentPlayer().md5;

            function highlightScoreChange(oldGame, newGame, controller) {
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

            function animateFoundWord(newGame, oldGame) {
                var foundChangedWord;
                angular.forEach(newGame.wordsToFind, function (word, index) {
                    if (angular.isUndefined(foundChangedWord)) {
                        if (word !== oldGame.wordsToFind[index]) {
                            foundChangedWord = oldGame.wordsToFind[index];
                        }
                    }
                });
                if (angular.isDefined(foundChangedWord)) {
                    $timeout(function () {
                        var alert = angular.element('#word-' + foundChangedWord.toLowerCase());
                        if (angular.isDefined(alert)) {
                            $animate.addClass(alert, 'animated lightSpeedIn').then(function () {
                                $animate.removeClass(alert, 'animated lightSpeedIn');
                            });
                        }
                    }, 200);//  give it a bit of chance to redraw
                }
            }

            return {
                animateGameUpdate: function (controller, oldGame, newGame) {
                    animateFoundWord(newGame, oldGame);
                    highlightScoreChange(oldGame, newGame, controller);
                }
            };
        }
    ]
);
