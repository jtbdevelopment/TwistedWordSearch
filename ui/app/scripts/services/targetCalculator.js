'use strict';

angular.module('twsUI.services').factory('targetCalculator',
    [
        function () {
            return {
                calculateTargetCell: function (startCoordinate, currentCoordinate) {
                    var rowDelta = startCoordinate.row - currentCoordinate.row;
                    var columnDelta = currentCoordinate.column - startCoordinate.column;
                    // up = 0, down = 180
                    // left to right = 90, right to left = -90
                    // left to right up = 45, down = 135
                    // right to left up = -45, down = -135
                    var angle = Math.atan2(columnDelta, rowDelta) * 180 / Math.PI;

                    var roundedAngle = Math.round(angle / 45);
                    var targetRow, targetColumn;
                    switch (roundedAngle) {
                        case 0:
                        case 4:
                            targetRow = currentCoordinate.row;
                            targetColumn = startCoordinate.column;
                            break;
                        case 2:
                        case -2:
                            targetRow = startCoordinate.row;
                            targetColumn = currentCoordinate.column;
                            break;
                        default:
                            if (Math.abs(rowDelta) > Math.abs(columnDelta)) {
                                targetRow = startCoordinate.row - rowDelta;
                                targetColumn = startCoordinate.column + (Math.abs(rowDelta) * Math.sign(columnDelta));
                            } else {
                                targetRow = startCoordinate.row - (Math.abs(columnDelta) * Math.sign(rowDelta));
                                targetColumn = startCoordinate.column + columnDelta;
                            }
                    }
                    return {row: targetRow, column: targetColumn};
                }
            };
        }
    ]
);
