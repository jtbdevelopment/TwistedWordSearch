'use strict';

describe('Controller: ProfileCtrl', function () {

    // load the controller's module
    beforeEach(module('twsUI'));

    var ProfileCtrl;

    var currentPlayer = {
        id: 'x',
        source: 'y'
    };
    var jtbPlayerService = {
        currentPlayer: function () {
            return currentPlayer;
        }
    };
    beforeEach(inject(function ($controller) {
        ProfileCtrl = $controller('ProfileCtrl', {
            jtbPlayerService: jtbPlayerService
        });
    }));


    it('initializes', function () {
        ProfileCtrl.player = currentPlayer;
    });
});
