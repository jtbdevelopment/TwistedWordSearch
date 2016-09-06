package com.jtbdevelopment.TwistedWordSearch.state

import com.jtbdevelopment.games.state.GamePhase

/**
 * Date: 8/12/16
 * Time: 4:54 PM
 */
class GamePhaseTransitionEngineTest extends GroovyTestCase {
    GamePhaseTransitionEngine engine = new GamePhaseTransitionEngine()

    void testEvaluateSetupPhase() {
        TWSGame game = new TWSGame(gamePhase: GamePhase.Setup, wordsToFind: ['TESTING'])
        def g = engine.evaluateSetupPhase(game)
        assert g.is(game)
        assert GamePhase.Playing == g.gamePhase
    }

    void testEvaluatePlayingWhileWordsToFindNotEmptyDoesNothing() {
        TWSGame game = new TWSGame(gamePhase: GamePhase.Playing, wordsToFind: ['LEFT'] as Set)
        def g = engine.evaluateSetupPhase(game)
        assert g.is(game)
        assert GamePhase.Playing == g.gamePhase
    }

    void testEvaluatePlayingWhileWordsToFindEmptyEndsGame() {
        TWSGame game = new TWSGame(gamePhase: GamePhase.Playing, wordsToFind: [] as Set)
        def g = engine.evaluateSetupPhase(game)
        assert g.is(game)
        assert GamePhase.RoundOver == g.gamePhase
    }
}
