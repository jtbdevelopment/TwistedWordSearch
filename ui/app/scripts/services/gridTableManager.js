'use strict';

angular.module('twsUI.services').factory('gridTableManager',
    [ 'gridOffsetTracker', '$rootScope',
        function (gridOffsetTracker, $rootScope) {
            var CURRENT_SELECTION = 'current-selection ';
            var PART_OF_FOUND_WORD = 'found-word ';

            var tableCells;
            var tableCellStyles;

            function reset() {
                tableCells = [];
                tableCellStyles = [];
            }

            function resizeForGameGrid() {
                var length = currentGame.grid[0].length;
                angular.forEach(currentGame.grid, function () {
                    tableCells.push(new Array(length));
                    tableCellStyles.push(new Array(length));
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

            function updateForGame() {
                placeLettersWithEmptyStyle(currentGame.grid);
                markFoundWordLocations(currentGame);
                return {cells: tableCells, styles: tableCellStyles};
            }

            var currentGame;
            reset();

            $rootScope.$on('GridOffsetsChanged', function() {
                if(angular.isDefined(currentGame)) {
                    updateForGame();
                }
            });

            return {
                updateForGame: function(game) {
                    currentGame = game;
                    reset();
                    resizeForGameGrid();
                    return updateForGame();
                },

                markCoordinatesAsSelected: function(offsetCoordinates) {
                    angular.forEach(offsetCoordinates, function (offsetCoordinate) {
                        addStyleToCell(offsetCoordinate.row, offsetCoordinate.column, CURRENT_SELECTION);
                    });
                },

                unmarkCoordinatesAsSelected: function(offsetCoordinates) {
                    angular.forEach(offsetCoordinates, function (offsetCoordinate) {
                        removeStyleFromCell(offsetCoordinate.row, offsetCoordinate.column, CURRENT_SELECTION);
                    });
                }
            };
        }
    ]
);