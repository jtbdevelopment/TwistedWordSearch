package com.jtbdevelopment.TwistedWordSearch.factory

import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.TwistedWordSearch.state.TWSGameFeature
import com.jtbdevelopment.games.factory.AbstractMultiPlayerGameFactory
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 7/13/16
 * Time: 9:30 PM
 */
@Component
@CompileStatic
class TWSGameFactory extends AbstractMultiPlayerGameFactory<TWSGame, TWSGameFeature> {
    protected TWSGame newGame() {
        return new TWSGame()
    }
}
