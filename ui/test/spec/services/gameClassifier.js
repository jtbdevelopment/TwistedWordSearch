'use strict';

describe('Service: gameClassifier', function () {
    // load the controller's module
    beforeEach(module('twsUI.services'));

    /*
     var playerMD5 = 'anmd5!';
     var mockPlayerService = {
     currentPlayer: function () {
     return {md5: playerMD5};
     }
     };

     var mockCanPlay = false, mockSetupNeeded = false, mockRematchPossible = false, mockResponseNeeded = false;
     var mockGameDetailsService = {
     playerCanPlay: function (game, md5) {
     expect(md5).toEqual(playerMD5);
     return mockCanPlay;
     },
     playerChallengeResponseNeeded: function (game, md5) {
     expect(md5).toEqual(playerMD5);
     return mockResponseNeeded;
     },
     playerRematchPossible: function (game, md5) {
     expect(md5).toEqual(playerMD5);
     return mockRematchPossible;
     },
     playerSetupEntryRequired: function (game, md5) {
     expect(md5).toEqual(playerMD5);
     return mockSetupNeeded;
     }

     };

     beforeEach(module(function ($provide) {
     $provide.factory('jtbPlayerService', [function () {
     return mockPlayerService;
     }]);
     $provide.factory('tbsGameDetails', [function () {
     return mockGameDetailsService;
     }])
     }));
     */

    var service;
    beforeEach(inject(function ($injector) {
//        mockCanPlay = false;
//        mockSetupNeeded = false;
//        mockRematchPossible = false;
//        mockResponseNeeded = false;
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
});