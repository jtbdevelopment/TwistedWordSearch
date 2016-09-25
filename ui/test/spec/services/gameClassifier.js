'use strict';

describe('Service: gameClassifier', function () {
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

    var service;
    beforeEach(inject(function ($injector) {
        service = $injector.get('jtbGameClassifier');
    }));

    var expectedYourTurnClassification = 'Your move.';
    var expectedTheirTurnClassification = 'Their move.';
    var expectedOlderGameClassification = 'Older games.';
    var expectedIconMap = {};
    expectedIconMap[expectedYourTurnClassification] = 'play';
    expectedIconMap[expectedTheirTurnClassification] = 'pause';
    expectedIconMap[expectedOlderGameClassification] = 'stop';

    it('get classifications', function () {
        expect(service.getClassifications()).toEqual([expectedYourTurnClassification, expectedTheirTurnClassification, expectedOlderGameClassification]);
    });

    it('get icon map', function () {
        expect(service.getIcons()).toEqual(expectedIconMap);
    });

    it('classification for no player action needed, non-set phase', function () {
        var game = {gamePhase: 'TBD'};
        expect(service.getClassification(game)).toEqual(expectedTheirTurnClassification);
    });

    angular.forEach(['Playing', 'RoundOver'], function (phase) {
        it('classification for phase ' + phase, function () {
            var game = {gamePhase: phase};
            expect(service.getClassification(game)).toEqual(expectedYourTurnClassification);
        });
    });

    angular.forEach(['Declined', 'Quit', 'NextRoundStarted'], function (phase) {
        it('classification for phase ' + phase, function () {
            var game = {gamePhase: phase};
            expect(service.getClassification(game)).toEqual(expectedOlderGameClassification);
        });
    });

    it('classifies challenged game where current player is pending', function () {
        var game = {gamePhase: 'Challenged', playerStates: {}};
        game.playerStates[playerMD5] = 'Pending';
        expect(service.getClassification(game)).toEqual(expectedYourTurnClassification);
    });

    it('classifies challenged game where current player is not pending', function () {
        var game = {gamePhase: 'Challenged', playerStates: {}};
        game.playerStates[playerMD5] = 'NotPending';
        expect(service.getClassification(game)).toEqual(expectedTheirTurnClassification);
    });
});