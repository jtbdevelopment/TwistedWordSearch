'use strict';

/**
 * @ngdoc function
 * @name uiApp.controller:AboutCtrl
 * @description
 * # AboutCtrl
 * Controller of the uiApp
 */
angular.module('uiApp')
    .controller('AboutCtrl', ['$scope', '$http', function ($scope, $http) {
        this.awesomeThings = [
            'HTML5 Boilerplate',
            'AngularJS',
            'Karma'
        ];

        $scope.proxyTest = 'Waiting';
        //  As a template, this proves your proxy connect service is working.
        return $http.get('/api/social/apis', {cache: true}).then(
            function (response) {
                $scope.proxyTest = JSON.stringify(response);
            },
            function (data, status) {
                $scope.proxyTest = 'Failed:  data=' + JSON.stringify(data) + ', status=' + status;
            }
        );

    }]);
