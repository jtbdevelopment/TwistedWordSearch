'use strict';

describe('Background Events', function () {
    describe('module definition tests', function () {

        var moduleUnderTest;
        var dependencies = [];

        var hasModule = function (module) {
            return dependencies.indexOf(module) >= 0;
        };

        beforeEach(function () {
            moduleUnderTest = angular.module('twsUIBackground');
            dependencies = moduleUnderTest.requires;
        });

        it('should load outside dependencies', function () {
            expect(hasModule('twsUI.services')).toBeTruthy();
            expect(hasModule('twsUI')).toBeTruthy();
        });
    });

    describe('functional tests', function () {

        beforeEach(module('twsUIBackground'));

        var $rootScope, $location;
        beforeEach(inject(function ($controller, _$rootScope_, _$location_) {
            $rootScope = _$rootScope_;
            $location = _$location_;
            spyOn($rootScope, '$broadcast').and.callThrough();
            spyOn($location, 'path');

        }));

        it('changes phases when game phase updated is current game', function () {
            var id = 'agameid';
            $location.path.calls.reset();
            $location.path.and.returnValue('/a/path/ending/' + id);
            $rootScope.$broadcast('gameUpdated', {id: id, gamePhase: 'P21'}, {id: id, gamePhase: 'NewPhase'});
            $rootScope.$apply();
            expect($location.path).toHaveBeenCalledWith('/game/newphase/' + id);
        });

        it('ignores game non phase changing game updated is current game', function () {
            var id = 'agameid';
            $location.path.calls.reset();
            $location.path.and.returnValue('/a/path/ending/' + id);
            $rootScope.$broadcast('gameUpdated', {id: id, gamePhase: 'P21'}, {id: id, gamePhase: 'P21'});
            $rootScope.$apply();
            expect($location.path).not.toHaveBeenCalledWith('/game/p21/' + id);
        });

        it('ignores other game  updated is not current game', function () {
            var id = 'agameid';
            $location.path.calls.reset();
            $location.path.and.returnValue('/a/path/ending/' + id);
            $rootScope.$broadcast('gameUpdated', {id: id + 'X', gamePhase: 'P21'}, {id: id + 'X', gamePhase: 'P21'});
            $rootScope.$apply();
            expect($location.path).not.toHaveBeenCalledWith('/game/p21/' + id + 'X');
        });
    });
});
