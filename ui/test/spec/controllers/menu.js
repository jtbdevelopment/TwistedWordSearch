'use strict';

describe('Controller: MenuCtrl', function () {

    // load the controller's module
    beforeEach(module('twsUI'));

    var MenuCtrl, $q, $scope, $rootScope;

    var games = {};
    var gameCache = {
        getGamesForPhase: function (phase) {
            return games[phase];
        }
    };
    var phasePromise;
    var phaseService = {
        phases: function () {
            phasePromise = $q.defer();
            return phasePromise.promise;
        }
    };

    var testPhases = {
        Phase1: ['Phase 1 Description', 'Phase1 Label'],
        PhaseX: ['Aha!', 'X'],
        Phase2: ['Description Phase 2', 'Label Phase2']
    };

    // Initialize the controller and a mock scope
    beforeEach(inject(function ($controller, _$rootScope_, _$q_) {
        $scope = _$rootScope_.$new();
        $q = _$q_;
        $rootScope = _$rootScope_;
        MenuCtrl = $controller('MenuCtrl', {
            $scope: $scope,
            jtbGamePhaseService: phaseService,
            jtbGameCache: gameCache
        });
    }));

    it('initializes', function () {
        expect(MenuCtrl.phases).toEqual([]);
        expect(MenuCtrl.phaseLabels).toEqual({});
        expect(MenuCtrl.phaseDescriptions).toEqual({});
        expect(MenuCtrl.phaseCollapsed).toEqual({});
        expect(MenuCtrl.games).toEqual({});
        expect(MenuCtrl.phaseGlyphicons).toEqual({
            Playing: 'play',
            Setup: 'comment',
            Challenged: 'inbox',
            RoundOver: 'repeat',
            Declined: 'remove',
            NextRoundStarted: 'ok-sign',
            Quit: 'flag'
        });
    });

    it('initializes after phases deferred', function () {
        phasePromise.resolve(testPhases);
        $rootScope.$apply();
        expect(MenuCtrl.phases).toEqual(['Phase1', 'PhaseX', 'Phase2']);
        expect(MenuCtrl.phaseLabels).toEqual({
            Phase1: 'Phase1 Label',
            PhaseX: 'X',
            Phase2: 'Label Phase2'
        });
        expect(MenuCtrl.phaseDescriptions).toEqual({
            Phase1: 'Phase 1 Description',
            PhaseX: 'Aha!',
            Phase2: 'Description Phase 2'
        });
        expect(MenuCtrl.phaseCollapsed).toEqual({Phase1: false, PhaseX: false, Phase2: false});
        expect(MenuCtrl.games).toEqual({Phase1: [], PhaseX: [], Phase2: []});
    });

    describe('updates games from cache on various broadcast messages', function () {
        beforeEach(function () {
            phasePromise.resolve(testPhases);
            $rootScope.$apply();
        });
        angular.forEach(['gameCachesLoaded', 'gameRemoved', 'gameAdded', 'gameUpdated'], function (message) {
            games = {};
            angular.forEach(testPhases, function (value, key) {
                var gamesToCreate = Math.floor(Math.random() * 10);
                games[key] = [];
                for (var i = 0; i < gamesToCreate; ++i) {
                    games[key].push({id: Math.floor(Math.random() * 10000)});
                }
            });

            it('refreshes games on ' + message, function () {
                $rootScope.$broadcast(message);
                $rootScope.$apply();
                expect(MenuCtrl.games).toEqual(games);
            });
        });
    });

    describe('can describe games', function () {
        beforeEach(function () {
            phasePromise.resolve(testPhases);
            $rootScope.$apply();
        });

        //  TODO
        it('placeholder test', function () {
            expect(MenuCtrl.describeGame()).toEqual('');
        });
    });
});
