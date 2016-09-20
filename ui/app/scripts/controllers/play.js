'use strict';

angular.module('twsUI').controller('PlayCtrl',
    ['$scope', '$http', '$routeParams',
        'gridOffsetTracker', 'gridTableManager', 'fontSizeManager',
        'foundWordsCanvasManager', 'canvasLineDrawer', 'featureDescriber',
        'jtbGameCache', 'jtbPlayerService', 'jtbBootstrapGameActions',
        function ($scope, $http, $routeParams,
                  gridOffsetTracker, gridTableManager, fontSizeManager,
                  foundWordsCanvasManager, canvasLineDrawer, featureDescriber,
                  jtbGameCache, jtbPlayerService, jtbBootstrapGameActions) {
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

            controller.showQuit = false;
            controller.showRematch = false;
            controller.fontSize = fontSizeManager.fontSizeStyle();
            controller.currentWordForward = '';
            controller.currentWordBackward = '';
            controller.forwardIsWord = false;
            controller.backwardIsWord = false;

            var rows;
            var columns;
            var acceptClicks = false;
            var tracking = false;
            var trackingPaused = false;
            var selectedCells = [];
            var originalSelectedCells = [];
            var selectStart;
            var selectEnd;
            var selectCanvas;
            var selectContext;

            function disableExistingTracking() {
                clearSelectedWord();
                tracking = false;
                trackingPaused = false;
            }

            function updateControllerFromGame() {
                controller.game = jtbGameCache.getGameForID($routeParams.gameID);
                featureDescriber.getShortDescriptionForGame(controller.game).then(function (data) {
                    controller.description = data;
                });
                rows = controller.game.grid.length;
                columns = controller.game.grid[0].length;
                gridOffsetTracker.gridSize(rows, columns);
                var grid = gridTableManager.updateForGame(controller.game);
                controller.grid = grid.cells;
                controller.cellStyles = grid.styles;
                foundWordsCanvasManager.updateForGame(controller.game, rows, columns);
                controller.showQuit = (controller.game.gamePhase === 'Playing');
                controller.showRematch = (controller.game.gamePhase === 'RoundOver');
                acceptClicks = controller.showQuit;
                if (!acceptClicks && tracking) {
                    disableExistingTracking();
                }
            }

            function getCoordinateFromEventTarget(event) {
                return {
                    row: parseInt(event.currentTarget.getAttribute('data-ws-row')),
                    column: parseInt(event.currentTarget.getAttribute('data-ws-column'))
                };
            }

            function clearSelectedWord() {
                gridTableManager.removeSelectedStyleFromCoordinates(selectedCells);
                selectedCells = [];
                controller.currentWordBackward = '';
                controller.currentWordForward = '';
                controller.forwardIsWord = false;
                controller.backwardIsWord = false;
                selectContext.clearRect(0, 0, selectCanvas.width, selectCanvas.height);
            }

            function submitSelectedWord() {
                if (controller.forwardIsWord || controller.backwardIsWord) {
                    var cells = [];
                    if (controller.backwardIsWord) {
                        selectedCells.reverse();
                    }
                    var last;
                    angular.forEach(originalSelectedCells, function (cell, index) {
                        if (index === 0) {
                            cells.push(cell);
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
                    jtbBootstrapGameActions.wrapActionOnGame(
                        $http.put(
                            jtbPlayerService.currentPlayerBaseURL() + '/game/' + controller.game.id + '/find',
                            cells
                        )
                    );
                }
            }

            function computeTargetEndPoint(event) {
                var coordinate = getCoordinateFromEventTarget(event);
                var dRow = selectStart.row - coordinate.row;
                var dCol = coordinate.column - selectStart.column;
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
                        targetRow = coordinate.row;
                        targetColumn = selectStart.column;
                        break;
                    case 2:
                    case -2:
                        targetRow = selectStart.row;
                        targetColumn = coordinate.column;
                        break;
                    default:
                        if (Math.abs(dRow) > Math.abs(dCol)) {
                            targetRow = selectStart.row - dRow;
                            targetColumn = selectStart.column + (Math.abs(dRow) * Math.sign(dCol));
                        } else {
                            targetRow = selectStart.row - (Math.abs(dCol) * Math.sign(dRow));
                            targetColumn = selectStart.column + dCol;
                        }
                }
                return {row: targetRow, column: targetColumn};
            }

            controller.zoomIn = function (amount) {
                controller.fontSize = fontSizeManager.increaseFontSize(amount);
            };

            controller.zoomOut = function (amount) {
                controller.fontSize = fontSizeManager.decreaseFontSize(amount);
            };

            controller.onMouseEntersTable = function () {
                tracking = trackingPaused;
            };

            controller.onMouseExitsTable = function () {
                trackingPaused = tracking;
                tracking = false;
            };

            controller.onMouseClick = function (event) {
                if (!acceptClicks) {
                    return;
                }
                tracking = !tracking;
                if (tracking) {
                    var coordinate = getCoordinateFromEventTarget(event);
                    if (controller.grid[coordinate.row][coordinate.column] === ' ') {
                        tracking = false;
                        return;
                    }
                    selectStart = coordinate;
                    selectEnd = coordinate;
                    controller.updateSelection();
                } else {
                    submitSelectedWord();
                    clearSelectedWord();
                }
            };

            function drawSelectionHighlight() {
                selectCanvas = angular.element('#select-canvas')[0];
                selectContext = selectCanvas.getContext('2d');
                selectContext.clearRect(0, 0, selectCanvas.width, selectCanvas.height);
                selectContext.beginPath();
                canvasLineDrawer.drawLine(
                    selectContext,
                    selectedCells[0],
                    selectedCells[selectedCells.length - 1],
                    selectCanvas.height / rows,
                    selectCanvas.width / columns,
                    SELECT_COLOR
                );
                selectContext.closePath();
            }

            controller.updateSelection = function () {
                gridTableManager.removeSelectedStyleFromCoordinates(selectedCells);
                var selectedData = gridTableManager.calculateSelected(selectStart, selectEnd);
                selectedCells = selectedData.selectedCoordinates;
                originalSelectedCells = selectedData.originalCoordinates;
                controller.currentWordForward = selectedData.wordForward;
                controller.currentWordBackward = selectedData.wordReversed;
                gridTableManager.addSelectedStyleToCoordinates(selectedCells);
                //noinspection JSUnresolvedVariable
                controller.backwardIsWord = controller.game.wordsToFind.indexOf(controller.currentWordBackward) > -1;
                //noinspection JSUnresolvedVariable
                controller.forwardIsWord = controller.game.wordsToFind.indexOf(controller.currentWordForward) > -1;
                drawSelectionHighlight();
            };

            controller.onMouseMove = function (event) {
                if (!tracking) {
                    return;
                }
                var target = computeTargetEndPoint(event);
                if (selectEnd.row !== target.row || selectEnd.column !== target.column) {
                    selectEnd = target;
                    controller.updateSelection();
                }
            };

            $scope.$on('gameUpdated', function (message, oldGame) {
                //noinspection JSUnresolvedVariable
                if (oldGame.id === controller.game.id) {
                    updateControllerFromGame();
                }
            });

            $scope.$on('GridOffsetsChanged', function () {
                disableExistingTracking();
            });

            gridOffsetTracker.reset();
            updateControllerFromGame();

        }
    ]
);
