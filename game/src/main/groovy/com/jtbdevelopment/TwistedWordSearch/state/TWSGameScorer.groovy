package com.jtbdevelopment.TwistedWordSearch.state

import com.jtbdevelopment.games.dao.AbstractPlayerRepository
import com.jtbdevelopment.games.state.scoring.GameScorer
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Date: 7/13/16
 * Time: 9:27 PM
 */
@CompileStatic
@Component
class TWSGameScorer implements GameScorer<TWSGame> {
    @Autowired
    AbstractPlayerRepository playerRepository

    TWSGame scoreGame(final TWSGame game) {
        int maxScore = game.scores.values().max()
        game.winners = game.scores.findAll { it.value == maxScore }.keySet().toList()
        return game
    }
}
