package com.jtbdevelopment.TwistedWordSearch.factory.initializers

import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.TwistedWordSearch.state.grid.GridCoordinate
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

    protected void wordPlacedAt(
            final TWSGame game, final String word, final GridCoordinate start, final GridCoordinate end) {
        game.wordStarts[word] = start
        game.wordEnds[word] = end
    }
}
