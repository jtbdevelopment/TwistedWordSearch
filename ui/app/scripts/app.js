'use strict';

/**
 * @ngdoc overview
 * @name twsUI
 * @description
 * # twsUI
 *
 * Main module of the application.
 */
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
            .when('/about', {
                templateUrl: 'views/about.html',
                controller: 'AboutCtrl',
                controllerAs: 'about'
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
            //  TODO
            // .when('/game/playing/:gameID', {
            //     templateUrl: 'views/phases/play.html',
            //     controller: 'PlayCtrl',
            //     controllerAs: 'play'
            // })
            // .when('/game/setup/:gameID', {
            //     templateUrl: 'views/phases/setup.html',
            //     controller: 'SetupCtrl',
            //     controllerAs: 'setup'
            // })
            // .when('/game/challenged/:gameID', {
            //     templateUrl: 'views/phases/challenged.html',
            //     controller: 'ChallengeCtrl',
            //     controllerAs: 'challenged'
            // })
            // .when('/game/declined/:gameID', {
            //     templateUrl: 'views/phases/declined.html',
            //     controller: 'DeclineCtrl',
            //     controllerAs: 'declined'
            // })
            // .when('/game/quit/:gameID', {
            //     templateUrl: 'views/phases/quit.html',
            //     controller: 'DeclineCtrl',
            //     controllerAs: 'declined'
            // })
            // .when('/game/roundover/:gameID', {
            //     templateUrl: 'views/phases/roundover.html',
            //     controller: 'RoundOverCtrl',
            //     controllerAs: 'roundOver'
            // })
            // .when('/game/nextroundstarted/:gameID', {
            //     templateUrl: 'views/phases/nextroundstarted.html',
            //     controller: 'NextRoundCtrl',
            //     controllerAs: 'nextRound'
            // })
            .otherwise({
                redirectTo: '/signin'
            });
    });
