'use strict';

angular.module('twsUI').controller('PlayCtrl',
    ['$scope', '$timeout', '$routeParams', 'jtbGameCache', 'jtbPlayerService', 'jtbBootstrapGameActions',
        function ($scope, $timeout, $routeParams, jtbGameCache, jtbPlayerService, jtbBootstrapGameActions) {
            var controller = this;

            //var currentPlayer = jtbPlayerService.currentPlayer();
            controller.grid = [];
            controller.endPoint = [];
            controller.rowOffset = 0;
            controller.columnOffset = 0;
            controller.rows = 0;
            controller.columns = 0;

            function recomputeDisplayedGrid() {
                angular.forEach(controller.game.grid, function (row, index) {
                    controller.grid.push(new Array(controller.columns));
                    controller.endPoint.push(new Array(controller.columns));
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
                        controller.endPoint[offSetRow][offSetColumn] = false;
                    });
                });
            }

            function updateControllerFromGame() {
                controller.tracking = false;
                controller.game = jtbGameCache.getGameForID($routeParams.gameID);
                controller.grid.slice(0);
                controller.rows = controller.game.grid.length;
                controller.columns = controller.game.grid[0].length;
                recomputeDisplayedGrid();
            }

            updateControllerFromGame();

            controller.shiftLeft = function (amount) {
                controller.columnOffset -= amount;
                if (controller.columnOffset === -controller.columns) {
                    controller.columnOffset = 0;
                }
                recomputeDisplayedGrid();
            };

            controller.shiftRight = function (amount) {
                controller.columnOffset += amount;
                if (controller.columnOffset === controller.columns) {
                    controller.columnOffset = 0;
                }
                recomputeDisplayedGrid();
            };

            controller.shiftUp = function (amount) {
                controller.rowOffset -= amount;
                if (controller.rowOffset === -controller.rows) {
                    controller.rowOffset = 0;
                }
                recomputeDisplayedGrid();
            };

            controller.shiftDown = function (amount) {
                controller.rowOffset += amount;
                if (controller.rowOffset === controller.rows) {
                    controller.rowOffset = 0;
                }
                recomputeDisplayedGrid();
            };

            controller.quit = function () {
                jtbBootstrapGameActions.quit(controller.game);
            };

            controller.tracking = false;
            controller.trackingPaused = false;
            controller.clickStart = [];
            controller.currentEnd = [];
            controller.onMouseEnter = function () {
                controller.tracking = controller.trackingPaused;
            };

            controller.onMouseExit = function () {
                controller.trackingPaused = controller.tracking;
                controller.tracking = false;
            };

            controller.onMouseClick = function (event) {
                controller.tracking = !controller.tracking;
                if (controller.tracking) {
                    var row = parseInt(event.currentTarget.getAttribute('data-ws-row'));
                    var column = parseInt(event.currentTarget.getAttribute('data-ws-column'));
                    controller.clickStart = [row, column];
                    controller.currentEnd = [-1, -1];
                    controller.endPoint[row][column] = true;
                } else {
                    controller.endPoint[controller.clickStart[0]][controller.clickStart[1]] = false;
                    if (controller.currentEnd[0] >= 0) {
                        controller.endPoint[controller.currentEnd[0]][controller.currentEnd[1]] = false;
                    }
                    //  TODO - submit word!
                }
            };

            controller.onMouseMove = function (event) {
                if (!controller.tracking) {
                    return;
                }

                var row = parseInt(event.currentTarget.getAttribute('data-ws-row'));
                var column = parseInt(event.currentTarget.getAttribute('data-ws-column'));
                var dRow = controller.clickStart[0] - row;
                var dCol = column - controller.clickStart[1];
                // up = 0, down = 180
                // left to right up = 45
                // left to right = 90, right to left = -90
                // left to right down = 135
                // right to left up = -45
                // right to left down -135
                var angle = Math.atan2(dCol, dRow) * 180 / Math.PI;
                var roundedAngle = Math.round(angle / 45);
                var targetRow, targetColumn;
                console.log(angle + '/' + roundedAngle);
                switch (roundedAngle) {
                    case 0:
                    case 4:
                        targetRow = row;
                        targetColumn = controller.clickStart[1];
                        break;
                    case 2:
                    case -2:
                        targetRow = controller.clickStart[0];
                        targetColumn = column;
                        break;
                    default:
                        if (Math.abs(dRow) > Math.abs(dCol)) {
                            targetRow = controller.clickStart[0] - dRow;
                            targetColumn = controller.clickStart[1] + (Math.abs(dRow) * Math.sign(dCol));
                        } else {
                            targetRow = controller.clickStart[0] - (Math.abs(dCol) * Math.sign(dRow));
                            targetColumn = controller.clickStart[1] + dCol;
                        }
                }
                console.log(JSON.stringify(controller.currentEnd) + '/' + targetRow + ',' + targetColumn);
                if (controller.currentEnd[0] !== targetRow || controller.currentEnd[1] !== targetColumn) {
                    if (controller.currentEnd[0] >= 0) {
                        if (controller.currentEnd[0] !== controller.clickStart[0] ||
                            controller.currentEnd[1] !== controller.clickStart[1])
                            controller.endPoint[controller.currentEnd[0]][controller.currentEnd[1]] = false;
                    }
                    controller.currentEnd = [targetRow, targetColumn];
                    console.log(JSON.stringify(controller.currentEnd));
                    controller.endPoint[controller.currentEnd[0]][controller.currentEnd[1]] = true;
                }
            };

            $scope.$on('gameUpdated', function (message, oldGame) {
                if (oldGame.id === controller.game.id) {
                    updateControllerFromGame();
                }
            });
        }
    ]
);
