'use strict';

//  TODO - cleanup/breakup
//  TODO - shifting while in selecting mode - odd
//  TODO - initial select should highlight first cell
angular.module('twsUI').controller('PlayCtrl',
    ['$scope', '$http', '$routeParams', 'gridOffsetTracker', 'gridTableManager', 'foundWordsCanvasManager', 'canvasLineDrawer', 'jtbGameCache', 'jtbPlayerService', 'jtbBootstrapGameActions', 'featureDescriber', 'fontSizeManager',
        function ($scope, $http, $routeParams, gridOffsetTracker, gridTableManager, foundWordsCanvasManager, canvasLineDrawer, jtbGameCache, jtbPlayerService, jtbBootstrapGameActions, featureDescriber, fontSizeManager) {
            var controller = this;

            var SELECT_COLOR = '#9DC4B5';

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

            function updateControllerFromGame() {
                controller.tracking = false;
                controller.game = jtbGameCache.getGameForID($routeParams.gameID);
                featureDescriber.getShortDescriptionForGame(controller.game).then(function (data) {
                    controller.description = data;
                });
                controller.rows = controller.game.grid.length;
                controller.columns = controller.game.grid[0].length;
                gridOffsetTracker.gridSize(controller.rows, controller.columns);
                var grid = gridTableManager.updateForGame(controller.game);
                controller.grid = grid.cells;
                controller.cellStyles = grid.styles;
                foundWordsCanvasManager.updateForGame(controller.game, controller.rows, controller.columns);
                controller.showQuit = (controller.game.gamePhase === 'Playing');
                controller.showRematch = (controller.game.gamePhase === 'RoundOver');
                controller.acceptClicks = controller.showQuit;
            }

            updateControllerFromGame();

            controller.zoomIn = function (amount) {
                controller.fontSize = fontSizeManager.increaseFontSize(amount);
            };

            controller.zoomOut = function (amount) {
                controller.fontSize = fontSizeManager.decreaseFontSize(amount);
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
                    controller.selectContext.clearRect(0, 0, controller.selectCanvas.width, controller.selectCanvas.height);
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

            controller.highlightSelectedLetters = function () {
                gridTableManager.unmarkCoordinatesAsSelected(controller.selectedCells);
                computeSelectedCells(controller.selectEnd);
                gridTableManager.markCoordinatesAsSelected(controller.selectedCells);
                controller.selectContext.clearRect(0, 0, controller.selectCanvas.width, controller.selectCanvas.height);
                controller.selectContext.beginPath();
                canvasLineDrawer.drawLine(
                    controller.selectContext,
                    controller.selectedCells[0],
                    controller.selectedCells[controller.selectedCells.length - 1],
                    controller.selectCanvas.height / controller.rows,
                    controller.selectCanvas.width / controller.columns,
                    SELECT_COLOR
                );
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
