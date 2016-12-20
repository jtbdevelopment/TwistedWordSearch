package com.jtbdevelopment.TwistedWordSearch.factory.initializers

import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.games.factory.GameInitializer
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 12/20/16
 * Time: 6:40 PM
 */
@Component
@CompileStatic
class HintsRemainingInitializer implements GameInitializer<TWSGame> {
    void initializeGame(final TWSGame game) {
        game.hintsRemaining = (game.numberOfWords / 4).intValue()
    }

    int getOrder() {
        return DEFAULT_ORDER
    }
}
