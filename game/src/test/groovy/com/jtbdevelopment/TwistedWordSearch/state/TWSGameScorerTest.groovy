package com.jtbdevelopment.TwistedWordSearch.state

/**
 * Date: 7/13/16
 * Time: 9:28 PM
 */
class TWSGameScorerTest extends GroovyTestCase {

    TWSGameScorer scorer = new TWSGameScorer()

    void testScoreGameReturnsGame() {
        TWSGame game = new TWSGame()

        assert game.is(scorer.scoreGame(game))
    }
}
