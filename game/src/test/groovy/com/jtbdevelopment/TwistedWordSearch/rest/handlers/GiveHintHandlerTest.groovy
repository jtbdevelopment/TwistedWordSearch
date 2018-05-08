package com.jtbdevelopment.TwistedWordSearch.rest.handlers

import com.jtbdevelopment.TwistedWordSearch.exceptions.NoHintsRemainException
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.TwistedWordSearch.state.grid.Grid
import com.jtbdevelopment.TwistedWordSearch.state.grid.GridCoordinate
import com.jtbdevelopment.games.exceptions.input.GameIsNotInPlayModeException
import com.jtbdevelopment.games.mongo.MongoGameCoreTestCase
import com.jtbdevelopment.games.state.GamePhase
import org.junit.Test

/**
 * Date: 12/20/16
 * Time: 9:21 PM
 */
class GiveHintHandlerTest extends MongoGameCoreTestCase {
    GiveHintHandler handler = new GiveHintHandler(null, null, null, null, null, null)

    @Test
    void testIgnoresNonPlayingGames() {
        TWSGame game = new TWSGame()
        game.wordsToFind = ['FIND']
        game.grid = new Grid(10, 10)
        game.wordStarts = ['FIND': new GridCoordinate(3, 3)]
        game.scores = [(PONE.id): 10]

        GamePhase.values().findAll { it != GamePhase.Playing }.each {
            game.gamePhase = it
            try {
                handler.handleActionInternal(PONE, game, null)
            } catch (GameIsNotInPlayModeException ignore) {
                return
            }

            fail('Should have exceptioned')
        }
    }

    @Test
    void testFailsIfNotHintsRemain() {
        TWSGame game = new TWSGame()
        game.wordsToFind = ['FIND']
        game.grid = new Grid(10, 10)
        game.wordStarts = ['FIND': new GridCoordinate(3, 3)]
        game.scores = [(PONE.id): 10]
        game.gamePhase = GamePhase.Playing
        game.hintsRemaining = 0

        try {
            handler.handleActionInternal(PONE, game, null)
        } catch (NoHintsRemainException ignore) {
            return
        }

        fail('should have exceptioned')
    }

    @Test
    void testGivesAHintForSingleWordPuzzle() {
        TWSGame game = new TWSGame()
        game.wordsToFind = ['FIND']
        game.grid = new Grid(10, 10)
        game.gamePhase = GamePhase.Playing
        game.hintsRemaining = 2
        game.hintsTaken = [(PONE.id): 4]
        game.wordStarts = ['FIND': new GridCoordinate(3, 3)]
        game.scores = [(PONE.id): 10]

        TWSGame r = handler.handleActionInternal(PONE, game, null)
        assert r.is(game)

        assert 8 == game.scores[PONE.id]
        assert 1 == game.hintsGiven.size()
        assert 1 == game.hintsRemaining
        assert game.hintsGiven.containsKey('FIND')
        def c = game.hintsGiven['FIND']
        assert 2 <= c.column
        assert 4 >= c.column
        assert 2 <= c.row
        assert 4 >= c.row
        assert 5 == game.hintsTaken[PONE.id]
    }

    @Test
    void testGivesAHintForTwoWordPuzzleEventuallyReturnsBoth() {
        TWSGame game = new TWSGame()
        game.wordsToFind = ['FIND', 'ME']
        game.grid = new Grid(10, 10)
        game.gamePhase = GamePhase.Playing
        game.wordStarts = ['FIND': new GridCoordinate(3, 3), 'ME': new GridCoordinate(0, 9)]
        game.scores = [(PONE.id): 10]
        game.hintsRemaining = 2
        game.hintsTaken = [(PONE.id): 0]

        TWSGame r = handler.handleActionInternal(PONE, game, null)
        assert r.is(game)
        assert 1 == game.hintsTaken[PONE.id]
        r = handler.handleActionInternal(PONE, game, null)
        assert r.is(game)

        assert 7 == game.scores[PONE.id]
        assert 2 == game.hintsGiven.size()
        assert 0 == game.hintsRemaining
        assert 2 == game.hintsTaken[PONE.id]
        assert game.hintsGiven.containsKey('FIND')
        assert game.hintsGiven.containsKey('ME')
        def c = game.hintsGiven['FIND']
        assert 2 <= c.column
        assert 4 >= c.column
        assert 2 <= c.row
        assert 4 >= c.row
        c = game.hintsGiven['ME']
        assert 0 <= c.row
        assert 2 >= c.row
        assert 7 <= c.column
        assert 9 >= c.column
    }
}
