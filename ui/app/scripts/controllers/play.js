'use strict';

angular.module('twsUI').controller('PlayCtrl',
    ['$scope', '$routeParams', 'jtbGameCache', 'jtbPlayerService',
        function ($scope, $routeParams, jtbGameCache, jtbPlayerService) {
            var controller = this;

            //var currentPlayer = jtbPlayerService.currentPlayer();
            controller.grid = [];
            controller.rowOffset = 0;
            controller.columnOffset = 0;
            controller.rows = 0;
            controller.columns = 0;

            function recomputeDisplayedGrid() {
                angular.forEach(controller.game.grid, function (row, index) {
                    controller.grid.push(new Array(controller.columns));
                    var offSetRow = index + controller.rowOffset;
                    if (offSetRow < 0) {
                        offSetRow = controller.rows + offSetRow;
                    }
                    if (offSetRow >= controller.rows) {
                        offSetRow -= controller.rows;
                    }
                    angular.forEach(row, function (column, index) {
                        var offSetColumn = index + controller.columnOffset;
                        if (offSetColumn < 0) {
                            offSetColumn = controller.columns + offSetColumn;
                        }
                        if (offSetColumn >= controller.columns) {
                            offSetColumn -= controller.columns;
                        }

                        controller.grid[offSetRow][offSetColumn] = column;
                    });
                });
            }

            function updateControllerFromGame() {
                controller.game = jtbGameCache.getGameForID($routeParams.gameID);
                controller.grid.slice(0);
                controller.rows = controller.game.grid.length;
                controller.columns = controller.game.grid[0].length;
                recomputeDisplayedGrid();
            }

            updateControllerFromGame();

            controller.shiftLeft = function () {
                controller.columnOffset -= 1;
                if(controller.columnOffset === -controller.columns) {
                    controller.columnOffset = 0;
                }
                recomputeDisplayedGrid();
            };

            controller.shiftRight = function () {
                controller.columnOffset += 1;
                if(controller.columnOffset === controller.columns) {
                    controller.columnOffset = 0;
                }
                recomputeDisplayedGrid();
            };

            controller.shiftUp = function () {
                controller.rowOffset -= 1;
                if(controller.rowOffset === -controller.rows) {
                    controller.rowOffset = 0;
                }
                recomputeDisplayedGrid();
            };

            controller.shiftDown = function () {
                controller.rowOffset += 1;
                if(controller.rowOffset === controller.rows) {
                    controller.rowOffset = 0;
                }
                recomputeDisplayedGrid();
            };

            $scope.$on('gameUpdated', function (message, oldGame) {
                if (oldGame.id === controller.game.id) {
                    updateControllerFromGame();
                }
            });
        }
    ]
);
