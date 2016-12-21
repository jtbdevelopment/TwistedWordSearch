package com.jtbdevelopment.TwistedWordSearch.rest.handlers

import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.games.players.Player
import com.jtbdevelopment.games.rest.handlers.AbstractGameActionHandler
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 12/20/16
 * Time: 6:34 PM
 */
@CompileStatic
@Component
class GiveHintHandler extends AbstractGameActionHandler<Integer, TWSGame> {
    //  will be ignoring param
    protected TWSGame handleActionInternal(final Player player, final TWSGame game, final Integer param) {
        return game
    }
}

