'use strict';

//  TODO - more hint testing required for spanning sides
describe('Service: foundWordsCanvasManager', function () {
    beforeEach(module('twsUI.services'));

    var canvasLineDrawer = {
        drawLine: jasmine.createSpy('drawLine'),
        drawSquare: jasmine.createSpy('drawSquare')
    };
    beforeEach(module(function ($provide) {
        $provide.factory('canvasLineDrawer', [function () {
            return canvasLineDrawer;
        }]);
    }));

    var context = {
        clearRect: jasmine.createSpy('clearRect'),
        aVariable: 'X'
    };

    var canvas = {
        width: 322,
        height: 408,
        getContext: function (type) {
            expect(type).toEqual('2d');
            return context;
        }
    };
    var ngElementFake = function (name) {
        expect(name).toEqual('#found-canvas');
        return [canvas];
    };

    var service, gridOffsetTracker, elementSpy, $timeout;
    beforeEach(inject(function ($injector, _$timeout_) {
        $timeout = _$timeout_;
        elementSpy = spyOn(angular, 'element').and.callFake(ngElementFake);
        //  not mocking, functionality is straight forward enough while dealing with edge cases
        gridOffsetTracker = $injector.get('gridOffsetTracker');
        service = $injector.get('foundWordsCanvasManager');
        context.clearRect.calls.reset();
        canvasLineDrawer.drawLine.calls.reset();
    }));

    afterEach(function () {
        elementSpy.and.callThrough();
    });

    var game = {
        grid: [
            ['A', 'B', 'C', 'D', 'E'],
            ['F', 'G', 'H', ' ', 'I'],
            ['J', 'K', 'L', ' ', 'M'],
            ['N', 'O', 'P', 'Q', 'R']
        ],
        foundWordLocations: {
            'NO': [{row: 3, column: 0}, {row: 3, column: 1}],
            'GAR': [{row: 1, column: 1}, {row: 0, column: 0}, {row: 3, column: 4}]
        },
        wordsFoundByPlayer: {
            'md1': ['NO'],
            'md2': ['GAR']
        },
        hints: [
            {
                row: 1,
                column: 2
            },
            {
                row: 3,
                column: 4
            }
        ]
    };

    var assignedColors = {
        'md1': '#003200',
        'md2': '#a200f2'
    };
    beforeEach(function () {
        gridOffsetTracker.reset();
        gridOffsetTracker.gridSize(4, 5);
    });

    it('simple compute lines and rects', function () {
        service.updateForGame(game, 4, 5, assignedColors);
        $timeout.flush();
        expect(context.clearRect).toHaveBeenCalledWith(0, 0, canvas.width, canvas.height);
        expect(canvasLineDrawer.drawSquare).toHaveBeenCalledWith(
            context,
            {row: 1, column: 2},
            1,
            102,
            64.4
        );
        expect(canvasLineDrawer.drawSquare).toHaveBeenCalledWith(
            context,
            {row: 3, column: 4},
            1,
            102,
            64.4
        );
        expect(canvasLineDrawer.drawLine).toHaveBeenCalledWith(
            context,
            {row: 3, column: 0},
            {row: 3, column: 1},
            102,
            64.4,
            '#003200');
        expect(canvasLineDrawer.drawLine).toHaveBeenCalledWith(
            context,
            {row: 1, column: 1},
            {row: 0, column: 0},
            102,
            64.4,
            '#a200f2');
        expect(canvasLineDrawer.drawLine).toHaveBeenCalledWith(
            context,
            {row: 3, column: 4},
            {row: 3, column: 4},
            102,
            64.4,
            '#a200f2');
    });

    it('simple lines with offset', function () {
        gridOffsetTracker.shiftUp(1);
        gridOffsetTracker.shiftRight(2);
        service.updateForGame(game, 4, 5, assignedColors);
        $timeout.flush();
        expect(context.clearRect).toHaveBeenCalledWith(0, 0, canvas.width, canvas.height);
        expect(canvasLineDrawer.drawLine).toHaveBeenCalledWith(
            context,
            {row: 2, column: 2},
            {row: 2, column: 3},
            102,
            64.4,
            '#003200');
        expect(canvasLineDrawer.drawLine).toHaveBeenCalledWith(
            context,
            {row: 0, column: 3},
            {row: 0, column: 3},
            102,
            64.4,
            '#a200f2');
        expect(canvasLineDrawer.drawLine).toHaveBeenCalledWith(
            context,
            {row: 3, column: 2},
            {row: 2, column: 1},
            102,
            64.4,
            '#a200f2');
    });

    it('recomputes canvas on offset changes', function () {
        service.updateForGame(game, 4, 5, assignedColors);
        $timeout.flush();
        expect(context.clearRect).toHaveBeenCalledWith(0, 0, canvas.width, canvas.height);
        expect(canvasLineDrawer.drawLine).toHaveBeenCalledWith(
            context,
            {row: 3, column: 0},
            {row: 3, column: 1},
            102,
            64.4,
            '#003200');
        expect(canvasLineDrawer.drawLine).toHaveBeenCalledWith(
            context,
            {row: 1, column: 1},
            {row: 0, column: 0},
            102,
            64.4,
            '#a200f2');
        expect(canvasLineDrawer.drawLine).toHaveBeenCalledWith(
            context,
            {row: 3, column: 4},
            {row: 3, column: 4},
            102,
            64.4,
            '#a200f2');

        context.clearRect.calls.reset();
        canvasLineDrawer.drawLine.calls.reset();

        gridOffsetTracker.shiftUp(1);
        $timeout.flush();
        expect(context.clearRect).toHaveBeenCalledWith(0, 0, canvas.width, canvas.height);
        // ignoring intermediate result line details
        context.clearRect.calls.reset();
        canvasLineDrawer.drawLine.calls.reset();

        gridOffsetTracker.shiftRight(2);
        $timeout.flush();

        expect(context.clearRect).toHaveBeenCalledWith(0, 0, canvas.width, canvas.height);
        expect(canvasLineDrawer.drawLine).toHaveBeenCalledWith(
            context,
            {row: 2, column: 2},
            {row: 2, column: 3},
            102,
            64.4,
            '#003200');
        expect(canvasLineDrawer.drawLine).toHaveBeenCalledWith(
            context,
            {row: 0, column: 3},
            {row: 0, column: 3},
            102,
            64.4,
            '#a200f2');
        expect(canvasLineDrawer.drawLine).toHaveBeenCalledWith(
            context,
            {row: 3, column: 2},
            {row: 2, column: 1},
            102,
            64.4,
            '#a200f2');
    });
});


