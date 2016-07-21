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
            //  TODO - switch core to controller as syntax
            .when('/signin', {
                templateUrl: 'views/signin.html',
                controller: 'CoreBootstrapSignInCtrl'
            })
            .when('/signedin', {
                templateUrl: 'views/signedin.html',
                controller: 'CoreBootstrapSignedInCtrl'
            })
            .otherwise({
                redirectTo: '/signin'
            });
    });
