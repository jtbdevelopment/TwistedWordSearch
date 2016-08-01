'use strict';

describe('Controller: MainCtrl', function () {

    // load the controller's module
    beforeEach(module('twsUI'));

    var MainCtrl,
        scope;

    var playerDetails = {};
    var jtbPlayerService = {
        currentPlayer: function () {
            return playerDetails;
        },
        signOutAndRedirect: jasmine.createSpy()
    };

    // Initialize the controller and a mock scope
    beforeEach(inject(function ($controller, $rootScope) {
        scope = $rootScope.$new();
        playerDetails = {};
        MainCtrl = $controller('MainCtrl', {
            $scope: scope,
            jtbPlayerService: jtbPlayerService
        });
    }));

    it('initializes to full screen empty side bar', function () {
        expect(MainCtrl.sideBarTemplate).toEqual('views/sidebar/empty.html');
        expect(MainCtrl.mainBodySize).toEqual('col-xs-12 col-md-12');
        expect(MainCtrl.showAdmin).toEqual(false);
        expect(MainCtrl.showLogout).toEqual(false);
        expect(MainCtrl.player).toEqual({});
    });

    it('converts to sidebar setup after normal fb player loaded message', function () {
        playerDetails = {adminUser: false, source: 'facebook'};
        scope.$broadcast('playerLoaded');
        scope.$apply();
        expect(MainCtrl.sideBarTemplate).toEqual('views/sidebar/games.html');
        expect(MainCtrl.mainBodySize).toEqual('col-xs-8 col-md-10');
        expect(MainCtrl.showAdmin).toEqual(false);
        expect(MainCtrl.showLogout).toEqual(false);
        expect(MainCtrl.player).toEqual(playerDetails);
    });

    it('converts to sidebar setup after admin fb player loaded message', function () {
        playerDetails = {adminUser: true, source: 'facebook'};
        scope.$broadcast('playerLoaded');
        scope.$apply();
        expect(MainCtrl.sideBarTemplate).toEqual('views/sidebar/games.html');
        expect(MainCtrl.mainBodySize).toEqual('col-xs-8 col-md-10');
        expect(MainCtrl.showAdmin).toEqual(true);
        expect(MainCtrl.showLogout).toEqual(false);
        expect(MainCtrl.player).toEqual(playerDetails);
    });

    it('converts to sidebar setup after normal manual player loaded message', function () {
        playerDetails = {adminUser: false, source: 'MANUAL'};
        scope.$broadcast('playerLoaded');
        scope.$apply();
        expect(MainCtrl.sideBarTemplate).toEqual('views/sidebar/games.html');
        expect(MainCtrl.mainBodySize).toEqual('col-xs-8 col-md-10');
        expect(MainCtrl.showAdmin).toEqual(true);
        expect(MainCtrl.showLogout).toEqual(true);
        expect(MainCtrl.player).toEqual(playerDetails);
    });

    it('handles logout for manual players', function () {
        playerDetails = {adminUser: false, source: 'MANUAL'};
        scope.$broadcast('playerLoaded');
        scope.$apply();
        expect(MainCtrl.sideBarTemplate).toEqual('views/sidebar/games.html');
        expect(MainCtrl.mainBodySize).toEqual('col-xs-8 col-md-10');
        expect(MainCtrl.showAdmin).toEqual(true);
        expect(MainCtrl.showLogout).toEqual(true);
        MainCtrl.logout();
        expect(MainCtrl.sideBarTemplate).toEqual('views/sidebar/empty.html');
        expect(MainCtrl.mainBodySize).toEqual('col-xs-12 col-md-12');
        expect(MainCtrl.showAdmin).toEqual(false);
        expect(MainCtrl.showLogout).toEqual(false);
        expect(MainCtrl.player).toEqual({});
        expect(jtbPlayerService.signOutAndRedirect).toHaveBeenCalled();
    });
});
