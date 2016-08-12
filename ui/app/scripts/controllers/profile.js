'use strict';

angular.module('twsUI')
    .controller('ProfileCtrl',
        ['jtbPlayerService',
            function (jtbPlayerService) {
                var controller = this;
                controller.player = jtbPlayerService.currentPlayer();
            }
        ]
    );
