'use strict';

describe('Service: canvasLineDrawer', function () {
    // load the controller's module
    beforeEach(module('twsUI.services'));
    var service;
    beforeEach(inject(function ($injector) {
        service = $injector.get('canvasLineDrawer');
    }));

    it('draws a line', function() {
        var context = {
            lineWidth: undefined,
            strokeStyle: undefined,
            lineCap: undefined,
            moveTo: jasmine.createSpy('moveTo'),
            lineTo: jasmine.createSpy('lineTo'),
            stroke: jasmine.createSpy('stroke')
        };
        var color = '#00ff12';
        service.drawLine(context, {row: 4, column: 6}, {row: 8, column: 7}, 17, 13, color);

        expect(context.lineWidth).toEqual((17/2 + 13/2) / 2);
        expect(context.strokeStyle).toEqual(color);
        expect(context.lineCap).toEqual('round');
        var expectedStartX = 6 * 13 + (13/2);
        var expectedStartY = 4 * 17 + (17/2);
        expect(context.moveTo).toHaveBeenCalledWith(expectedStartX, expectedStartY);
        expect(context.lineTo).toHaveBeenCalledWith((7-6) * 13 + expectedStartX, (8 - 4) * 17 + expectedStartY);
        expect(context.stroke).toHaveBeenCalled();
    });

});

