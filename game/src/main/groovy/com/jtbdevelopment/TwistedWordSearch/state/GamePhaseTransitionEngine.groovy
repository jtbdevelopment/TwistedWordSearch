package com.jtbdevelopment.TwistedWordSearch.state

import com.jtbdevelopment.games.state.GamePhase
import com.jtbdevelopment.games.state.transition.AbstractGamePhaseTransitionEngine
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 7/13/16
 * Time: 9:26 PM
 */
@CompileStatic
@Component
class GamePhaseTransitionEngine extends AbstractGamePhaseTransitionEngine<TWSGame> {
    @Override
    protected TWSGame evaluateSetupPhase(final TWSGame game) {
        return changeStateAndReevaluate(GamePhase.Playing, game)
    }
}
