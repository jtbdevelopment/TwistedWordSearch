package com.jtbdevelopment.TwistedWordSearch.factory.initializers

import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.games.factory.GameInitializer
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 9/23/16
 * Time: 10:31 PM
 */
@Component
@CompileStatic
class HintsTakenInitializer implements GameInitializer<TWSGame> {
    void initializeGame(final TWSGame game) {
        game.hintsTaken = game.players.collectEntries {
            return [(it.id): 0]
        }
    }

    int getOrder() {
        return DEFAULT_ORDER
    }
}
