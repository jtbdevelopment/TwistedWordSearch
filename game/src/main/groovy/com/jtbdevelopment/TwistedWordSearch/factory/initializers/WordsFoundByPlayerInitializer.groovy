package com.jtbdevelopment.TwistedWordSearch.factory.initializers

import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.games.factory.GameInitializer
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 8/30/16
 * Time: 3:26 PM
 */
@Component
@CompileStatic
class WordsFoundByPlayerInitializer implements GameInitializer<TWSGame> {
    void initializeGame(final TWSGame game) {
        game.wordsFoundByPlayer = game.players.collectEntries {
            [(it.id): [] as Set]
        }
        game.foundWordLocations = [:]
    }

    int getOrder() {
        return DEFAULT_ORDER
    }
}
