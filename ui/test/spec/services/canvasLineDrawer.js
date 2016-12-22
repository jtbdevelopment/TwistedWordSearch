'use strict';

describe('Service: canvasLineDrawer', function () {
    beforeEach(module('twsUI.services'));
    var service;
    beforeEach(inject(function ($injector) {
        service = $injector.get('canvasLineDrawer');
    }));

    it('draws a line', function () {
        var context = {
            lineWidth: undefined,
            strokeStyle: undefined,
            lineCap: undefined,
            beginPath: jasmine.createSpy('beginPath'),
            closePath: jasmine.createSpy('closePath'),
            moveTo: jasmine.createSpy('moveTo'),
            lineTo: jasmine.createSpy('lineTo'),
            stroke: jasmine.createSpy('stroke')
        };
        var color = '#00ff12';
        service.drawLine(context, {row: 4, column: 6}, {row: 8, column: 7}, 17, 13, color);

        expect(context.beginPath).toHaveBeenCalled();
        expect(context.lineWidth).toEqual((17 / 2 + 13 / 2) / 2);
        expect(context.strokeStyle).toEqual(color);
        expect(context.lineCap).toEqual('round');
        var expectedStartX = 6 * 13 + (13 / 2);
        var expectedStartY = 4 * 17 + (17 / 2);
        expect(context.moveTo).toHaveBeenCalledWith(expectedStartX, expectedStartY);
        expect(context.lineTo).toHaveBeenCalledWith((7 - 6) * 13 + expectedStartX, (8 - 4) * 17 + expectedStartY);
        expect(context.stroke).toHaveBeenCalled();
        expect(context.closePath).toHaveBeenCalled();
    });

    it('draws a square', function () {
        var context = {
            fillStyle: undefined,
            beginPath: jasmine.createSpy('beginPath'),
            closePath: jasmine.createSpy('closePath'),
            fillRect: jasmine.createSpy('fillRect')
        };
        service.drawSquare(context, {row: 4, column: 6}, 2, 17, 13);

        expect(context.beginPath).toHaveBeenCalled();
        expect(context.fillStyle).toEqual('white');
        var expectedStartX = 6 * 13 - (13 * 2);
        var expectedStartY = 4 * 17 - (17 * 2);
        expect(context.fillRect).toHaveBeenCalledWith(expectedStartX, expectedStartY, (13 * 5), (17 * 5));
        expect(context.closePath).toHaveBeenCalled();
    });
});

