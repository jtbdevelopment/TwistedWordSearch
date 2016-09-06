'use strict';

//  TODO - cleanup/breakup
angular.module('twsUI').controller('PlayCtrl',
    ['$scope', '$timeout', '$http', '$routeParams', 'jtbGameCache', 'jtbPlayerService', 'jtbBootstrapGameActions',
        function ($scope, $timeout, $http, $routeParams, jtbGameCache, jtbPlayerService, jtbBootstrapGameActions) {
            var controller = this;

            $scope.gridCanvasStyle = {
                top: 0,
                left: 0,
                height: 0,
                width: 0
            };
            var CURRENT_SELECTION = 'current-selection ';
            var PART_OF_FOUND_WORD = 'found-word ';

            controller.grid = [];
            controller.forwardIsWord = false;
            controller.backwardIsWord = false;
            controller.cellStyles = [];
            controller.rowOffset = 0;
            controller.columnOffset = 0;
            controller.rows = 0;
            controller.columns = 0;

            function computeOriginalRow(row) {
                var originalRow = row + controller.rowOffset;
                if (originalRow < 0) {
                    originalRow += controller.rows;
                }
                if (originalRow >= controller.rows) {
                    originalRow -= controller.rows;
                }
                return originalRow;
            }

            function computeOffsetRow(row) {
                var offSetRow = row + controller.rowOffset;
                if (offSetRow < 0) {
                    offSetRow += controller.rows;
                }
                if (offSetRow >= controller.rows) {
                    offSetRow -= controller.rows;
                }
                return offSetRow;
            }

            function computeOriginalColumn(column) {
                var originalColumn = column - controller.columnOffset;
                if (originalColumn < 0) {
                    originalColumn += controller.columns;
                }
                if (originalColumn >= controller.columns) {
                    originalColumn -= controller.columns;
                }
                return originalColumn;
            }

            function computeOffsetColumn(column) {
                var offSetColumn = column + controller.columnOffset;
                if (offSetColumn < 0) {
                    offSetColumn += controller.columns;
                }
                if (offSetColumn >= controller.columns) {
                    offSetColumn -= controller.columns;
                }
                return offSetColumn;
            }

            //  TODO - this is crappy
            controller.timeout = 1000;
            controller.highlightFoundWords = function () {
                $timeout(function () {
                    controller.foundCanvas = angular.element('#found-canvas')[0];
                    controller.foundContext = controller.foundCanvas.getContext('2d');
                    controller.foundCanvas.height = $scope.gridCanvasStyle.height;
                    controller.foundCanvas.width = $scope.gridCanvasStyle.width;
                    var linesToDraw = [];
                    angular.forEach(controller.game.foundWordLocations, function (cells) {
                        var currentLine = {};
                        var lastCoordinate;
                        angular.forEach(cells, function (cell, index) {
                            var thisCoordinate = {
                                row: computeOffsetRow(cell.row),
                                column: computeOffsetColumn(cell.column)
                            };
                            if (index === 0) {
                                lastCoordinate = thisCoordinate;
                                currentLine.from = lastCoordinate;
                            } else {
                                if (Math.abs(lastCoordinate.row - thisCoordinate.row) > 1 || Math.abs(lastCoordinate.column - thisCoordinate.column) > 1) {
                                    currentLine.to = lastCoordinate;
                                    linesToDraw.push(currentLine);
                                    currentLine = {};
                                    currentLine.from = thisCoordinate;
                                }
                                lastCoordinate = thisCoordinate;
                            }
                        });
                        currentLine.to = lastCoordinate;
                        linesToDraw.push(currentLine);
                    });
                    clearGridCanvas(controller.foundCanvas, controller.foundContext);
                    controller.foundContext.beginPath();
                    angular.forEach(linesToDraw, function (lineToDraw) {
                        //  TODO - customize color
                        highlightWord(
                            controller.foundContext,
                            lineToDraw.from,
                            lineToDraw.to,
                            '#89E894');
                    });
                    controller.foundContext.closePath();
                }, controller.timeout);  //  bit buggy depends on how fast it renders
                controller.timeout = 0;
            };

            function recomputeDisplayedGrid() {
                controller.grid = [];
                controller.cellStyles = [];
                console.log(controller.game.grid.length);
                console.log(controller.grid.length);
                angular.forEach(controller.game.grid, function () {
                    controller.grid.push(new Array(controller.columns));
                    controller.cellStyles.push(new Array(controller.columns));
                });
                console.log(controller.grid.length);
                angular.forEach(controller.game.grid, function (row, index) {
                    var offSetRow = computeOffsetRow(index);
                    angular.forEach(row, function (column, index) {
                        var offSetColumn = computeOffsetColumn(index);

                        controller.grid[offSetRow][offSetColumn] = column;
                        controller.cellStyles[offSetRow][offSetColumn] = '';
                    });
                });

                angular.forEach(controller.game.foundWordLocations, function (cells) {
                    angular.forEach(cells, function (cell) {
                        var offSetRow = computeOffsetRow(cell.row);
                        var offSetColumn = computeOffsetColumn(cell.column);
                        controller.cellStyles[offSetRow][offSetColumn] += PART_OF_FOUND_WORD;
                    });
                });


                controller.highlightFoundWords();
            }

            function updateControllerFromGame() {
                controller.tracking = false;
                controller.game = jtbGameCache.getGameForID($routeParams.gameID);
                controller.grid = [];
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
                controller.selectCanvas = angular.element('#select-canvas')[0];
                controller.selectContext = controller.selectCanvas.getContext('2d');
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
                    controller.selectCanvas.height = $scope.gridCanvasStyle.height;
                    controller.selectCanvas.width = $scope.gridCanvasStyle.width;
                } else {
                    clearStyleFromCoordinates(controller.selectedCells, CURRENT_SELECTION);
                    if (controller.forwardIsWord || controller.backwardIsWord) {
                        var cells = [];
                        if (controller.backwardIsWord) {
                            controller.selectedCells.reverse();
                        }
                        var last;
                        angular.forEach(controller.selectedCells, function (cell, index) {
                            if (index === 0) {
                                cells.push({
                                    row: computeOriginalRow(cell.row),
                                    column: computeOriginalColumn(cell.column)
                                });
                                last = cell;
                            }
                            else {
                                cells.push({
                                    row: cell.row - last.row,
                                    column: cell.column - last.column
                                });
                                last = cell;
                            }
                        });
                        jtbBootstrapGameActions.wrapActionOnGame($http.put(jtbPlayerService.currentPlayerBaseURL() + '/game/' + controller.game.id + '/find', cells));
                    }
                    controller.selectedCells = [];
                    controller.currentWordBackward = '';
                    controller.currentWordForward = '';
                    controller.forwardIsWord = false;
                    controller.backwardIsWord = false;
                    clearGridCanvas(controller.selectCanvas, controller.selectContext);
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

            function clearGridCanvas(canvas, canvasContext) {
                canvasContext.clearRect(0, 0, canvas.width, canvas.height);
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
                    if (controller.grid[coordinate.row][coordinate.column] === ' ' ||
                        coordinate.row < 0 ||
                        coordinate.column < 0 ||
                        coordinate.column >= controller.columns ||
                        coordinate.row >= controller.rows) {
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
                controller.backwardIsWord = controller.game.wordsToFind.indexOf(controller.currentWordBackward) > -1;
                controller.forwardIsWord = controller.game.wordsToFind.indexOf(controller.currentWordForward) > -1;
            }

            function highlightWord(context, startCell, endCell, color) {
                var table = angular.element('#word-grid')[0];
                var halfWidth = table.offsetWidth / controller.columns / 2;
                var halfHeight = table.offsetHeight / controller.rows / 2;
                var startX = (startCell.column * halfWidth * 2) + halfWidth;
                var startY = (startCell.row * halfHeight * 2) + halfHeight;
                var endX = ((endCell.column - startCell.column) * halfWidth * 2) + startX;
                var endY = ((endCell.row - startCell.row) * halfHeight * 2) + startY;
                context.lineWidth = 8;
                context.strokeStyle = color;
                context.moveTo(startX, startY);
                context.lineTo(endX, endY);
                context.stroke();
            }

            controller.highlightSelectedLetters = function () {
                clearStyleFromCoordinates(controller.selectedCells, CURRENT_SELECTION);
                computeSelectedCells(controller.selectEnd);
                addStyleToCoordinates(controller.selectedCells, CURRENT_SELECTION);
                clearGridCanvas(controller.selectCanvas, controller.selectContext);
                controller.selectContext.beginPath();
                highlightWord(
                    controller.selectContext,
                    controller.selectedCells[0],
                    controller.selectedCells[controller.selectedCells.length - 1],
                    '#FFFF99');  //  TODO - parameterize
                controller.selectContext.closePath();
            };


            controller.onMouseMove = function (event) {
                if (!controller.tracking) {
                    return;
                }
                var target = computeTargetEndPoint(event);
                if (controller.selectEnd.row !== target.row || controller.selectEnd.column !== target.column) {
                    controller.selectEnd = target;
                    controller.highlightSelectedLetters();
                    computeSelectedWord();
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
