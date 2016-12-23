'use strict';

angular.module('twsUI.services').factory('canvasLineDrawer',
    [
        function () {
            return {
                drawLine: function (context, startCell, endCell, cellHeight, cellWidth, color) {
                    var halfWidth = cellWidth / 2;
                    var halfHeight = cellHeight / 2;
                    var startX = (startCell.column * cellWidth) + halfWidth;
                    var startY = (startCell.row * cellHeight) + halfHeight;
                    var endX = ((endCell.column - startCell.column) * cellWidth) + startX;
                    var endY = ((endCell.row - startCell.row) * cellHeight) + startY;
                    context.beginPath();
                    context.lineWidth = (halfHeight + halfWidth) / 2;
                    context.strokeStyle = color;
                    context.lineCap = 'round';
                    context.moveTo(startX, startY);
                    context.lineTo(endX, endY);
                    context.stroke();
                    context.closePath();
                },
                drawSquare: function (context, centerCell, cellHeight, cellWidth) {
                    var startX = (centerCell.column * cellWidth);
                    var startY = (centerCell.row * cellHeight);
                    context.beginPath();
                    //  TODO - maybe another color
                    context.fillStyle = 'pink';
                    context.fillRect(
                        startX,
                        startY,
                        cellWidth,
                        cellHeight);
                    context.closePath();
                }
            };
        }
    ]
);
