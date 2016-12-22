'use strict';

angular.module('twsUI.services').factory('foundWordsCanvasManager',
    ['gridOffsetTracker', '$rootScope', 'canvasLineDrawer', '$timeout',
        function (gridOffsetTracker, $rootScope, canvasLineDrawer, $timeout) {

            var colors = {};
            var currentGame;
            var rows, columns;
            var canvas, context;

            function calculateLinesToDraw() {
                var linesToDraw = [];
                var wordsFoundBy = {};
                angular.forEach(currentGame.wordsFoundByPlayer, function (words, player) {
                    angular.forEach(words, function (word) {
                        wordsFoundBy[word] = player;
                    });
                });
                //noinspection JSUnresolvedVariable
                angular.forEach(currentGame.foundWordLocations, function (cells, word) {
                    var currentLine = {color: colors[wordsFoundBy[word]]};

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
                                currentLine = {color: currentLine.color};
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
                angular.forEach(linesToDraw, function (lineToDraw) {
                    canvasLineDrawer.drawLine(context, lineToDraw.from, lineToDraw.to, height, width, lineToDraw.color);
                });
            }

            function highlightHints() {
                var width = canvas.width / columns;
                var height = canvas.height / rows;
                angular.forEach(currentGame.hints, function (hint) {
                    //  TODO - what if box now spans sides
                    var thisCoordinate = {
                        row: gridOffsetTracker.getOffsetRow(hint.row),
                        column: gridOffsetTracker.getOffsetColumn(hint.column)
                    };
                    canvasLineDrawer.drawSquare(context, thisCoordinate, 1, height, width);
                });
            }

            //  Timeout exists for slower browsers - sometimes canvas not ready right away
            var timeout = 1000;
            function highlightFoundWords() {
                $timeout(function() {
                    canvas = angular.element('#found-canvas')[0];
                    context = canvas.getContext('2d');
                    drawLines(calculateLinesToDraw());
                    highlightHints();
                    timeout = 0;
                }, timeout);
            }

            $rootScope.$on('GridOffsetsChanged', function () {
                if (angular.isDefined(currentGame)) {
                    highlightFoundWords();
                }
            });

            return {
                updateForGame: function (game, gameRows, gameColumns, playerColors) {
                    currentGame = game;
                    colors = playerColors;
                    rows = gameRows;
                    columns = gameColumns;
                    timeout = 1000;
                    highlightFoundWords();
                }
            };
        }
    ]
);
