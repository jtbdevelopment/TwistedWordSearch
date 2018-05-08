package com.jtbdevelopment.TwistedWordSearch.state

import com.jtbdevelopment.games.mongo.MongoGameCoreTestCase
import org.junit.Test

/**
 * Date: 7/13/16
 * Time: 9:28 PM
 */
class TWSGameScorerTest extends MongoGameCoreTestCase {

    TWSGameScorer scorer = new TWSGameScorer()

    @Test
    void testScoreGameReturnsGameWithSingleWinner() {
        TWSGame game = new TWSGame()
        game.scores = [(PONE.id): 34]

        assert game.is(scorer.scoreGame(game))
        assert [PONE.id] == game.winners
    }

    @Test
    void testScoreGameReturnsGameWithMultipleWinners() {
        TWSGame game = new TWSGame()
        game.scores = [(PONE.id): 34, (PTWO.id): 38, (PTHREE.id): 15, (PFOUR.id): 38]

        assert game.is(scorer.scoreGame(game))
        assert [PTWO.id, PFOUR.id] == game.winners
    }
}
