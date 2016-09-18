'use strict';

angular.module('twsUI.services').factory('gridOffsetTracker',
    ['$rootScope',
        function ($rootScope) {
            var BROADCAST = 'GridOffsetsChanged';
            var columnOffset = 0;
            var rowOffset = 0;
            var rows = 0;
            var columns = 0;
            return {
                reset: function () {
                    columnOffset = 0;
                    rowOffset = 0;
                },
                gridSize: function (newRows, newColumns) {
                    rows = newRows;
                    columns = newColumns;
                },
                shiftLeft: function (amount) {
                    columnOffset -= amount;
                    if (columnOffset <= -columns) {
                        columnOffset += columns;
                    }
                    $rootScope.$broadcast(BROADCAST);
                },
                shiftRight: function (amount) {
                    columnOffset += amount;
                    if (columnOffset >= columns) {
                        columnOffset -= columns;
                    }
                    $rootScope.$broadcast(BROADCAST);
                },
                shiftUp: function (amount) {
                    rowOffset -= amount;
                    if (rowOffset <= -rows) {
                        rowOffset += rows;
                    }
                    $rootScope.$broadcast(BROADCAST);
                },
                shiftDown: function (amount) {
                    rowOffset += amount;
                    if (rowOffset >= rows) {
                        rowOffset -= rows;
                    }
                    $rootScope.$broadcast(BROADCAST);
                },

                getOriginalRow: function (offsetRow) {
                    var originalRow = offsetRow - rowOffset;
                    if (originalRow < 0) {
                        originalRow += rows;
                    }
                    if (originalRow >= rows) {
                        originalRow -= rows;
                    }
                    return originalRow;
                },

                getOriginalColumn: function (offsetColumn) {
                    var originalColumn = offsetColumn - columnOffset;
                    if (originalColumn < 0) {
                        originalColumn += columns;
                    }
                    if (originalColumn >= columns) {
                        originalColumn -= columns;
                    }
                    return originalColumn;
                },

                getOffsetRow: function (originalRow) {
                    var offSetRow = originalRow + rowOffset;
                    if (offSetRow < 0) {
                        offSetRow += rows;
                    }
                    if (offSetRow >= rows) {
                        offSetRow -= rows;
                    }
                    return offSetRow;
                },

                getOffsetColumn: function (originalColumn) {
                    var offSetColumn = originalColumn + columnOffset;
                    if (offSetColumn < 0) {
                        offSetColumn += columns;
                    }
                    if (offSetColumn >= columns) {
                        offSetColumn -= columns;
                    }
                    return offSetColumn;
                }
            };
        }
    ]
);
