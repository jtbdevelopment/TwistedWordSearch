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

    var service, $animate, $rootElement, $rootScope, element;
    beforeEach(inject(function ($injector, _$animate_, _$rootElement_, _$rootScope_, $document) {
        $animate = _$animate_;
        $rootScope = _$rootScope_;
        $rootElement = _$rootElement_;
        element = angular.element('<div class="score-alert" id="score-alert">This is my view</div>');
        angular.element($document).find('body').append(element);
        $rootScope.$digest();
        service = $injector.get('gameAnimations');
    }));

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
        expect(element.hasClass('in')).toEqual(true);
        $animate.flush();
        expect(element.hasClass('in')).toEqual(false);
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
        expect(element.hasClass('in')).toEqual(false);
    });
});
