'use strict';

//  TODO - add to starter base
describe('testing app js primary app', function () {

    describe('module definition tests', function () {

        var moduleUnderTest;
        var dependencies = [];

        var hasModule = function (module) {
            return dependencies.indexOf(module) >= 0;
        };

        beforeEach(function () {
            moduleUnderTest = angular.module('twsUI');
            dependencies = moduleUnderTest.requires;
        });

        it('should load outside dependencies', function () {
            expect(hasModule('ngCookies')).toBeTruthy();
            expect(hasModule('ngResource')).toBeTruthy();
            expect(hasModule('ngRoute')).toBeTruthy();
            expect(hasModule('ngSanitize')).toBeTruthy();
            expect(hasModule('ngTouch')).toBeTruthy();
            expect(hasModule('ui.bootstrap')).toBeTruthy();
            expect(hasModule('ui.select')).toBeTruthy();
            expect(hasModule('coreGamesUi')).toBeTruthy();
            expect(hasModule('coreGamesBootstrapUi')).toBeTruthy();
        });
    });

    describe('config', function () {
        var $routeProvider;
        beforeEach(function () {
            module('ngRoute');

            module(function ($provide, _$routeProvider_) {
                $routeProvider = _$routeProvider_;
                spyOn($routeProvider, 'when').and.callThrough();
                spyOn($routeProvider, 'otherwise').and.callThrough();
            });

            module('twsUI');
        });

        beforeEach(inject());

        it('should configure url router default', function () {
            expect($routeProvider.otherwise).toHaveBeenCalledWith({redirectTo: '/signin'});
        });

        it('should register /main', function () {
            expect($routeProvider.when).toHaveBeenCalledWith('/main', {templateUrl: 'views/main.html'});
        });
        it('should register signin', function () {
            expect($routeProvider.when).toHaveBeenCalledWith(
                '/signin',
                {
                    templateUrl: 'views/signin.html',
                    controller: 'CoreBootstrapSignInCtrl',
                    controllerAs: 'signIn'
                });
        });
        it('should register signedin', function () {
            expect($routeProvider.when).toHaveBeenCalledWith(
                '/signedin',
                {
                    templateUrl: 'views/signedin.html',
                    controller: 'CoreBootstrapSignedInCtrl',
                    controllerAs: 'signedIn'
                });
        });
        it('should register admin', function () {
            expect($routeProvider.when).toHaveBeenCalledWith(
                '/admin',
                {
                    templateUrl: 'views/admin/admin.html',
                    controller: 'CoreAdminCtrl',
                    controllerAs: 'admin'
                });
        });
        it('should register profile', function () {
            expect($routeProvider.when).toHaveBeenCalledWith(
                '/profile',
                {
                    templateUrl: 'views/profile.html',
                    controller: 'ProfileCtrl',
                    controllerAs: 'profile'
                });
        });
        it('should register create', function () {
            expect($routeProvider.when).toHaveBeenCalledWith(
                '/create',
                {
                    templateUrl: 'views/create.html',
                    controller: 'CreateGameCtrl',
                    controllerAs: 'create'
                });
        });
        it('should register create', function () {
            expect($routeProvider.when).toHaveBeenCalledWith(
                '/create',
                {
                    templateUrl: 'views/create.html',
                    controller: 'CreateGameCtrl',
                    controllerAs: 'create'
                });
        });
        it('should register challenged', function () {
            expect($routeProvider.when).toHaveBeenCalledWith(
                '/game/challenged/:gameID',
                {
                    templateUrl: 'views/phases/playerList.html',
                    controller: 'PlayerListCtrl',
                    controllerAs: 'list'
                });
        });
        it('should register declined', function () {
            expect($routeProvider.when).toHaveBeenCalledWith(
                '/game/declined/:gameID',
                {
                    templateUrl: 'views/phases/playerList.html',
                    controller: 'PlayerListCtrl',
                    controllerAs: 'list'
                });
        });
        it('should register quit', function () {
            expect($routeProvider.when).toHaveBeenCalledWith(
                '/game/quit/:gameID',
                {
                    templateUrl: 'views/phases/playerList.html',
                    controller: 'PlayerListCtrl',
                    controllerAs: 'list'
                });
        });
        it('should register roundover', function () {
            expect($routeProvider.when).toHaveBeenCalledWith(
                '/game/roundover/:gameID',
                {
                    templateUrl: 'views/phases/play.html',
                    controller: 'PlayCtrl',
                    controllerAs: 'play'
                });
        });
        it('should register nextroundstarted', function () {
            expect($routeProvider.when).toHaveBeenCalledWith(
                '/game/nextroundstarted/:gameID',
                {
                    templateUrl: 'views/phases/play.html',
                    controller: 'PlayCtrl',
                    controllerAs: 'play'
                });
        });
        it('should register play', function () {
            expect($routeProvider.when).toHaveBeenCalledWith(
                '/game/playing/:gameID',
                {
                    templateUrl: 'views/phases/play.html',
                    controller: 'PlayCtrl',
                    controllerAs: 'play'
                });
        });


    });
});