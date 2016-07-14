package com.jtbdevelopment.TwistedWordSearch.state

import com.jtbdevelopment.games.state.scoring.GameScorer
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 7/13/16
 * Time: 9:27 PM
 */
@CompileStatic
@Component
class TWSGameScorer implements GameScorer<TWSGame> {
    TWSGame scoreGame(final TWSGame game) {
        return game
    }
}
