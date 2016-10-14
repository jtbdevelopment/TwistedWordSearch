'use strict';

describe('Service: gameAnimations', function () {
    beforeEach(module('ngAnimateMock'));
    beforeEach(module('twsUI.services'));

    var playerMD5 = 'anmd5!';
    var mockPlayerService = {
        currentPlayer: function () {
            return {md5: playerMD5};
        }
    };

    beforeEach(module(function ($provide) {
        $provide.factory('jtbPlayerService', [function () {
            return mockPlayerService;
        }]);
    }));

    var service, $animate, $rootScope, $timeout, $document;
    beforeEach(inject(function ($injector, _$animate_, _$rootScope_, _$document_, _$timeout_) {
        $animate = _$animate_;
        $rootScope = _$rootScope_;
        $timeout = _$timeout_;
        $document = _$document_;
        service = $injector.get('gameAnimations');
    }));

    describe('testing word element', function () {
        var wordElement;
        beforeEach(function () {
            wordElement = angular.element('<div class="word" id="word-changed">Changed</div>');
            angular.element($document).find('body').append(wordElement);
            $rootScope.$digest();
        });

        it('animates a word if it moved between being found/not found', function () {
            var oldGame = {
                wordsToFind: ['one', 'two', 'Changed', 'three'],
                scores: {}
            };
            var newGame = {
                wordsToFind: ['one', 'two', 'three'],
                scores: {}
            };
            oldGame.scores[playerMD5] = 13;
            newGame.scores[playerMD5] = 13;


            service.animateGameUpdate({}, oldGame, newGame);
            expect(wordElement.hasClass('animated')).toEqual(false);
            expect(wordElement.hasClass('lightSpeedIn')).toEqual(false);
            expect(wordElement.hasClass('word')).toEqual(true);
            $timeout.flush();
            $rootScope.$apply();
            expect(wordElement.hasClass('animated')).toEqual(true);
            expect(wordElement.hasClass('lightSpeedIn')).toEqual(true);
            expect(wordElement.hasClass('word')).toEqual(true);
            $animate.flush();
            expect(wordElement.hasClass('animated')).toEqual(false);
            expect(wordElement.hasClass('lightSpeedIn')).toEqual(false);
            expect(wordElement.hasClass('word')).toEqual(true);
        });

        it('does not animates a word if did not move between being found/not found', function () {
            var oldGame = {
                wordsToFind: ['one', 'two', 'Changed', 'three'],
                scores: {}
            };
            var newGame = {
                wordsToFind: ['one', 'two', 'Changed', 'three'],
                scores: {}
            };
            oldGame.scores[playerMD5] = 13;
            newGame.scores[playerMD5] = 13;


            service.animateGameUpdate({}, oldGame, newGame);
            expect(wordElement.hasClass('animated')).toEqual(false);
            expect(wordElement.hasClass('lightSpeedIn')).toEqual(false);
            expect(wordElement.hasClass('word')).toEqual(true);
            var exception = false;
            try {
                $timeout.flush();
            } catch (ex) {
                exception = true;
            }
            expect(exception).toEqual(true);
            expect(wordElement.hasClass('animated')).toEqual(false);
            expect(wordElement.hasClass('lightSpeedIn')).toEqual(false);
            expect(wordElement.hasClass('word')).toEqual(true);
        });

        it('does not animates a word if it moved but cant find it', function () {
            var oldGame = {
                wordsToFind: ['one', 'two', 'Changed', 'three'],
                scores: {}
            };
            var newGame = {
                wordsToFind: ['one', 'Changed', 'three'],
                scores: {}
            };
            oldGame.scores[playerMD5] = 13;
            newGame.scores[playerMD5] = 13;


            service.animateGameUpdate({}, oldGame, newGame);
            expect(wordElement.hasClass('animated')).toEqual(false);
            expect(wordElement.hasClass('lightSpeedIn')).toEqual(false);
            expect(wordElement.hasClass('word')).toEqual(true);
            $timeout.flush();
            expect(wordElement.hasClass('animated')).toEqual(false);
            expect(wordElement.hasClass('lightSpeedIn')).toEqual(false);
            expect(wordElement.hasClass('word')).toEqual(true);
        });
    });

    describe('testing score element', function () {
        var scoreElement;
        beforeEach(function () {
            scoreElement = angular.element('<div class="score-alert" id="score-alert">This is my view</div>');
            angular.element($document).find('body').append(scoreElement);
            $rootScope.$digest();
        });
        it('animates score when score changes', function () {
            var controller = {};
            var oldGame = {
                scores: {}
            };
            var newGame = angular.copy(oldGame);
            oldGame.scores[playerMD5] = 13;
            newGame.scores[playerMD5] = 28;
            service.animateGameUpdate(controller, oldGame, newGame);
            $rootScope.$apply();
            expect(controller.scoreChange).toEqual(15);
            expect(scoreElement.hasClass('in')).toEqual(true);
            expect(scoreElement.hasClass('score-alert')).toEqual(true);
            $animate.flush();
            expect(scoreElement.hasClass('in')).toEqual(false);
            expect(scoreElement.hasClass('score-alert')).toEqual(true);
        });

        it('does not animates score when score for player unchanged', function () {
            var controller = {};
            var oldGame = {
                scores: {}
            };
            var newGame = angular.copy(oldGame);
            oldGame.scores[playerMD5] = 13;
            newGame.scores[playerMD5] = 13;
            service.animateGameUpdate(controller, oldGame, newGame);
            $rootScope.$apply();
            expect(controller.scoreChange).toBeUndefined();
            expect(scoreElement.hasClass('in')).toEqual(false);
            expect(scoreElement.hasClass('score-alert')).toEqual(true);
        });
    });

});
