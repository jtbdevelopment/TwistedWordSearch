'use strict';

angular.module('twsUI.services').factory('gridTableManager',
    ['gridOffsetTracker', '$rootScope',
        function (gridOffsetTracker, $rootScope) {
            var CURRENT_SELECTION = 'current-selection ';
            var PART_OF_FOUND_WORD = 'found-word ';

            var tableCells;
            var tableCellStyles;
            var rows;
            var columns;

            function reset() {
                tableCells = [];
                tableCellStyles = [];
            }

            function resizeForGameGrid() {
                angular.forEach(currentGame.grid, function () {
                    tableCells.push(new Array(columns));
                    tableCellStyles.push(new Array(columns));
                });
            }

            function placeLettersWithEmptyStyle(grid) {
                angular.forEach(grid, function (row, index) {
                    var offSetRow = gridOffsetTracker.getOffsetRow(index);
                    angular.forEach(row, function (column, index) {
                        var offSetColumn = gridOffsetTracker.getOffsetColumn(index);

                        tableCells[offSetRow][offSetColumn] = column;
                        tableCellStyles[offSetRow][offSetColumn] = '';
                    });
                });
            }

            function addStyleToCell(offsetRow, offsetColumn, style) {
                tableCellStyles[offsetRow][offsetColumn] += style;
            }

            function removeStyleFromCell(offsetRow, offsetColumn, style) {
                tableCellStyles[offsetRow][offsetColumn] = tableCellStyles[offsetRow][offsetColumn].replace(style, '');
            }

            function markFoundWordLocations(game) {
                //noinspection JSUnresolvedVariable
                angular.forEach(game.foundWordLocations, function (cells) {
                    angular.forEach(cells, function (cell) {
                        var offSetRow = gridOffsetTracker.getOffsetRow(cell.row);
                        var offSetColumn = gridOffsetTracker.getOffsetColumn(cell.column);
                        addStyleToCell(offSetRow, offSetColumn, PART_OF_FOUND_WORD);
                    });
                });
            }

            function getOriginalCoordinate(offsetCoordinate) {
                return {
                    row: gridOffsetTracker.getOriginalRow(offsetCoordinate.row),
                    column: gridOffsetTracker.getOriginalColumn(offsetCoordinate.column)
                };
            }

            function updateForGame() {
                placeLettersWithEmptyStyle(currentGame.grid);
                markFoundWordLocations(currentGame);
                return {cells: tableCells, styles: tableCellStyles};
            }

            var currentGame;
            reset();

            $rootScope.$on('GridOffsetsChanged', function () {
                if (angular.isDefined(currentGame)) {
                    updateForGame();
                }
            });

            return {
                updateForGame: function (game, gameRows, gameColumns) {
                    currentGame = game;
                    rows = gameRows;
                    columns = gameColumns;
                    reset();
                    resizeForGameGrid();
                    return updateForGame();
                },

                addSelectedStyleToCoordinates: function (offsetCoordinates) {
                    angular.forEach(offsetCoordinates, function (offsetCoordinate) {
                        addStyleToCell(offsetCoordinate.row, offsetCoordinate.column, CURRENT_SELECTION);
                    });
                },

                removeSelectedStyleFromCoordinates: function (offsetCoordinates) {
                    angular.forEach(offsetCoordinates, function (offsetCoordinate) {
                        removeStyleFromCell(offsetCoordinate.row, offsetCoordinate.column, CURRENT_SELECTION);
                    });
                },

                calculateSelected: function (startCoordinate, targetCoordinate) {
                    var selectedCoordinates = [startCoordinate];
                    var originalSelectedCoordinates = [getOriginalCoordinate(startCoordinate)];

                    var coordinate = {row: startCoordinate.row, column: startCoordinate.column};
                    var workingWordForward = tableCells[coordinate.row][coordinate.column];
                    var workingWordReversed = tableCells[coordinate.row][coordinate.column];

                    while (coordinate.row !== targetCoordinate.row || coordinate.column !== targetCoordinate.column) {
                        coordinate.row += Math.sign(targetCoordinate.row - coordinate.row);
                        coordinate.column += Math.sign(targetCoordinate.column - coordinate.column);
                        if (coordinate.row < 0 ||
                            coordinate.column < 0 ||
                            coordinate.column >= columns ||
                            coordinate.row >= rows ||
                            tableCells[coordinate.row][coordinate.column] === ' ') {
                            break;
                        }
                        selectedCoordinates.push({row: coordinate.row, column: coordinate.column});
                        originalSelectedCoordinates.push(getOriginalCoordinate(coordinate));
                        workingWordForward += tableCells[coordinate.row][coordinate.column];
                        workingWordReversed = tableCells[coordinate.row][coordinate.column] + workingWordReversed;
                    }
                    return {
                        selectedCoordinates: selectedCoordinates,
                        originalCoordinates: originalSelectedCoordinates,
                        wordForward: workingWordForward,
                        wordReversed: workingWordReversed
                    };
                }
            };
        }
    ]
);