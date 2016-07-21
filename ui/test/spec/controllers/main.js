'use strict';

describe('Controller: MainCtrl', function () {

    // load the controller's module
    beforeEach(module('twsUI'));

    var MainCtrl,
        scope;

    // Initialize the controller and a mock scope
    beforeEach(inject(function ($controller, $rootScope) {
        scope = $rootScope.$new();
        MainCtrl = $controller('MainCtrl', {
            $scope: scope,
            jtbPlayerService: undefined
        });
    }));

    it('initializes to full screen empty side bar', function () {
        expect(MainCtrl.sideBarTemplate).toEqual('views/sidebar/empty.html');
        expect(MainCtrl.mainBodySize).toEqual('col-xs-12 col-md-12');
    });

    it('converts to sidebar setup after player loaded message', function () {
        scope.$broadcast('playerLoaded');
        scope.$apply();
        expect(MainCtrl.sideBarTemplate).toEqual('views/sidebar/games.html');
        expect(MainCtrl.mainBodySize).toEqual('col-xs-10 col-md-8');
    });
});
