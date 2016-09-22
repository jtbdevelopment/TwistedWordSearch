'use strict';

describe('Service: targetCalculator', function () {
    // load the controller's module
    beforeEach(module('twsUI.services'));
    var service;
    beforeEach(inject(function ($injector) {
        service = $injector.get('targetCalculator');
    }));

    it('test up', function () {
        expect(service.calculateTargetCell({row: 4, column: 5}, {row: 1, column: 5})).toEqual({row: 1, column: 5});
    });

    it('test down', function () {
        expect(service.calculateTargetCell({row: 4, column: 5}, {row: 7, column: 5})).toEqual({row: 7, column: 5});
    });

    it('test left', function () {
        expect(service.calculateTargetCell({row: 4, column: 5}, {row: 4, column: 1})).toEqual({row: 4, column: 1});
    });

    it('test right', function () {
        expect(service.calculateTargetCell({row: 4, column: 5}, {row: 4, column: 8})).toEqual({row: 4, column: 8});
    });

    it('test right up, more right', function () {
        expect(service.calculateTargetCell({row: 5, column: 5}, {row: 3, column: 8})).toEqual({row: 2, column: 8});
    });

    it('test right up, more up', function () {
        expect(service.calculateTargetCell({row: 5, column: 5}, {row: 1, column: 7})).toEqual({row: 1, column: 9});
    });

    it('test right down, more right', function () {
        expect(service.calculateTargetCell({row: 5, column: 5}, {row: 7, column: 8})).toEqual({row: 8, column: 8});
    });

    it('test right down, more down', function () {
        expect(service.calculateTargetCell({row: 5, column: 5}, {row: 9, column: 7})).toEqual({row: 9, column: 9});
    });

    it('test left up, more right', function () {
        expect(service.calculateTargetCell({row: 5, column: 5}, {row: 3, column: 2})).toEqual({row: 2, column: 2});
    });

    it('test left up, more up', function () {
        expect(service.calculateTargetCell({row: 5, column: 5}, {row: 1, column: 3})).toEqual({row: 1, column: 1});
    });

    it('test left down, more right', function () {
        expect(service.calculateTargetCell({row: 5, column: 5}, {row: 7, column: 2})).toEqual({row: 8, column: 2});
    });

    it('test left down, more down', function () {
        expect(service.calculateTargetCell({row: 5, column: 5}, {row: 9, column: 3})).toEqual({row: 9, column: 1});
    });
});
