'use strict';

describe('Service: gridOffsetTracker', function () {
    // load the controller's module
    beforeEach(module('twsUI.services'));

    var service;
    beforeEach(inject(function ($injector) {
        service = $injector.get('gridOffsetTracker');
    }));

    beforeEach(function() {
        service.gridSize(20, 30);
    });

    it('no adjustments necessary', function() {
        expect(service.getOriginalRow(5)).toEqual(5);
        expect(service.getOffsetRow(2)).toEqual(2);
        expect(service.getOriginalColumn(4)).toEqual(4);
        expect(service.getOffsetColumn(8)).toEqual(8);
    });

    it('simple adjustments', function() {
        service.shiftUp(2);
        service.shiftRight(5);

        expect(service.getOriginalRow(5)).toEqual(7);
        expect(service.getOffsetRow(2)).toEqual(0);
        expect(service.getOriginalColumn(8)).toEqual(3);
        expect(service.getOffsetColumn(8)).toEqual(13);

    });

    it('boundary crossing adjustments', function() {
        service.shiftUp(2);
        service.shiftRight(5);

        expect(service.getOriginalRow(18)).toEqual(0);
        expect(service.getOffsetRow(1)).toEqual(19);
        expect(service.getOriginalColumn(1)).toEqual(26);
        expect(service.getOffsetColumn(27)).toEqual(2);
    });

    it('more boundary crossing adjustments', function() {
        service.shiftDown(2);
        service.shiftLeft(5);

        expect(service.getOriginalRow(0)).toEqual(18);
        expect(service.getOffsetRow(19)).toEqual(1);
        expect(service.getOriginalColumn(29)).toEqual(4);
        expect(service.getOffsetColumn(2)).toEqual(27);
    });

    it('boundary crossing adjustments after many shifts', function() {
        service.shiftUp(2);
        service.shiftDown(1);
        service.shiftUp(21);
        service.shiftRight(5);
        service.shiftLeft(3);
        service.shiftRight(33);

        expect(service.getOriginalRow(18)).toEqual(0);
        expect(service.getOffsetRow(1)).toEqual(19);
        expect(service.getOriginalColumn(1)).toEqual(26);
        expect(service.getOffsetColumn(27)).toEqual(2);

    });

    it('boundary crossing additional boundary crossing adjustments', function() {
        service.shiftUp(2);
        service.shiftDown(22);
        service.shiftRight(5);
        service.shiftLeft(35);

        expect(service.getOriginalRow(18)).toEqual(18);
        expect(service.getOffsetRow(1)).toEqual(1);
        expect(service.getOriginalColumn(1)).toEqual(1);
        expect(service.getOffsetColumn(27)).toEqual(27);

    });

    it('adjustments after a reset', function() {
        service.shiftUp(2);
        service.shiftRight(5);

        expect(service.getOriginalRow(18)).toEqual(0);
        expect(service.getOffsetRow(1)).toEqual(19);
        expect(service.getOriginalColumn(1)).toEqual(26);
        expect(service.getOffsetColumn(27)).toEqual(2);

        service.reset();

        expect(service.getOriginalRow(18)).toEqual(18);
        expect(service.getOffsetRow(1)).toEqual(1);
        expect(service.getOriginalColumn(1)).toEqual(1);
        expect(service.getOffsetColumn(27)).toEqual(27);
    });
});
