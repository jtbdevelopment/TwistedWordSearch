'use strict';

//  TODO - cleanup/breakup
//  TODO - shifting while in selecting mode - odd
//  TODO - initial select should highlight first cell
angular.module('twsUI').controller('PlayCtrl',
    ['$scope', '$http', '$routeParams', 'gridOffsetTracker', 'gridTableManager', 'jtbGameCache', 'jtbPlayerService', 'jtbBootstrapGameActions', 'featureDescriber', 'fontSizeManager',
        function ($scope, $http, $routeParams, gridOffsetTracker, gridTableManager, jtbGameCache, jtbPlayerService, jtbBootstrapGameActions, featureDescriber, fontSizeManager) {
            var controller = this;

            var SELECT_COLOR = '#9DC4B5';
            var FOUND_COLOR = '#C1D37F';

            //  controlled by gridMaster directive
            $scope.gridCanvasStyle = {
                top: 0,
                left: 0,
                height: 0,
                width: 0
            };
            controller.actions = jtbBootstrapGameActions;
            controller.offsetTracker = gridOffsetTracker;

            controller.grid = [];
            controller.cellStyles = [];

            controller.description = [];

            controller.forwardIsWord = false;
            controller.backwardIsWord = false;
            controller.rows = 0;
            controller.columns = 0;
            controller.showQuit = false;
            controller.showRematch = false;
            controller.acceptClicks = false;
            controller.fontSize = fontSizeManager.fontSizeStyle();
            gridOffsetTracker.reset();

            controller.highlightFoundWords = function () {
                    controller.foundCanvas = angular.element('#found-canvas')[0];
                    controller.foundContext = controller.foundCanvas.getContext('2d');
                    var linesToDraw = [];
                    //noinspection JSUnresolvedVariable
                    angular.forEach(controller.game.foundWordLocations, function (cells) {
                        var currentLine = {};
                        var lastCoordinate = null;
                        angular.forEach(cells, function (cell, index) {
                            var thisCoordinate = {
                                row: gridOffsetTracker.getOffsetRow(cell.row),
                                column: gridOffsetTracker.getOffsetColumn(cell.column)
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
                        highlightWord(
                            controller.foundCanvas,
                            controller.foundContext,
                            lineToDraw.from,
                            lineToDraw.to,
                            FOUND_COLOR);
                    });
                    controller.foundContext.closePath();
            };

            function updateControllerFromGame() {
                controller.tracking = false;
                controller.game = jtbGameCache.getGameForID($routeParams.gameID);
                featureDescriber.getShortDescriptionForGame(controller.game).then(function (data) {
                    controller.description = data;
                });
                controller.rows = controller.game.grid.length;
                controller.columns = controller.game.grid[0].length;
                gridOffsetTracker.gridSize(controller.game.grid.length, controller.game.grid[0].length);
                var grid = gridTableManager.updateForGame(controller.game);
                controller.grid = grid.cells;
                controller.cellStyles = grid.styles;
                controller.showQuit = (controller.game.gamePhase === 'Playing');
                controller.showRematch = (controller.game.gamePhase === 'RoundOver');
                controller.acceptClicks = controller.showQuit;
                controller.highlightFoundWords();
            }

            updateControllerFromGame();

            controller.zoomIn = function (amount) {
                controller.fontSize = fontSizeManager.increaseFontSize(amount);
            };

            controller.zoomOut = function (amount) {
                controller.fontSize = fontSizeManager.decreaseFontSize(amount);
            };

            $scope.$on('GridOffsetsChanged', function() {
                controller.highlightFoundWords();
            });

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

            controller.onMouseClick = function (event) {
                if (!controller.acceptClicks) {
                    return;
                }
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
                    gridTableManager.markCoordinatesAsSelected(controller.selectedCells);
                } else {
                    gridTableManager.unmarkCoordinatesAsSelected(controller.selectedCells);
                    if (controller.forwardIsWord || controller.backwardIsWord) {
                        var cells = [];
                        if (controller.backwardIsWord) {
                            controller.selectedCells.reverse();
                        }
                        var last;
                        angular.forEach(controller.selectedCells, function (cell, index) {
                            if (index === 0) {
                                cells.push({
                                    row: gridOffsetTracker.getOriginalRow(cell.row),
                                    column: gridOffsetTracker.getOriginalColumn(cell.column)
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
                //noinspection JSUnresolvedVariable
                controller.backwardIsWord = controller.game.wordsToFind.indexOf(controller.currentWordBackward) > -1;
                //noinspection JSUnresolvedVariable
                controller.forwardIsWord = controller.game.wordsToFind.indexOf(controller.currentWordForward) > -1;
            }

            function highlightWord(canvas, context, startCell, endCell, color) {
                var width = canvas.width / controller.columns;
                var halfWidth = width / 2;
                var height = canvas.height / controller.rows;
                var halfHeight = height / 2;
                var startX = (startCell.column * width) + halfWidth;
                var startY = (startCell.row * height) + halfHeight;
                var endX = ((endCell.column - startCell.column) * width) + startX;
                var endY = ((endCell.row - startCell.row) * height) + startY;
                context.lineWidth = (halfHeight + halfWidth) / 2;
                context.strokeStyle = color;
                context.lineCap = 'round';
                context.moveTo(startX, startY);
                context.lineTo(endX, endY);
                context.stroke();
            }

            controller.highlightSelectedLetters = function () {
                gridTableManager.unmarkCoordinatesAsSelected(controller.selectedCells);
                computeSelectedCells(controller.selectEnd);
                gridTableManager.markCoordinatesAsSelected(controller.selectedCells);
                clearGridCanvas(controller.selectCanvas, controller.selectContext);
                controller.selectContext.beginPath();
                highlightWord(
                    controller.selectCanvas,
                    controller.selectContext,
                    controller.selectedCells[0],
                    controller.selectedCells[controller.selectedCells.length - 1],
                    SELECT_COLOR);
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
                //noinspection JSUnresolvedVariable
                if (oldGame.id === controller.game.id) {
                    updateControllerFromGame();
                }
            });
        }
    ]
);
