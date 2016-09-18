'use strict';

angular.module('twsUI.services').factory('foundWordsCanvasManager',
    ['gridOffsetTracker', '$rootScope', 'canvasLineDrawer',
        function (gridOffsetTracker, $rootScope, canvasLineDrawer) {

            var FOUND_COLOR = '#C1D37F';
            var currentGame;
            var rows, columns;
            var canvas, context;

            function calculateLinesToDraw() {
                var linesToDraw = [];
                //noinspection JSUnresolvedVariable
                angular.forEach(currentGame.foundWordLocations, function (cells) {
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
                return linesToDraw;
            }

            function drawLines(linesToDraw) {
                var width = canvas.width / columns;
                var height = canvas.height / rows;
                context.clearRect(0, 0, canvas.width, canvas.height);
                context.beginPath();
                angular.forEach(linesToDraw, function (lineToDraw) {
                    canvasLineDrawer.drawLine(context, lineToDraw.from, lineToDraw.to, height, width, FOUND_COLOR);
                });
                context.closePath();
            }

            function highlightFoundWords() {
                drawLines(calculateLinesToDraw());
            }

            $rootScope.$on('GridOffsetsChanged', function () {
                if (angular.isDefined(currentGame)) {
                    highlightFoundWords();
                }
            });

            return {
                updateForGame: function (game, gameRows, gameColumns) {
                    currentGame = game;
                    rows = gameRows;
                    columns = gameColumns;
                    canvas = angular.element('#found-canvas')[0];
                    context = canvas.getContext('2d');
                    highlightFoundWords();
                }
            };
        }
    ]
);
