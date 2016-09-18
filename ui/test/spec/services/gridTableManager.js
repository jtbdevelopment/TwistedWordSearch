'use strict';

describe('Service: gridTableManager', function () {
    // load the controller's module
    beforeEach(module('twsUI.services'));

    var service, $gridOffsetTracker;
    beforeEach(inject(function ($injector) {
        //  not mocking, functionality is straight foward enough while dealing with edge cases
        $gridOffsetTracker = $injector.get('gridOffsetTracker');
        service = $injector.get('gridTableManager');
    }));

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
        $gridOffsetTracker.reset();
        $gridOffsetTracker.gridSize(4, 5);
    });

    it('simple compute grid', function () {
        var data = service.updateForGame(game);
        expect(data.cells).toEqual(game.grid);
        expect(data.styles).toEqual([
            ['found-word ', '', '', '', ''],
            ['', 'found-word ', '', '', ''],
            ['', '', '', '', ''],
            ['found-word ', 'found-word ', '', '', 'found-word ']
        ]);
    });

    it('simple compute grid with offsets', function () {
        $gridOffsetTracker.shiftUp(1);
        $gridOffsetTracker.shiftRight(2);
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
        $gridOffsetTracker.shiftUp(1);
        $gridOffsetTracker.shiftRight(2);
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
        $gridOffsetTracker.shiftUp(2);
        $gridOffsetTracker.shiftRight(2);
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
        $gridOffsetTracker.shiftUp(2);
        $gridOffsetTracker.shiftRight(2);
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
});

