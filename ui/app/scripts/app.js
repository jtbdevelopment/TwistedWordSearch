'use strict';

/**
 * @ngdoc overview
 * @name twsUI
 * @description
 * # twsUI
 *
 * Main module of the application.
 */

//  Polyfill
Math.sign = Math.sign || function (x) {
        x = +x; // convert to a number
        if (x === 0 || isNaN(x)) {
            return x;
        }
        return x > 0 ? 1 : -1;
    };

angular.module('twsUIBackground', ['twsUI.services', 'twsUI'])
//  Separate module to avoid interfering with tests
    .run(function ($rootScope, $location) {
        $rootScope.$on('InvalidSession', function () {
            $location.path('/signin');
        });
        $rootScope.$on('gameUpdated', function (message, oldGame, newGame) {
            if ($location.path().endsWith(oldGame.id) && oldGame.gamePhase !== newGame.gamePhase) {
                $location.path('/game/' + newGame.gamePhase.toLowerCase() + '/' + newGame.id);
            }
        });
    });

angular
    .module('twsUI', [
        'ngAnimate',
        'ngCookies',
        'ngResource',
        'ngRoute',
        'ngSanitize',
        'ngTouch',
        'ui.bootstrap',
        'ui.select',
        'coreGamesUi',
        'coreGamesBootstrapUi'
    ])
    .config(function ($routeProvider) {
        $routeProvider
            .when('/main', {
                templateUrl: 'views/main.html'
            })
            .when('/help', {
                templateUrl: 'views/help.html',
                controller: 'HelpCtrl',
                controllerAs: 'help'
            })
            .when('/admin', {
                templateUrl: 'views/admin/admin.html',
                controller: 'CoreAdminCtrl',
                controllerAs: 'admin'
            })
            .when('/profile', {
                templateUrl: 'views/profile.html',
                controller: 'ProfileCtrl',
                controllerAs: 'profile'
            })
            .when('/create', {
                templateUrl: 'views/create.html',
                controller: 'CreateGameCtrl',
                controllerAs: 'create'
            })
            .when('/signin', {
                templateUrl: 'views/signin.html',
                controller: 'CoreBootstrapSignInCtrl',
                controllerAs: 'signIn'
            })
            .when('/signedin', {
                templateUrl: 'views/signedin.html',
                controller: 'CoreBootstrapSignedInCtrl',
                controllerAs: 'signedIn'
            })
            .when('/game/challenged/:gameID', {
                templateUrl: 'views/phases/playerList.html',
                controller: 'PlayerListCtrl',
                controllerAs: 'list'
            })
            .when('/game/declined/:gameID', {
                templateUrl: 'views/phases/playerList.html',
                controller: 'PlayerListCtrl',
                controllerAs: 'list'
            })
            .when('/game/quit/:gameID', {
                templateUrl: 'views/phases/playerList.html',
                controller: 'PlayerListCtrl',
                controllerAs: 'list'
            })
            //  TODO - review
            .when('/game/roundover/:gameID', {
                templateUrl: 'views/phases/play.html',
                controller: 'PlayCtrl',
                controllerAs: 'play'
            })
            .when('/game/nextroundstarted/:gameID', {
                templateUrl: 'views/phases/play.html',
                controller: 'PlayCtrl',
                controllerAs: 'play'
            })
            .when('/game/playing/:gameID', {
                templateUrl: 'views/phases/play.html',
                controller: 'PlayCtrl',
                controllerAs: 'play'
            })
            //  TODO
            // .when('/game/setup/:gameID', {
            //     templateUrl: 'views/phases/setup.html',
            //     controller: 'SetupCtrl',
            //     controllerAs: 'setup'
            // })
            .otherwise({
                redirectTo: '/signin'
            });
    });
