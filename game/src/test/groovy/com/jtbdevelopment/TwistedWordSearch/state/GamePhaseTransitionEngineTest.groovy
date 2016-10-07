package com.jtbdevelopment.TwistedWordSearch.state

import com.jtbdevelopment.games.state.GamePhase

/**
 * Date: 8/12/16
 * Time: 4:54 PM
 */
class GamePhaseTransitionEngineTest extends GroovyTestCase {
    TWSGame scored = new TWSGame()
    TWSGame expectedGame;
    def scorer = [
            scoreGame: {
                TWSGame game ->
                    assert expectedGame.is(game)
                    scored
            }
    ] as TWSGameScorer
    GamePhaseTransitionEngine engine = new GamePhaseTransitionEngine(gameScorer: scorer)

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

    void testEvaluatePlayingWhileWordsToFindEmptyEndsGameAndScoresIt() {
        TWSGame game = new TWSGame(gamePhase: GamePhase.Playing, wordsToFind: [] as Set)
        expectedGame = game
        def g = engine.evaluateSetupPhase(game)
        assert g.is(scored)
        assert GamePhase.RoundOver == game.gamePhase
    }
}
