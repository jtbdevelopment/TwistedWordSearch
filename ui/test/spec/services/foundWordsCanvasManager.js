'use strict';

describe('Service: foundWordsCanvasManager', function () {
    // load the controller's module
    beforeEach(module('twsUI.services'));

    var canvasLineDrawer = {
        drawLine: jasmine.createSpy('drawLine')
    };
    beforeEach(module(function ($provide) {
        $provide.factory('canvasLineDrawer', [function () {
            return canvasLineDrawer;
        }]);
    }));

    var context = {
        clearRect: jasmine.createSpy('clearRect'),
        beginPath: jasmine.createSpy('beginPath'),
        closePath: jasmine.createSpy('closePath'),
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
        context.beginPath.calls.reset();
        context.closePath.calls.reset();
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
        }
    };

    beforeEach(function () {
        gridOffsetTracker.reset();
        gridOffsetTracker.gridSize(4, 5);
    });

    it('simple compute lines', function () {
        service.updateForGame(game, 4, 5);
        $timeout.flush();
        expect(context.beginPath.calls.count()).toEqual(1);
        expect(context.clearRect).toHaveBeenCalledWith(0, 0, canvas.width, canvas.height);
        expect(canvasLineDrawer.drawLine).toHaveBeenCalledWith(
            context,
            {row: 3, column: 0},
            {row: 3, column: 1},
            102,
            64.4,
            '#C1D37F');
        expect(canvasLineDrawer.drawLine).toHaveBeenCalledWith(
            context,
            {row: 1, column: 1},
            {row: 0, column: 0},
            102,
            64.4,
            '#C1D37F');
        expect(canvasLineDrawer.drawLine).toHaveBeenCalledWith(
            context,
            {row: 3, column: 4},
            {row: 3, column: 4},
            102,
            64.4,
            '#C1D37F');
        expect(context.closePath.calls.count()).toEqual(1);
    });

    it('simple lines with offset', function () {
        gridOffsetTracker.shiftUp(1);
        gridOffsetTracker.shiftRight(2);
        service.updateForGame(game, 4, 5);
        $timeout.flush();
        expect(context.beginPath.calls.count()).toEqual(1);
        expect(context.clearRect).toHaveBeenCalledWith(0, 0, canvas.width, canvas.height);
        expect(canvasLineDrawer.drawLine).toHaveBeenCalledWith(
            context,
            {row: 2, column: 2},
            {row: 2, column: 3},
            102,
            64.4,
            '#C1D37F');
        expect(canvasLineDrawer.drawLine).toHaveBeenCalledWith(
            context,
            {row: 0, column: 3},
            {row: 0, column: 3},
            102,
            64.4,
            '#C1D37F');
        expect(canvasLineDrawer.drawLine).toHaveBeenCalledWith(
            context,
            {row: 3, column: 2},
            {row: 2, column: 1},
            102,
            64.4,
            '#C1D37F');
        expect(context.closePath.calls.count()).toEqual(1);
    });

    it('recomputes canvas on offset changes', function () {
        service.updateForGame(game, 4, 5);
        $timeout.flush();
        expect(context.beginPath.calls.count()).toEqual(1);
        expect(context.clearRect).toHaveBeenCalledWith(0, 0, canvas.width, canvas.height);
        expect(canvasLineDrawer.drawLine).toHaveBeenCalledWith(
            context,
            {row: 3, column: 0},
            {row: 3, column: 1},
            102,
            64.4,
            '#C1D37F');
        expect(canvasLineDrawer.drawLine).toHaveBeenCalledWith(
            context,
            {row: 1, column: 1},
            {row: 0, column: 0},
            102,
            64.4,
            '#C1D37F');
        expect(canvasLineDrawer.drawLine).toHaveBeenCalledWith(
            context,
            {row: 3, column: 4},
            {row: 3, column: 4},
            102,
            64.4,
            '#C1D37F');
        expect(context.closePath.calls.count()).toEqual(1);

        context.beginPath.calls.reset();
        context.closePath.calls.reset();
        context.clearRect.calls.reset();
        canvasLineDrawer.drawLine.calls.reset();

        gridOffsetTracker.shiftUp(1);
        $timeout.flush();
        expect(context.beginPath.calls.count()).toEqual(1);
        expect(context.clearRect).toHaveBeenCalledWith(0, 0, canvas.width, canvas.height);
        expect(context.closePath.calls.count()).toEqual(1);
        // ignoring intermediate result line details
        context.beginPath.calls.reset();
        context.closePath.calls.reset();
        context.clearRect.calls.reset();
        canvasLineDrawer.drawLine.calls.reset();

        gridOffsetTracker.shiftRight(2);
        $timeout.flush();

        expect(context.beginPath.calls.count()).toEqual(1);
        expect(context.clearRect).toHaveBeenCalledWith(0, 0, canvas.width, canvas.height);
        expect(canvasLineDrawer.drawLine).toHaveBeenCalledWith(
            context,
            {row: 2, column: 2},
            {row: 2, column: 3},
            102,
            64.4,
            '#C1D37F');
        expect(canvasLineDrawer.drawLine).toHaveBeenCalledWith(
            context,
            {row: 0, column: 3},
            {row: 0, column: 3},
            102,
            64.4,
            '#C1D37F');
        expect(canvasLineDrawer.drawLine).toHaveBeenCalledWith(
            context,
            {row: 3, column: 2},
            {row: 2, column: 1},
            102,
            64.4,
            '#C1D37F');
        expect(context.closePath.calls.count()).toEqual(1);
    });
    /*
     it('simple compute grid with offsets', function () {
     gridOffsetTracker.shiftUp(1);
     gridOffsetTracker.shiftRight(2);
     var data = service.updateForGame(game);
     expect(data.cells).toEqual([
     [' ', 'I', 'F', 'G', 'H'],
     [' ', 'M', 'J', 'K', 'L'],
     ['Q', 'R', 'N', 'O', 'P'],
     ['D', 'E', 'A', 'B', 'C']
     ]);
     expect(data.styles).toEqual([
     ['', '', '', 'found-word ', ''],
     ['', '', '', '', ''],
     ['', 'found-word ', 'found-word ', 'found-word ', ''],
     ['', '', 'found-word ', '', '']
     ]);
     });

     it('results change as shifts are called', function () {
     var data = service.updateForGame(game);
     expect(data.cells).toEqual(game.grid);
     expect(data.styles).toEqual([
     ['found-word ', '', '', '', ''],
     ['', 'found-word ', '', '', ''],
     ['', '', '', '', ''],
     ['found-word ', 'found-word ', '', '', 'found-word ']
     ]);
     gridOffsetTracker.shiftUp(1);
     gridOffsetTracker.shiftRight(2);
     expect(data.cells).toEqual([
     [' ', 'I', 'F', 'G', 'H'],
     [' ', 'M', 'J', 'K', 'L'],
     ['Q', 'R', 'N', 'O', 'P'],
     ['D', 'E', 'A', 'B', 'C']
     ]);
     expect(data.styles).toEqual([
     ['', '', '', 'found-word ', ''],
     ['', '', '', '', ''],
     ['', 'found-word ', 'found-word ', 'found-word ', ''],
     ['', '', 'found-word ', '', '']
     ]);
     });

     it('add found word style to shifted grid', function () {
     gridOffsetTracker.shiftUp(2);
     gridOffsetTracker.shiftRight(2);
     var data = service.updateForGame(game);
     service.addSelectedStyleToCoordinates([{row: 0, column: 0}, {row: 1, column: 1}]);
     expect(data.cells).toEqual([
     [' ', 'M', 'J', 'K', 'L'],
     ['Q', 'R', 'N', 'O', 'P'],
     ['D', 'E', 'A', 'B', 'C'],
     [' ', 'I', 'F', 'G', 'H']
     ]);
     expect(data.styles).toEqual([
     ['current-selection ', '', '', '', ''],
     ['', 'found-word current-selection ', 'found-word ', 'found-word ', ''],
     ['', '', 'found-word ', '', ''],
     ['', '', '', 'found-word ', '']
     ]);
     });

     it('remove found word style to shifted grid', function () {
     gridOffsetTracker.shiftUp(2);
     gridOffsetTracker.shiftRight(2);
     var data = service.updateForGame(game);
     service.addSelectedStyleToCoordinates([{row: 0, column: 0}, {row: 1, column: 1}, {row: 3, column: 4}]);
     service.removeSelectedStyleFromCoordinates([{row: 1, column: 1}]);
     expect(data.cells).toEqual([
     [' ', 'M', 'J', 'K', 'L'],
     ['Q', 'R', 'N', 'O', 'P'],
     ['D', 'E', 'A', 'B', 'C'],
     [' ', 'I', 'F', 'G', 'H']
     ]);
     expect(data.styles).toEqual([
     ['current-selection ', '', '', '', ''],
     ['', 'found-word ', 'found-word ', 'found-word ', ''],
     ['', '', 'found-word ', '', ''],
     ['', '', '', 'found-word ', 'current-selection ']
     ]);
     });
     */
    it('dummy test', function () {

    });
});


