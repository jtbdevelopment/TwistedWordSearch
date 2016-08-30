package com.jtbdevelopment.TwistedWordSearch.factory.initializers

import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 8/19/16
 * Time: 7:04 PM
 */
@Component
@CompileStatic
class WordPlacementInitializer extends AbstractWordPlacementInitializer {

    int getOrder() {
        return DEFAULT_ORDER + 10
    }

    protected Collection<String> getWordsToPlace(final TWSGame game) {
        return game.words
    }
}
