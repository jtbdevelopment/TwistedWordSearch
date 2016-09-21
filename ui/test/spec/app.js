'use strict';

describe('Background Events', function () {
    beforeEach(module('twsUIBackground'));

    var $rootScope, $location;
    beforeEach(inject(function ($controller, _$rootScope_, _$location_) {
        $rootScope = _$rootScope_;
        $location = _$location_;
        spyOn($rootScope, '$broadcast').and.callThrough();
        spyOn($location, 'path');

    }));

    it('navigates to signin on invalid session', function () {
        $rootScope.$broadcast('InvalidSession');
        $rootScope.$apply();
        expect($location.path).toHaveBeenCalledWith('/signin');
    });


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
