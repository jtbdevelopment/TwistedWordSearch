package com.jtbdevelopment.TwistedWordSearch.rest.handlers

import com.jtbdevelopment.TwistedWordSearch.exceptions.InvalidStartCoordinateException
import com.jtbdevelopment.TwistedWordSearch.exceptions.InvalidWordFindCoordinatesException
import com.jtbdevelopment.TwistedWordSearch.exceptions.NoWordToFindAtCoordinatesException
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.TwistedWordSearch.state.grid.Grid
import com.jtbdevelopment.TwistedWordSearch.state.grid.GridCoordinate
import com.jtbdevelopment.games.exceptions.input.GameIsNotInPlayModeException
import com.jtbdevelopment.games.mongo.MongoGameCoreTestCase
import com.jtbdevelopment.games.state.GamePhase
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertFalse
import static org.junit.Assert.fail

/**
 * Date: 9/3/2016
 * Time: 3:44 PM
 */
class SubmitFindHandlerTest extends MongoGameCoreTestCase {
    SubmitFindHandler handler = new SubmitFindHandler(null, null, null, null, null, null)
    TWSGame game

    @Before
    void setUp() throws Exception {
        game = new TWSGame(
                grid: new Grid(10, 10),
                players: [PONE],
                wordsFoundByPlayer: [(PONE.id): [] as Set],
                wordsToFind: ['WORD', 'WRAPPED', 'AT'] as Set,
                gamePhase: GamePhase.Playing,
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

        game.grid.setGridCell(8, 8, 'A' as char)
        game.grid.setGridCell(8, 7, 'T' as char)
        game.scores = [(PONE.id): 0]
    }

    @Test(expected = InvalidWordFindCoordinatesException.class)
    void testInvalidWordFindCoordinatesIfSetEmpty() {
        handler.handleActionInternal(PONE, game, [])
    }

    @Test(expected = InvalidWordFindCoordinatesException.class)
    void testInvalidWordFindCoordinatesIfSetNull() {
        handler.handleActionInternal(PONE, game, null)
    }

    @Test
    void testInvalidWordStartExceptionIfOutsideGrid() {
        try {
            handler.handleActionInternal(PONE, game, [new GridCoordinate(-1, 5), new GridCoordinate(0, 1)])
            fail()
        } catch (InvalidStartCoordinateException ignore) {
            //
        }
        try {
            handler.handleActionInternal(PONE, game, [new GridCoordinate(5, -3), new GridCoordinate(0, 1)])
            fail()
        } catch (InvalidStartCoordinateException ignore) {
            //
        }
        try {
            handler.handleActionInternal(PONE, game, [new GridCoordinate(5, 10), new GridCoordinate(0, 1)])
            fail()
        } catch (InvalidStartCoordinateException ignore) {
            //
        }
        try {
            handler.handleActionInternal(PONE, game, [new GridCoordinate(11, 5), new GridCoordinate(0, 1)])
            fail()
        } catch (InvalidStartCoordinateException ignore) {
            //
        }
    }

    @Test(expected = InvalidStartCoordinateException.class)
    void testInvalidWordStartExceptionIfStartingOnSpace() {
        handler.handleActionInternal(PONE, game, [new GridCoordinate(0, 0), new GridCoordinate(0, 1)])
    }

    @Test(expected = InvalidWordFindCoordinatesException.class)
    void testInvalidWordFindCoordinatesIfNoSecondCoordinate() {
        handler.handleActionInternal(PONE, game, [new GridCoordinate(5, 5)])
    }

    @Test
    void testInvalidWordFindCoordinatesIfListLongerThanBiggestBoundary() {
        try {
            handler.handleActionInternal(
                    PONE,
                    new TWSGame(grid: new Grid(5, 3), gamePhase: GamePhase.Playing),
                    [new GridCoordinate(0, 0), new GridCoordinate(1, 1), new GridCoordinate(1, 1), new GridCoordinate(1, 1), new GridCoordinate(1, 1), new GridCoordinate(1, 1)])
            fail()
        } catch (InvalidWordFindCoordinatesException ignore) {
            //
        }
        try {
            handler.handleActionInternal(
                    PONE,
                    new TWSGame(grid: new Grid(3, 4), gamePhase: GamePhase.Playing),
                    [new GridCoordinate(0, 0), new GridCoordinate(1, 1), new GridCoordinate(1, 1), new GridCoordinate(1, 1), new GridCoordinate(1, 1), new GridCoordinate(1, 1)])
            fail()
        } catch (InvalidWordFindCoordinatesException ignore) {
            //
        }
    }

    @Test
    void testInvalidWordFindCoordinatesIfSecondCoordinateIsNotAProperMoveOneOrZero() {
        try {
            handler.handleActionInternal(PONE, game, [new GridCoordinate(5, 5), new GridCoordinate(0, 0)])
        } catch (InvalidWordFindCoordinatesException ignore) {
            //
        }
        try {
            handler.handleActionInternal(PONE, game, [new GridCoordinate(5, 5), new GridCoordinate(-2, 0)])
        } catch (InvalidWordFindCoordinatesException ignore) {
            //
        }
        try {
            handler.handleActionInternal(PONE, game, [new GridCoordinate(5, 5), new GridCoordinate(3, 1)])
        } catch (InvalidWordFindCoordinatesException ignore) {
            //
        }
        try {
            handler.handleActionInternal(PONE, game, [new GridCoordinate(5, 5), new GridCoordinate(1, 2)])
        } catch (InvalidWordFindCoordinatesException ignore) {
            //
        }
        try {
            handler.handleActionInternal(PONE, game, [new GridCoordinate(5, 5), new GridCoordinate(1, -4)])
        } catch (InvalidWordFindCoordinatesException ignore) {
            //
        }
    }

    @Test
    void testInvalidWordFindCoordinatesIfRemainingCoordinatesNotInALine() {
        try {
            handler.handleActionInternal(PONE, game, [new GridCoordinate(5, 5), new GridCoordinate(0, 1), new GridCoordinate(0, 0), new GridCoordinate(1, 1)])
        } catch (InvalidWordFindCoordinatesException ignore) {
            //
        }
        try {
            handler.handleActionInternal(PONE, game, [new GridCoordinate(5, 5), new GridCoordinate(0, 1), new GridCoordinate(0, 1), new GridCoordinate(1, 1)])
        } catch (InvalidWordFindCoordinatesException ignore) {
            //
        }
        try {
            handler.handleActionInternal(PONE, game, [new GridCoordinate(5, 5), new GridCoordinate(0, -1), new GridCoordinate(0, -1), new GridCoordinate(1, -1)])
        } catch (InvalidWordFindCoordinatesException ignore) {
            //
        }
        try {
            handler.handleActionInternal(PONE, game, [new GridCoordinate(5, 5), new GridCoordinate(1, 0), new GridCoordinate(1, 0), new GridCoordinate(1, -1)])
        } catch (InvalidWordFindCoordinatesException ignore) {
            //
        }
        try {
            handler.handleActionInternal(PONE, game, [new GridCoordinate(5, 5), new GridCoordinate(-1, 0), new GridCoordinate(-1, 0), new GridCoordinate(-1, -1)])
        } catch (InvalidWordFindCoordinatesException ignore) {
            //
        }
        try {
            handler.handleActionInternal(PONE, game, [new GridCoordinate(5, 5), new GridCoordinate(1, 1), new GridCoordinate(1, 1), new GridCoordinate(1, 0)])
        } catch (InvalidWordFindCoordinatesException ignore) {
            //
        }
        try {
            handler.handleActionInternal(PONE, game, [new GridCoordinate(5, 5), new GridCoordinate(1, -1), new GridCoordinate(1, -1), new GridCoordinate(1, 0)])
        } catch (InvalidWordFindCoordinatesException ignore) {
            //
        }
        try {
            handler.handleActionInternal(PONE, game, [new GridCoordinate(5, 5), new GridCoordinate(-1, 1), new GridCoordinate(-1, 1), new GridCoordinate(-1, 0)])
        } catch (InvalidWordFindCoordinatesException ignore) {
            //
        }
        try {
            handler.handleActionInternal(PONE, game, [new GridCoordinate(5, 5), new GridCoordinate(-1, -1), new GridCoordinate(-1, -1), new GridCoordinate(-1, 0)])
        } catch (InvalidWordFindCoordinatesException ignore) {
            //
        }
    }

    @Test
    void testFailsIfGameNotInPlayingMode() {
        GamePhase.values().findAll { it != GamePhase.Playing }.each {
            try {
                game.gamePhase = it
                handler.handleActionInternal(PONE, game, [new GridCoordinate(8, 8), new GridCoordinate(0, -1)])
            } catch (GameIsNotInPlayModeException ignore) {
                return
            }
            fail('Should have exceptioned')
        }
    }

    @Test(expected = NoWordToFindAtCoordinatesException.class)
    void testNoWordFoundExceptionIfNotActuallyGood() {
        handler.handleActionInternal(PONE, game, [new GridCoordinate(9, 9), new GridCoordinate(0, -1)])
    }

    @Test
    void testAbleToFindWordCoordinatesGivenInForwardDirection() {
        TWSGame update = handler.handleActionInternal(PONE, game, [new GridCoordinate(8, 8), new GridCoordinate(0, -1)])
        assert update.is(game)
        assert ['WORD', 'WRAPPED'] as Set == update.wordsToFind
        assert [(PONE.id): ['AT'] as Set] == update.wordsFoundByPlayer
        assert ['AT': [new GridCoordinate(8, 8), new GridCoordinate(8, 7)] as Set] == update.foundWordLocations
        assert [(PONE.id): 2] == update.scores
    }

    @Test
    void testAbleToFindWordCoordinatesGivenInBackwardDirection() {
        TWSGame update = handler.handleActionInternal(PONE, game, [new GridCoordinate(8, 7), new GridCoordinate(0, 1)])
        assert update.is(game)
        assert ['WORD', 'WRAPPED'] as Set == update.wordsToFind
        assert [(PONE.id): ['AT'] as Set] == update.wordsFoundByPlayer
        assert ['AT': [new GridCoordinate(8, 8), new GridCoordinate(8, 7)] as Set] == update.foundWordLocations
        assert [(PONE.id): 2] == update.scores
    }

    @Test
    void testAbleToFindWordCoordinatesGivenInWrap() {
        TWSGame update = handler.handleActionInternal(PONE, game, [new GridCoordinate(2, 7), new GridCoordinate(-1, 1), new GridCoordinate(-1, 1), new GridCoordinate(-1, 1), new GridCoordinate(-1, 1), new GridCoordinate(-1, 1), new GridCoordinate(-1, 1)])
        assert update.is(game)
        assert ['WORD', 'AT'] as Set == update.wordsToFind
        assert [(PONE.id): ['WRAPPED'] as Set] == update.wordsFoundByPlayer
        assert ['WRAPPED': [new GridCoordinate(2, 7), new GridCoordinate(1, 8), new GridCoordinate(1, 8), new GridCoordinate(0, 9), new GridCoordinate(9, 0), new GridCoordinate(8, 1), new GridCoordinate(7, 2), new GridCoordinate(6, 3)] as Set] == update.foundWordLocations
        assert [(PONE.id): 7] == update.scores
    }

    @Test
    void testRemovesRelevantHint() {
        game.hintsGiven = ['AT': new GridCoordinate(1, 1), 'WRAPPED': new GridCoordinate(2, 5)]
        TWSGame update = handler.handleActionInternal(PONE, game, [new GridCoordinate(8, 8), new GridCoordinate(0, -1)])
        assert update.is(game)
        assert ['WORD', 'WRAPPED'] as Set == update.wordsToFind
        assert [(PONE.id): ['AT'] as Set] == update.wordsFoundByPlayer
        assert ['AT': [new GridCoordinate(8, 8), new GridCoordinate(8, 7)] as Set] == update.foundWordLocations
        assert [(PONE.id): 2] == update.scores
        assertFalse game.hintsGiven.containsKey('AT')
        assert ['WRAPPED': new GridCoordinate(2, 5)] == game.hintsGiven
    }


}
