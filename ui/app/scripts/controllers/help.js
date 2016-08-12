'use strict';

/**
 * @ngdoc function
 * @name twsUI.controller:HelpCtrl
 * @description
 * Controller of the twsUI
 *
 * TODO - delete proxyTest which is just handy to confirm setup is working after initial setup
 */
angular.module('twsUI')
    .controller('HelpCtrl',
        ['$http',
            function ($http) {
                var controller = this;

                controller.proxyTest = 'Waiting';
                //  As a template, this proves your proxy connect service is working.
                $http.get('/api/social/apis', {cache: true}).then(
                    function (response) {
                        console.log(JSON.stringify(response));
                        controller.proxyTest = JSON.stringify(response);
                    },
                    function (data, status) {
                        controller.proxyTest = 'Failed:  data=' + JSON.stringify(data) + ', status=' + status;
                    }
                );

            }
        ]
    );
