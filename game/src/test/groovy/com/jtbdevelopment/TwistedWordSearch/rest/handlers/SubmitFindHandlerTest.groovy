package com.jtbdevelopment.TwistedWordSearch.rest.handlers

import com.jtbdevelopment.TwistedWordSearch.exceptions.InvalidStartCoordinateException
import com.jtbdevelopment.TwistedWordSearch.exceptions.InvalidWordFindCoordinatesException
import com.jtbdevelopment.TwistedWordSearch.exceptions.NoWordToFindAtCoordinatesException
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.TwistedWordSearch.state.grid.Grid
import com.jtbdevelopment.TwistedWordSearch.state.grid.GridCoordinate
import com.jtbdevelopment.games.mongo.MongoGameCoreTestCase

/**
 * Date: 9/3/2016
 * Time: 3:44 PM
 */
class SubmitFindHandlerTest extends MongoGameCoreTestCase {
    SubmitFindHandler handler = new SubmitFindHandler();
    TWSGame game

    @Override
    void setUp() throws Exception {
        super.setUp()
        game = new TWSGame(
                grid: new Grid(10, 10),
                players: [PONE],
                wordsFoundByPlayer: [(PONE.id): [] as Set],
                wordsToFind: ['WORD', 'WRAPPED', 'AT'] as Set,
                foundWordLocations: [:])
        (0..game.grid.rowUpperBound).each {
            int row ->
                (0..game.grid.columnUpperBound).each {
                    int col ->
                        game.grid.setGridCell(row, col, 'A' as char)
                }
        }
        game.grid.setGridCell(0, 0, Grid.SPACE)
        game.grid.setGridCell(0, 1, 'W' as char)
        game.grid.setGridCell(0, 2, 'O' as char)
        game.grid.setGridCell(0, 3, 'R' as char)
        game.grid.setGridCell(0, 4, 'D' as char)

        game.grid.setGridCell(2, 7, 'W' as char)
        game.grid.setGridCell(1, 8, 'R' as char)
        game.grid.setGridCell(0, 9, 'A' as char)
        game.grid.setGridCell(9, 0, 'P' as char)
        game.grid.setGridCell(8, 1, 'P' as char)
        game.grid.setGridCell(7, 2, 'E' as char)
        game.grid.setGridCell(6, 3, 'D' as char)

        game.grid.setGridCell(8,8, 'A' as char)
        game.grid.setGridCell(8,7, 'T' as char)
        game.scores = [(PONE.id): 0]
    }


    void testInvalidWordFindCoordinatesIfSetEmpty() {
        shouldFail(InvalidWordFindCoordinatesException.class) {
            handler.handleActionInternal(PONE, game, [])
        }
    }

    void testInvalidWordFindCoordinatesIfSetNull() {
        shouldFail(InvalidWordFindCoordinatesException.class) {
            handler.handleActionInternal(PONE, game, null)
        }
    }

    void testInvalidWordStartExceptionIfOutsideGrid() {
        shouldFail(InvalidStartCoordinateException.class) {
            handler.handleActionInternal(PONE, game, [new GridCoordinate(-1, 5), new GridCoordinate(0, 1)])
        }
        shouldFail(InvalidStartCoordinateException.class) {
            handler.handleActionInternal(PONE, game, [new GridCoordinate(5, -3), new GridCoordinate(0, 1)])
        }
        shouldFail(InvalidStartCoordinateException.class) {
            handler.handleActionInternal(PONE, game, [new GridCoordinate(5, 10), new GridCoordinate(0, 1)])
        }
        shouldFail(InvalidStartCoordinateException.class) {
            handler.handleActionInternal(PONE, game, [new GridCoordinate(11, 5), new GridCoordinate(0, 1)])
        }
    }

    void testInvalidWordStartExceptionIfStartingOnSpace() {
        shouldFail(InvalidStartCoordinateException.class) {
            handler.handleActionInternal(PONE, game, [new GridCoordinate(0, 0), new GridCoordinate(0, 1)])
        }
    }

    void testInvalidWordFindCoordinatesIfNoSecondCoordinate() {
        shouldFail(InvalidWordFindCoordinatesException.class) {
            handler.handleActionInternal(PONE, game, [new GridCoordinate(5, 5)])
        }
    }

    void testnvalidWordFindCoordinatesIfListLongerThanBiggestBoundary() {
        shouldFail(InvalidWordFindCoordinatesException.class) {
            handler.handleActionInternal(
                    PONE,
                    new TWSGame(grid: new Grid(5, 3)),
                    [new GridCoordinate(0, 0), new GridCoordinate(1, 1), new GridCoordinate(1, 1), new GridCoordinate(1, 1), new GridCoordinate(1,1), new GridCoordinate(1,1)])
        }
        shouldFail(InvalidWordFindCoordinatesException.class) {
            handler.handleActionInternal(
                    PONE,
                    new TWSGame(grid: new Grid(3, 4)),
                    [new GridCoordinate(0, 0), new GridCoordinate(1, 1), new GridCoordinate(1, 1), new GridCoordinate(1, 1), new GridCoordinate(1,1), new GridCoordinate(1,1)])
        }
    }

