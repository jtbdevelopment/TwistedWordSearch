'use strict';

//  TODO - show messages for completed games
angular.module('twsUI').controller('PlayCtrl',
    ['$scope', '$http', '$routeParams',
        'gridOffsetTracker', 'gridTableManager', 'targetCalculator', 'fontSizeManager',
        'foundWordsCanvasManager', 'canvasLineDrawer', 'featureDescriber',
        'jtbGameCache', 'jtbPlayerService', 'jtbBootstrapGameActions', 'gameAnimations',
        function ($scope, $http, $routeParams,
                  gridOffsetTracker, gridTableManager, targetCalculator, fontSizeManager,
                  foundWordsCanvasManager, canvasLineDrawer, featureDescriber,
                  jtbGameCache, jtbPlayerService, jtbBootstrapGameActions, gameAnimations) {
            var controller = this;

            var PLAYER_COLORS = [
                '#C1D37F',
                '#7C3238',
                '#F0E2A3',
                '#FFFFFF',
                '#A9A9A9'
            ];
            var PLAYER_FONT_COLORS = [
                'black',
                'white',
                'black',
                'black',
                'white'
            ];
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

            controller.playerColors = {};
            controller.playerFontColors = {};

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
                controller.playerColors = {};
                var colorIndex = 0;
                angular.forEach(controller.game.players, function (name, md5) {
                    controller.playerColors[md5] = PLAYER_COLORS[colorIndex];
                    controller.playerFontColors[md5] = PLAYER_FONT_COLORS[colorIndex];
                    colorIndex += 1;
                });
                rows = controller.game.grid.length;
                columns = controller.game.grid[0].length;
                gridOffsetTracker.gridSize(rows, columns);
                var grid = gridTableManager.updateForGame(controller.game);
                controller.grid = grid.cells;
                controller.cellStyles = grid.styles;
                foundWordsCanvasManager.updateForGame(controller.game, rows, columns, controller.playerColors);
                controller.showQuit = (controller.game.gamePhase === 'Playing');
                controller.showRematch = (controller.game.gamePhase === 'RoundOver');
                acceptClicks = controller.showQuit;
                if (!acceptClicks && tracking) {
                    disableExistingTracking();
                }
                controller.updateSelection();
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
                if (tracking) {
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
                }
            };

            controller.onMouseMove = function (event) {
                if (!tracking) {
                    return;
                }
                var target = targetCalculator.calculateTargetCell(selectStart, getCoordinateFromEventTarget(event));
                if (selectEnd.row !== target.row || selectEnd.column !== target.column) {
                    selectEnd = target;
                    controller.updateSelection();
                }
            };

            $scope.$on('gameUpdated', function (message, oldGame, newGame) {
                //noinspection JSUnresolvedVariable
                console.log(JSON.stringify(oldGame));
                console.log(JSON.stringify(newGame));
                if (oldGame.id === controller.game.id) {
                    updateControllerFromGame();
                    gameAnimations.animateGameUpdate(controller, oldGame, newGame);
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
