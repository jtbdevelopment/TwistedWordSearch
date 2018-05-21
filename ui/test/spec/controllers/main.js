'use strict';

describe('Controller: MainCtrl', function () {

    beforeEach(module('twsUI'));

    var MainCtrl, $rootScope;

    var longName = 'some longer name';
    var playerDetails = {};
    var jtbPlayerService = {
        currentPlayer: function () {
            return playerDetails;
        },
        signOutAndRedirect: jasmine.createSpy('soar')
    };
    var versionService = {
        displayVersionNotesIfAppropriate: jasmine.createSpy('dvia')
    };

    beforeEach(inject(function ($controller, _$rootScope_) {
        $rootScope = _$rootScope_;
        spyOn($rootScope, '$broadcast').and.callThrough();

        playerDetails = {};
        MainCtrl = $controller('MainCtrl', {
            jtbAppLongName: longName,
            jtbBootstrapVersionNotesService: versionService,
            jtbPlayerService: jtbPlayerService
        });
    }));

    it('initializes to full screen empty side bar', function () {
        expect(MainCtrl.adTemplate).toEqual('views/ads/empty.html');
        expect(MainCtrl.sideBarTemplate).toEqual('views/sidebar/empty.html');
        expect(MainCtrl.mainBodySize).toEqual('col-xs-12 col-sm-12 col-md-12');
        expect(MainCtrl.sideBarSize).toEqual('hidden');
        expect(MainCtrl.showAdmin).toEqual(false);
        expect(MainCtrl.showLogout).toEqual(false);
        expect(MainCtrl.hideGames).toEqual(false);
        expect(MainCtrl.player).toEqual({});
        expect(MainCtrl.appName).toEqual(longName);
    });

    it('converts to sidebar setup after normal fb player loaded message', function () {
        playerDetails = {adminUser: false, source: 'facebook'};
        $rootScope.$broadcast('playerLoaded');
        $rootScope.$apply();
      expect(MainCtrl.adTemplate).toEqual('views/ads/empty.html');
        expect(MainCtrl.sideBarTemplate).toEqual('views/sidebar/games.html');
        expect(MainCtrl.mainBodySize).toEqual('col-xs-8 col-sm-9 col-md-10');
        expect(MainCtrl.sideBarSize).toEqual('col-xs-4 col-sm-3 col-md-2');
        expect(MainCtrl.showAdmin).toEqual(false);
        expect(MainCtrl.showLogout).toEqual(false);
        expect(MainCtrl.hideGames).toEqual(false);
        expect(MainCtrl.player).toEqual(playerDetails);
        expect(versionService.displayVersionNotesIfAppropriate).toHaveBeenCalledWith(1.3, 'Added hints.');
    });

    it('converts to sidebar setup after admin fb player loaded message', function () {
        playerDetails = {adminUser: true, source: 'facebook'};
        $rootScope.$broadcast('playerLoaded');
        $rootScope.$apply();
      expect(MainCtrl.adTemplate).toEqual('views/ads/empty.html');
        expect(MainCtrl.sideBarTemplate).toEqual('views/sidebar/games.html');
        expect(MainCtrl.mainBodySize).toEqual('col-xs-8 col-sm-9 col-md-10');
        expect(MainCtrl.sideBarSize).toEqual('col-xs-4 col-sm-3 col-md-2');
        expect(MainCtrl.showAdmin).toEqual(true);
        expect(MainCtrl.showLogout).toEqual(false);
        expect(MainCtrl.hideGames).toEqual(false);
        expect(MainCtrl.player).toEqual(playerDetails);
        expect(versionService.displayVersionNotesIfAppropriate).toHaveBeenCalledWith(1.3, 'Added hints.');
    });

    it('converts to sidebar setup after normal manual player loaded message', function () {
        playerDetails = {adminUser: false, source: 'MANUAL'};
        $rootScope.$broadcast('playerLoaded');
        $rootScope.$apply();
      expect(MainCtrl.adTemplate).toEqual('views/ads/empty.html');
        expect(MainCtrl.sideBarTemplate).toEqual('views/sidebar/games.html');
        expect(MainCtrl.mainBodySize).toEqual('col-xs-8 col-sm-9 col-md-10');
        expect(MainCtrl.sideBarSize).toEqual('col-xs-4 col-sm-3 col-md-2');
        expect(MainCtrl.showAdmin).toEqual(false);
        expect(MainCtrl.showLogout).toEqual(true);
        expect(MainCtrl.hideGames).toEqual(false);
        expect(MainCtrl.player).toEqual(playerDetails);
        expect(versionService.displayVersionNotesIfAppropriate).toHaveBeenCalledWith(1.3, 'Added hints.');
    });

    it('once an admin always and admin for session', function () {
        playerDetails = {adminUser: true, source: 'MANUAL'};
        $rootScope.$broadcast('playerLoaded');
        $rootScope.$apply();
      expect(MainCtrl.adTemplate).toEqual('views/ads/empty.html');
        expect(MainCtrl.sideBarTemplate).toEqual('views/sidebar/games.html');
        expect(MainCtrl.mainBodySize).toEqual('col-xs-8 col-sm-9 col-md-10');
        expect(MainCtrl.sideBarSize).toEqual('col-xs-4 col-sm-3 col-md-2');
        expect(MainCtrl.showAdmin).toEqual(true);
        expect(MainCtrl.showLogout).toEqual(true);
        expect(MainCtrl.hideGames).toEqual(false);
        expect(MainCtrl.player).toEqual(playerDetails);
        expect(versionService.displayVersionNotesIfAppropriate).toHaveBeenCalledWith(1.3, 'Added hints.');
        versionService.displayVersionNotesIfAppropriate.calls.reset();
        playerDetails = {adminUser: false, source: 'FACEBOOK'};
        $rootScope.$broadcast('playerLoaded');
        $rootScope.$apply();
        expect(MainCtrl.showAdmin).toEqual(true);
        expect(MainCtrl.showLogout).toEqual(false);
        expect(versionService.displayVersionNotesIfAppropriate).toHaveBeenCalledWith(1.3, 'Added hints.');
    });

    it('handles logout for manual players', function () {
        playerDetails = {adminUser: false, source: 'MANUAL'};
        $rootScope.$broadcast('playerLoaded');
        $rootScope.$apply();
        MainCtrl.toggleMenu();
        expect(MainCtrl.sideBarTemplate).toEqual('views/sidebar/games.html');
      expect(MainCtrl.adTemplate).toEqual('views/ads/empty.html');
        expect(MainCtrl.mainBodySize).toEqual('col-xs-12 col-sm-12 col-md-12');
        expect(MainCtrl.sideBarSize).toEqual('col-xs-4 col-sm-3 col-md-2');
        expect(MainCtrl.showAdmin).toEqual(false);
        expect(MainCtrl.showLogout).toEqual(true);
        expect(MainCtrl.hideGames).toEqual(true);

        MainCtrl.logout();
        expect(MainCtrl.adTemplate).toEqual('views/ads/empty.html');
        expect(MainCtrl.sideBarTemplate).toEqual('views/sidebar/empty.html');
        expect(MainCtrl.mainBodySize).toEqual('col-xs-12 col-sm-12 col-md-12');
        expect(MainCtrl.sideBarSize).toEqual('hidden');
        expect(MainCtrl.showAdmin).toEqual(false);
        expect(MainCtrl.showLogout).toEqual(false);
        expect(MainCtrl.hideGames).toEqual(false);
        expect(MainCtrl.player).toEqual({});
        expect(jtbPlayerService.signOutAndRedirect).toHaveBeenCalled();
    });

    describe('menu tests', function () {
        beforeEach(function () {
            playerDetails = {adminUser: false, source: 'MANUAL'};
            $rootScope.$broadcast('playerLoaded');
            $rootScope.$apply();
        });

        it('toggling menu, no hover involved', function () {
            expect(MainCtrl.hideGames).toEqual(false);
            MainCtrl.toggleMenu();
            expect(MainCtrl.hideGames).toEqual(true);
            expect(MainCtrl.mainBodySize).toEqual('col-xs-12 col-sm-12 col-md-12');
            MainCtrl.toggleMenu();
            expect(MainCtrl.hideGames).toEqual(false);
            MainCtrl.toggleMenu();
            expect(MainCtrl.hideGames).toEqual(true);
            expect(MainCtrl.mainBodySize).toEqual('col-xs-12 col-sm-12 col-md-12');
        });

        it('hovering when menu visible', function () {
            expect(MainCtrl.hideGames).toEqual(false);
            expect(MainCtrl.forceShowGames).toEqual(false);
            expect(MainCtrl.mainBodySize).toEqual('col-xs-8 col-sm-9 col-md-10');

            MainCtrl.hoverMenu();

            expect(MainCtrl.hideGames).toEqual(false);
            expect(MainCtrl.forceShowGames).toEqual(false);
            expect(MainCtrl.mainBodySize).toEqual('col-xs-8 col-sm-9 col-md-10');

            MainCtrl.stopHoverMenu();

            expect(MainCtrl.hideGames).toEqual(false);
            expect(MainCtrl.forceShowGames).toEqual(false);
            expect(MainCtrl.mainBodySize).toEqual('col-xs-8 col-sm-9 col-md-10');
        });

        it('hovering when menu not visible', function () {
            MainCtrl.toggleMenu();

            expect(MainCtrl.hideGames).toEqual(true);
            expect(MainCtrl.forceShowGames).toEqual(false);
            expect(MainCtrl.mainBodySize).toEqual('col-xs-12 col-sm-12 col-md-12');

            MainCtrl.hoverMenu();

            expect(MainCtrl.hideGames).toEqual(false);
            expect(MainCtrl.forceShowGames).toEqual(true);
            expect(MainCtrl.mainBodySize).toEqual('col-xs-8 col-sm-9 col-md-10');

            MainCtrl.stopHoverMenu();

            expect(MainCtrl.hideGames).toEqual(true);
            expect(MainCtrl.forceShowGames).toEqual(false);
            expect(MainCtrl.mainBodySize).toEqual('col-xs-12 col-sm-12 col-md-12');
        });

        it('enabling disabled menu while hovering', function () {
            MainCtrl.toggleMenu();

            expect(MainCtrl.hideGames).toEqual(true);
            expect(MainCtrl.forceShowGames).toEqual(false);
            expect(MainCtrl.mainBodySize).toEqual('col-xs-12 col-sm-12 col-md-12');

            MainCtrl.hoverMenu();

            expect(MainCtrl.hideGames).toEqual(false);
            expect(MainCtrl.forceShowGames).toEqual(true);
            expect(MainCtrl.mainBodySize).toEqual('col-xs-8 col-sm-9 col-md-10');

            MainCtrl.toggleMenu();
            expect(MainCtrl.hideGames).toEqual(false);
            expect(MainCtrl.forceShowGames).toEqual(false);
            expect(MainCtrl.mainBodySize).toEqual('col-xs-8 col-sm-9 col-md-10');

            MainCtrl.stopHoverMenu();

            expect(MainCtrl.hideGames).toEqual(false);
            expect(MainCtrl.forceShowGames).toEqual(false);
            expect(MainCtrl.mainBodySize).toEqual('col-xs-8 col-sm-9 col-md-10');
        });
    });
});
