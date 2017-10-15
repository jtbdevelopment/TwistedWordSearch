package com.jtbdevelopment.TwistedWordSearch.state

import com.jtbdevelopment.games.state.GamePhase
import com.jtbdevelopment.games.state.transition.AbstractMPGamePhaseTransitionEngine
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 7/13/16
 * Time: 9:26 PM
 */
@CompileStatic
@Component
class GamePhaseTransitionEngine extends AbstractMPGamePhaseTransitionEngine<TWSGame> {
    @Override
    protected TWSGame evaluateSetupPhase(final TWSGame game) {
        return changeStateAndReevaluate(GamePhase.Playing, game)
    }

    @Override
    protected TWSGame evaluatePlayingPhase(final TWSGame game) {
        if (game.wordsToFind.empty) {
            return changeStateAndReevaluate(GamePhase.RoundOver, game)
        }
        return (TWSGame) super.evaluatePlayingPhase(game)
    }
}
