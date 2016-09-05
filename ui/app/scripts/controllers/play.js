'use strict';

//  TODO - cleanup/breakup
angular.module('twsUI').controller('PlayCtrl',
    ['$scope', '$timeout', '$routeParams', 'jtbGameCache', 'jtbPlayerService', 'jtbBootstrapGameActions',
        function ($scope, $timeout, $routeParams, jtbGameCache, jtbPlayerService, jtbBootstrapGameActions) {
            var controller = this;

            var CURRENT_SELECTION = 'current-selection ';

            //var currentPlayer = jtbPlayerService.currentPlayer();
            controller.grid = [];
            controller.forwardIsWord = false;
            controller.backwardIsWord = false;
            controller.cellStyles = [];
            controller.rowOffset = 0;
            controller.columnOffset = 0;
            controller.rows = 0;
            controller.columns = 0;

            controller.canvas = angular.element('#grid-canvas')[0];
            controller.canvasContext = controller.canvas.getContext('2d');


            function recomputeDisplayedGrid() {
                angular.forEach(controller.game.grid, function (row, index) {
                    controller.grid.push(new Array(controller.columns));
                    controller.cellStyles.push(new Array(controller.columns));
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
                        controller.cellStyles[offSetRow][offSetColumn] = '';
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
            controller.selectedCells = [];
            controller.selectedStartElement = undefined;
            controller.selectStart = undefined;
            controller.selectEnd = undefined;
            controller.currentWordForward = '';
            controller.currentWordBackward = '';
            controller.onMouseEnter = function () {
                controller.tracking = controller.trackingPaused;
            };

            controller.onMouseExit = function () {
                controller.trackingPaused = controller.tracking;
                controller.tracking = false;
            };

            function clearStyleFromCoordinates(coordinates, style) {
                angular.forEach(coordinates, function (coordinate) {
                    controller.cellStyles[coordinate.row][coordinate.column] = controller.cellStyles[coordinate.row][coordinate.column].replace(style, '');
                });
            }

            function addStyleToCoordinates(coordinates, style) {
                angular.forEach(coordinates, function (coordinate) {
                    controller.cellStyles[coordinate.row][coordinate.column] += style;
                });
            }

            controller.onMouseClick = function (event) {
                controller.canvas = angular.element('#grid-canvas')[0];
                controller.canvasContext = controller.canvas.getContext('2d');
                controller.tracking = !controller.tracking;
                if (controller.tracking) {
                    var row = parseInt(event.currentTarget.getAttribute('data-ws-row'));
                    var column = parseInt(event.currentTarget.getAttribute('data-ws-column'));
                    if (controller.grid[row][column] === ' ') {
                        controller.tracking = false;
                        return;
                    }
                    var coordinate = {
                        row: row,
                        column: column
                    };
                    controller.selectStart = coordinate;
                    controller.selectEnd = coordinate;
                    controller.selectedCells = [coordinate];
                    controller.currentWordBackward = '';
                    controller.currentWordForward = '';
                    addStyleToCoordinates(controller.selectedCells, CURRENT_SELECTION);
                    controller.selectedStartElement = {
                        offsetTop: event.currentTarget.offsetTop,
                        offsetLeft: event.currentTarget.offsetLeft,
                        offsetWidth: event.currentTarget.offsetWidth,
                        offsetHeight: event.currentTarget.offsetHeight
                    };
                    controller.canvas.height = $scope.gridCanvasStyle.height;
                    controller.canvas.width = $scope.gridCanvasStyle.width;
                } else {
                    clearStyleFromCoordinates(controller.selectedCells, CURRENT_SELECTION);
                    //  TODO - submit word!
                    controller.selectedCells = [];
                    controller.currentWordBackward = '';
                    controller.currentWordForward = '';
                    controller.forwardIsWord = false;
                    controller.backwardIsWord = false;
                    clearGridCanvas();
                }
            };

            function computeTargetEndPoint(event) {
                var row = parseInt(event.currentTarget.getAttribute('data-ws-row'));
                var column = parseInt(event.currentTarget.getAttribute('data-ws-column'));
                var dRow = controller.selectStart.row - row;
                var dCol = column - controller.selectStart.column;
                // up = 0, down = 180
                // left to right = 90, right to left = -90
                // left to right up = 45, down = 135
                // right to left up = -45, down = -135
                var angle = Math.atan2(dCol, dRow) * 180 / Math.PI;
                var roundedAngle = Math.round(angle / 45);
                var targetRow, targetColumn;
                switch (roundedAngle) {
                    case 0:
                    case 4:
                        targetRow = row;
                        targetColumn = controller.selectStart.column;
                        break;
                    case 2:
                    case -2:
                        targetRow = controller.selectStart.row;
                        targetColumn = column;
                        break;
                    default:
                        if (Math.abs(dRow) > Math.abs(dCol)) {
                            targetRow = controller.selectStart.row - dRow;
                            targetColumn = controller.selectStart.column + (Math.abs(dRow) * Math.sign(dCol));
                        } else {
                            targetRow = controller.selectStart.row - (Math.abs(dCol) * Math.sign(dRow));
                            targetColumn = controller.selectStart.column + dCol;
                        }
                }
                return {row: targetRow, column: targetColumn};
            }

            function clearGridCanvas() {
                controller.canvasContext.clearRect(0, 0, controller.canvas.width, controller.canvas.height);
            }

            function computeSelectedCells(target) {
                controller.selectedCells = [controller.selectStart];
                var coordinate = {
                    row: controller.selectStart.row,
                    column: controller.selectStart.column
                };
                while (coordinate.row !== target.row || coordinate.column !== target.column) {
                    if (coordinate.row > target.row) {
                        coordinate.row -= 1;
                    }
                    if (coordinate.row < target.row) {
                        coordinate.row += 1;
                    }
                    if (coordinate.column > target.column) {
                        coordinate.column -= 1;
                    }
                    if (coordinate.column < target.column) {
                        coordinate.column += 1;
                    }
                    if (controller.grid[coordinate.row][coordinate.column] === ' ') {
                        break;
                    }
                    controller.selectedCells.push({row: coordinate.row, column: coordinate.column});
                }
            }

            function computeSelectedWord() {
                controller.currentWordForward = '';
                controller.currentWordBackward = '';
                angular.forEach(controller.selectedCells, function (coordinate) {
                    controller.currentWordForward += controller.grid[coordinate.row][coordinate.column];
                    controller.currentWordBackward =
                        controller.grid[coordinate.row][coordinate.column] +
                        controller.currentWordBackward;
                });
            }

            function highlightWord(startCell, endCell, color) {
                var halfWidth = controller.selectedStartElement.offsetWidth / 2;
                var halfHeight = controller.selectedStartElement.offsetHeight / 2;
                var startX = controller.selectedStartElement.offsetLeft + halfWidth;
                var startY = controller.selectedStartElement.offsetTop + halfHeight;
                var endX = ((endCell.column - startCell.column) * halfWidth * 2) + startX;
                var endY = ((endCell.row - startCell.row) * halfHeight * 2) + startY;
                controller.canvasContext.beginPath();
                controller.canvasContext.lineWidth = 8;
                controller.canvasContext.strokeStyle = color;
                controller.canvasContext.moveTo(startX, startY);
                controller.canvasContext.lineTo(endX, endY);
                controller.canvasContext.stroke();
                controller.canvasContext.closePath();
            }

            controller.onMouseMove = function (event) {
                if (!controller.tracking) {
                    return;
                }
                var target = computeTargetEndPoint(event);
                if (controller.selectEnd.row !== target.row || controller.selectEnd.column !== target.column) {
                    clearStyleFromCoordinates(controller.selectedCells, CURRENT_SELECTION);
                    controller.selectEnd = target;
                    computeSelectedCells(target);
                    addStyleToCoordinates(controller.selectedCells, CURRENT_SELECTION);
                    //  TODO - highlight when a word is found?
                    computeSelectedWord();
                    clearGridCanvas();
                    highlightWord(
                        controller.selectedCells[0],
                        controller.selectedCells[controller.selectedCells.length - 1],
                        '#FFFF99');
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
