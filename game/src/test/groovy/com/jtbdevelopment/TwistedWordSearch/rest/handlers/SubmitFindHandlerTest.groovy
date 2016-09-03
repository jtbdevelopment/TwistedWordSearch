package com.jtbdevelopment.TwistedWordSearch.rest.handlers

import com.jtbdevelopment.TwistedWordSearch.exceptions.InvalidStartCoordinateException
import com.jtbdevelopment.TwistedWordSearch.exceptions.InvalidWordFindCoordinatesException
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
}