    void testInvalidWordFindCoordinatesIfSecondCoordinateIsNotAProperMoveOneOrZero() {
        shouldFail(InvalidWordFindCoordinatesException.class) {
            handler.handleActionInternal(PONE, game, [new GridCoordinate(5, 5), new GridCoordinate(0, 0)])
        }
        shouldFail(InvalidWordFindCoordinatesException.class) {
            handler.handleActionInternal(PONE, game, [new GridCoordinate(5, 5), new GridCoordinate(-2, 0)])
        }
        shouldFail(InvalidWordFindCoordinatesException.class) {
            handler.handleActionInternal(PONE, game, [new GridCoordinate(5, 5), new GridCoordinate(3, 1)])
        }
        shouldFail(InvalidWordFindCoordinatesException.class) {
            handler.handleActionInternal(PONE, game, [new GridCoordinate(5, 5), new GridCoordinate(1, 2)])
        }
        shouldFail(InvalidWordFindCoordinatesException.class) {
            handler.handleActionInternal(PONE, game, [new GridCoordinate(5, 5), new GridCoordinate(1, -4)])
        }
    }

    void testInvalidWordFindCoordinatesIfRemainingCoordinatesNotInALine() {
        shouldFail(InvalidWordFindCoordinatesException.class) {
            handler.handleActionInternal(PONE, game, [new GridCoordinate(5, 5), new GridCoordinate(0, 1), new GridCoordinate(0, 0), new GridCoordinate(1, 1)])
        }
        shouldFail(InvalidWordFindCoordinatesException.class) {
            handler.handleActionInternal(PONE, game, [new GridCoordinate(5, 5), new GridCoordinate(0, 1), new GridCoordinate(0, 1), new GridCoordinate(1, 1)])
        }
        shouldFail(InvalidWordFindCoordinatesException.class) {
            handler.handleActionInternal(PONE, game, [new GridCoordinate(5, 5), new GridCoordinate(0, -1), new GridCoordinate(0, -1), new GridCoordinate(1, -1)])
        }
        shouldFail(InvalidWordFindCoordinatesException.class) {
            handler.handleActionInternal(PONE, game, [new GridCoordinate(5, 5), new GridCoordinate(1, 0), new GridCoordinate(1, 0), new GridCoordinate(1, -1)])
        }
        shouldFail(InvalidWordFindCoordinatesException.class) {
            handler.handleActionInternal(PONE, game, [new GridCoordinate(5, 5), new GridCoordinate(-1, 0), new GridCoordinate(-1, 0), new GridCoordinate(-1, -1)])
        }
        shouldFail(InvalidWordFindCoordinatesException.class) {
            handler.handleActionInternal(PONE, game, [new GridCoordinate(5, 5), new GridCoordinate(1, 1), new GridCoordinate(1, 1), new GridCoordinate(1, 0)])
        }
        shouldFail(InvalidWordFindCoordinatesException.class) {
            handler.handleActionInternal(PONE, game, [new GridCoordinate(5, 5), new GridCoordinate(1, -1), new GridCoordinate(1, -1), new GridCoordinate(1, 0)])
        }
        shouldFail(InvalidWordFindCoordinatesException.class) {
            handler.handleActionInternal(PONE, game, [new GridCoordinate(5, 5), new GridCoordinate(-1, 1), new GridCoordinate(-1, 1), new GridCoordinate(-1, 0)])
        }
        shouldFail(InvalidWordFindCoordinatesException.class) {
            handler.handleActionInternal(PONE, game, [new GridCoordinate(5, 5), new GridCoordinate(-1, -1), new GridCoordinate(-1, -1), new GridCoordinate(-1, 0)])
        }
    }

    void testNoWordFoundExceptionIfNotActuallyGood() {
        shouldFail(NoWordToFindAtCoordinatesException.class) {
            handler.handleActionInternal(PONE, game, [new GridCoordinate(9, 9), new GridCoordinate(0, -1)])
        }
    }

    void testAbleToFindWordCoordinatesGivenInForwardDirection() {
        TWSGame update = handler.handleActionInternal(PONE, game, [new GridCoordinate(8, 8), new GridCoordinate(0, -1)])
        assert update.is(game)
        assert ['WORD', 'WRAPPED'] as Set == update.wordsToFind
        assert [(PONE.id): ['AT'] as Set] == update.wordsFoundByPlayer
        assert ['AT': [new GridCoordinate(8, 8), new GridCoordinate(8, 7)] as Set] == update.foundWordLocations
        assert [(PONE.id): 2] == update.scores
    }

    void testAbleToFindWordCoordinatesGivenInBackwardDirection() {
        TWSGame update = handler.handleActionInternal(PONE, game, [new GridCoordinate(8, 7), new GridCoordinate(0, 1)])
        assert update.is(game)
        assert ['WORD', 'WRAPPED'] as Set == update.wordsToFind
        assert [(PONE.id): ['AT'] as Set] == update.wordsFoundByPlayer
        assert ['AT': [new GridCoordinate(8, 8), new GridCoordinate(8, 7)] as Set] == update.foundWordLocations
        assert [(PONE.id): 2] == update.scores
    }

    void testAbleToFindWordCoordinatesGivenInWrap() {
        TWSGame update = handler.handleActionInternal(PONE, game, [new GridCoordinate(2, 7), new GridCoordinate(-1, 1), new GridCoordinate(-1, 1), new GridCoordinate(-1, 1), new GridCoordinate(-1, 1), new GridCoordinate(-1, 1), new GridCoordinate(-1, 1)])
        assert update.is(game)
        assert ['WORD', 'AT'] as Set == update.wordsToFind
        assert [(PONE.id): ['WRAPPED'] as Set] == update.wordsFoundByPlayer
        assert ['WRAPPED': [new GridCoordinate(2, 7), new GridCoordinate(1, 8), new GridCoordinate(1, 8), new GridCoordinate(0, 9), new GridCoordinate(9, 0), new GridCoordinate(8, 1), new GridCoordinate(7, 2), new GridCoordinate(6, 3)] as Set] == update.foundWordLocations
        assert [(PONE.id): 7] == update.scores
    }
}
