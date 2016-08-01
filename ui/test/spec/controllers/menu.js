'use strict';

describe('Controller: MenuCtrl', function () {

    // load the controller's module
    beforeEach(module('twsUI'));

    var MenuCtrl, $q, $scope;

    var phasePromise;
    var phaseService = {
        phases: function () {
            phasePromise = $q.defer();
            return phasePromise.promise;
        }
    };

    // Initialize the controller and a mock scope
    beforeEach(inject(function ($controller, _$rootScope_, _$q_) {
        $scope = _$rootScope_.$new();
        $q = _$q_;
        MenuCtrl = $controller('MenuCtrl', {
            $scope: $scope,
            jtbGamePhaseService: phaseService,
            jtbGameCache: undefined
        });
    }));

    it('placeholder', function () {
    });

});
