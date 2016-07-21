'use strict';

describe('Controller: MenuCtrl', function () {

    // load the controller's module
    beforeEach(module('twsUI'));

    var MenuCtrl,
        scope;

    // Initialize the controller and a mock scope
    beforeEach(inject(function ($controller, $rootScope) {
        scope = $rootScope.$new();
        MenuCtrl = $controller('MenuCtrl', {
            $scope: scope,
            jtbPlayerService: undefined
        });
    }));

    it('initializes to full screen empty side bar', function () {
        expect(MenuCtrl.menuIsCollapsed).toEqual(false);
    });

    it('converts to sidebar setup after player loaded message', function () {
        scope.$broadcast('playerLoaded');
        scope.$apply();
    });
});
