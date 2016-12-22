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
                drawSquare: function (context, centerCell, extent, cellHeight, cellWidth) {
                    var startX = (centerCell.column * cellWidth);
                    var startY = (centerCell.row * cellHeight);
                    context.beginPath();
                    //  TODO - maybe another color
                    context.fillStyle = 'white';
                    context.fillRect(
                        startX - Math.floor(cellWidth * extent),
                        startY - Math.floor(cellHeight * extent),
                        Math.ceil(cellWidth * (extent * 2 + 1)),
                        Math.ceil(cellHeight * (extent * 2 + 1)));
                    context.closePath();
                }
            };
        }
    ]
);
