'use strict';

describe('Controller: MainCtrl', function () {

    // load the controller's module
    beforeEach(module('twsUI'));

    var MainCtrl, $scope, $location, $rootScope;

    var playerDetails = {};
    var jtbPlayerService = {
        currentPlayer: function () {
            return playerDetails;
        },
        signOutAndRedirect: jasmine.createSpy()
    };

    // Initialize the controller and a mock scope
    beforeEach(inject(function ($controller, _$rootScope_, _$location_) {
        $rootScope = _$rootScope_;
        $location = _$location_;
        spyOn($rootScope, '$broadcast').and.callThrough();
        spyOn($location, 'path');

        $scope = $rootScope.$new();
        playerDetails = {};
        MainCtrl = $controller('MainCtrl', {
            $scope: $scope,
            jtbPlayerService: jtbPlayerService
        });
    }));

    it('initializes to full screen empty side bar', function () {
        expect(MainCtrl.sideBarTemplate).toEqual('views/sidebar/empty.html');
        expect(MainCtrl.mainBodySize).toEqual('col-xs-12 col-md-12');
        expect(MainCtrl.sideBarSize).toEqual('hidden');
        expect(MainCtrl.showAdmin).toEqual(false);
        expect(MainCtrl.showLogout).toEqual(false);
        expect(MainCtrl.hideGames).toEqual(false);
        expect(MainCtrl.player).toEqual({});
    });

    it('converts to sidebar setup after normal fb player loaded message', function () {
        playerDetails = {adminUser: false, source: 'facebook'};
        $scope.$broadcast('playerLoaded');
        $scope.$apply();
        expect(MainCtrl.sideBarTemplate).toEqual('views/sidebar/games.html');
        expect(MainCtrl.mainBodySize).toEqual('col-xs-8 col-md-10');
        expect(MainCtrl.sideBarSize).toEqual('col-xs-4 col-md-2');
        expect(MainCtrl.showAdmin).toEqual(false);
        expect(MainCtrl.showLogout).toEqual(false);
        expect(MainCtrl.hideGames).toEqual(false);
        expect(MainCtrl.player).toEqual(playerDetails);
    });

    it('converts to sidebar setup after admin fb player loaded message', function () {
        playerDetails = {adminUser: true, source: 'facebook'};
        $scope.$broadcast('playerLoaded');
        $scope.$apply();
        expect(MainCtrl.sideBarTemplate).toEqual('views/sidebar/games.html');
        expect(MainCtrl.mainBodySize).toEqual('col-xs-8 col-md-10');
        expect(MainCtrl.sideBarSize).toEqual('col-xs-4 col-md-2');
        expect(MainCtrl.showAdmin).toEqual(true);
        expect(MainCtrl.showLogout).toEqual(false);
        expect(MainCtrl.hideGames).toEqual(false);
        expect(MainCtrl.player).toEqual(playerDetails);
    });

    it('converts to sidebar setup after normal manual player loaded message', function () {
        playerDetails = {adminUser: false, source: 'MANUAL'};
        $scope.$broadcast('playerLoaded');
        $scope.$apply();
        expect(MainCtrl.sideBarTemplate).toEqual('views/sidebar/games.html');
        expect(MainCtrl.mainBodySize).toEqual('col-xs-8 col-md-10');
        expect(MainCtrl.sideBarSize).toEqual('col-xs-4 col-md-2');
        expect(MainCtrl.showAdmin).toEqual(true);
        expect(MainCtrl.showLogout).toEqual(true);
        expect(MainCtrl.hideGames).toEqual(false);
        expect(MainCtrl.player).toEqual(playerDetails);
    });

    it('handles logout for manual players', function () {
        playerDetails = {adminUser: false, source: 'MANUAL'};
        $scope.$broadcast('playerLoaded');
        $scope.$apply();
        MainCtrl.toggleMenu();
        expect(MainCtrl.sideBarTemplate).toEqual('views/sidebar/games.html');
        expect(MainCtrl.mainBodySize).toEqual('col-xs-12 col-md-12');
        expect(MainCtrl.sideBarSize).toEqual('col-xs-4 col-md-2');
        expect(MainCtrl.showAdmin).toEqual(true);
        expect(MainCtrl.showLogout).toEqual(true);
        expect(MainCtrl.hideGames).toEqual(true);

        MainCtrl.logout();
        expect(MainCtrl.sideBarTemplate).toEqual('views/sidebar/empty.html');
        expect(MainCtrl.mainBodySize).toEqual('col-xs-12 col-md-12');
        expect(MainCtrl.sideBarSize).toEqual('hidden');
        expect(MainCtrl.showAdmin).toEqual(false);
        expect(MainCtrl.showLogout).toEqual(false);
        expect(MainCtrl.hideGames).toEqual(false);
        expect(MainCtrl.player).toEqual({});
        expect(jtbPlayerService.signOutAndRedirect).toHaveBeenCalled();
    });

    it('goes to location game on go action', function () {
        MainCtrl.go('/somewhere');
        expect($location.path).toHaveBeenCalledWith('/somewhere');
    });

    it('broadcasts refresh games on action', function () {
        MainCtrl.refreshGames();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('refreshGames');
    });

    describe('menu tests', function () {
        beforeEach(function () {
            playerDetails = {adminUser: false, source: 'MANUAL'};
            $scope.$broadcast('playerLoaded');
            $scope.$apply();
        });

        it('toggling menu, no hover involved', function () {
            expect(MainCtrl.hideGames).toEqual(false);
            MainCtrl.toggleMenu();
            expect(MainCtrl.hideGames).toEqual(true);
            expect(MainCtrl.mainBodySize).toEqual('col-xs-12 col-md-12');
            MainCtrl.toggleMenu();
            expect(MainCtrl.hideGames).toEqual(false);
            MainCtrl.toggleMenu();
            expect(MainCtrl.hideGames).toEqual(true);
            expect(MainCtrl.mainBodySize).toEqual('col-xs-12 col-md-12');
        });

        it('hovering when menu visible', function () {
            expect(MainCtrl.hideGames).toEqual(false);
            expect(MainCtrl.forceShowGames).toEqual(false);
            expect(MainCtrl.mainBodySize).toEqual('col-xs-8 col-md-10');

            MainCtrl.hoverMenu();

            expect(MainCtrl.hideGames).toEqual(false);
            expect(MainCtrl.forceShowGames).toEqual(false);
            expect(MainCtrl.mainBodySize).toEqual('col-xs-8 col-md-10');

            MainCtrl.stopHoverMenu();

            expect(MainCtrl.hideGames).toEqual(false);
            expect(MainCtrl.forceShowGames).toEqual(false);
            expect(MainCtrl.mainBodySize).toEqual('col-xs-8 col-md-10');
        });

        it('hovering when menu not visible', function () {
            MainCtrl.toggleMenu();

            expect(MainCtrl.hideGames).toEqual(true);
            expect(MainCtrl.forceShowGames).toEqual(false);
            expect(MainCtrl.mainBodySize).toEqual('col-xs-12 col-md-12');

            MainCtrl.hoverMenu();

            expect(MainCtrl.hideGames).toEqual(false);
            expect(MainCtrl.forceShowGames).toEqual(true);
            expect(MainCtrl.mainBodySize).toEqual('col-xs-8 col-md-10');

            MainCtrl.stopHoverMenu();

            expect(MainCtrl.hideGames).toEqual(true);
            expect(MainCtrl.forceShowGames).toEqual(false);
            expect(MainCtrl.mainBodySize).toEqual('col-xs-12 col-md-12');
        });

        it('enabling disabled menu while hovering', function () {
            MainCtrl.toggleMenu();

            expect(MainCtrl.hideGames).toEqual(true);
            expect(MainCtrl.forceShowGames).toEqual(false);
            expect(MainCtrl.mainBodySize).toEqual('col-xs-12 col-md-12');

            MainCtrl.hoverMenu();

            expect(MainCtrl.hideGames).toEqual(false);
            expect(MainCtrl.forceShowGames).toEqual(true);
            expect(MainCtrl.mainBodySize).toEqual('col-xs-8 col-md-10');

            MainCtrl.toggleMenu();
            expect(MainCtrl.hideGames).toEqual(false);
            expect(MainCtrl.forceShowGames).toEqual(false);
            expect(MainCtrl.mainBodySize).toEqual('col-xs-8 col-md-10');

            MainCtrl.stopHoverMenu();

            expect(MainCtrl.hideGames).toEqual(false);
            expect(MainCtrl.forceShowGames).toEqual(false);
            expect(MainCtrl.mainBodySize).toEqual('col-xs-8 col-md-10');
        });
    });
});
