'use strict';

describe('Controller: MenuCtrl', function () {

    beforeEach(module('twsUI'));

    var MenuCtrl, $q, $scope, $rootScope;

    var games = {};
    var gameCache = {
        getGamesForPhase: function (phase) {
            return games[phase];
        }
    };
    var testPhasesAndIcons = {
        'Phase 1 Description': 'icon1',
        'Aha.': 'icon3',
        'Description Phase 2': 'icon2'
    };

    var jtbGameClassifier = {
        getIcons: function () {
            return testPhasesAndIcons;
        },
        getClassifications: function () {
            var c = [];
            angular.forEach(testPhasesAndIcons, function (value, key) {
                c.push(key);
            });
            return c;
        }
    };

    var featureDescriberPromises = {};
    var featureDescriber = {
        getShortDescriptionForGame: function (game) {
            featureDescriberPromises[game.id] = $q.defer();
            return featureDescriberPromises[game.id].promise;
        }
    };

    beforeEach(inject(function ($controller, _$rootScope_, _$q_) {
        $scope = _$rootScope_.$new();
        $q = _$q_;
        $rootScope = _$rootScope_;
        MenuCtrl = $controller('MenuCtrl', {
            $scope: $scope,
            jtbGameClassifier: jtbGameClassifier,
            featureDescriber: featureDescriber,
            jtbGameCache: gameCache
        });
    }));

    it('initializes', function () {
        expect(MenuCtrl.phases).toEqual(['Phase 1 Description', 'Aha.', 'Description Phase 2']);
        expect(MenuCtrl.phaseLabels).toEqual({
            'Phase 1 Description': 'Phase 1 Description',
            'Aha.': 'Aha.',
            'Description Phase 2': 'Description Phase 2'
        });
        expect(MenuCtrl.phaseDescriptions).toEqual({
            'Phase 1 Description': 'Phase 1 Description',
            'Aha.': 'Aha.',
            'Description Phase 2': 'Description Phase 2'
        });

        expect(MenuCtrl.phaseStyles).toEqual({
            'Phase 1 Description': 'phase-1-description',
            'Aha.': 'aha',
            'Description Phase 2': 'description-phase-2'
        });
        expect(MenuCtrl.phaseCollapsed).toEqual({
            'Phase 1 Description': false,
            'Aha.': false,
            'Description Phase 2': false
        });
        expect(MenuCtrl.games).toEqual({
            'Phase 1 Description': [],
            'Aha.': [],
            'Description Phase 2': []
        });
        expect(MenuCtrl.phaseGlyphicons).toEqual({
            'Phase 1 Description': 'icon1',
            'Aha.': 'icon3',
            'Description Phase 2': 'icon2'
        });
    });

    describe('updates games from cache on various broadcast messages', function () {
        angular.forEach(['gameCachesLoaded', 'gameRemoved', 'gameAdded', 'gameUpdated'], function (message) {
            games = {};
            angular.forEach(testPhasesAndIcons, function (value, key) {
                var gamesToCreate = Math.floor(Math.random() * 10);
                games[key] = [];
                for (var i = 0; i < gamesToCreate; ++i) {
                    games[key].push({id: Math.floor(Math.random() * 100000)});
                }
            });
            featureDescriberPromises = {};

            it('refreshes games on ' + message, function () {
                $rootScope.$broadcast(message);
                $rootScope.$apply();
                expect(MenuCtrl.games).toEqual(games);
                angular.forEach(games, function (phase) {
                    angular.forEach(phase, function(game) {
                        expect(MenuCtrl.descriptions[game.id]).toBeUndefined();
                        var resolve = {x: 'X', random: Math.random() * 50};
                        featureDescriberPromises[game.id].resolve(resolve);
                        $scope.$apply();
                        expect(MenuCtrl.descriptions[game.id]).toEqual(resolve);
                    });
                });
            });
        });
    });
});
