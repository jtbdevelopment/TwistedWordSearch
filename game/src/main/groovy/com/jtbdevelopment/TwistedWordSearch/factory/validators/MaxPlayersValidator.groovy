package com.jtbdevelopment.TwistedWordSearch.factory.validators

import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.games.factory.GameValidator
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 9/24/2016
 * Time: 4:46 PM
 */
@Component
@CompileStatic
class MaxPlayersValidator implements GameValidator<TWSGame> {
    private static final int MAX_PLAYERS = 3;
    boolean validateGame(final TWSGame game) {
        return game.players.size() <= MAX_PLAYERS
    }

    String errorMessage() {
        return "Sorry, there are too many players - maximum is 3."
    }
}
