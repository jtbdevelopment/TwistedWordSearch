package com.jtbdevelopment.TwistedWordSearch.factory.initializers

import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.games.factory.GameInitializer
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 8/16/16
 * Time: 6:49 AM
 */
@CompileStatic
@Component
class UsableSquaresInitializer implements GameInitializer<TWSGame> {
    void initializeGame(final TWSGame game) {
        game.usableSquares = game.grid.usableSquaresCount
    }

    int getOrder() {
        return EARLY_ORDER + 20
    }
}
